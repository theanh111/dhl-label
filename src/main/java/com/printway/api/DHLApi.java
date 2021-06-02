package com.printway.api;

import com.google.gson.Gson;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.printway.api.entity.*;
import com.printway.entityDB.OrderInfo;
import com.printway.entityDB.OrderMeasure;
import com.printway.entityDB.OrderMaterial;
import com.printway.randomCode.CodeGenerator;
import com.printway.service.OrderLabelService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class DHLApi {
    private final String DHL_EXPRESS_API = "http://wsbexpress.dhl.com/rest/sndpt/ShipmentRequest";
    private final String USERNAME = "printwaycolVN";
    private final String PASSWORD = "V@2cY^0wB$9s";
    private final String DHL_ACCOUNT = "527255451";
    private final String CURRENCY = "USD";
    private final String UNIT_OF_MEASUREMENT = "SI";
    private final String LABEL_TYPE = "PDF";
    //    private final String LABEL_TEMPLATE = "ECOM26_A6_002";
    private final String LABEL_TEMPLATE = "ECOM26_84_001";
    private final String ARCHIVE_LABEL_TEMPLATE = "ARCH_6X4";
    private final String SERVICE_TYPE = "P";
    private final String DROP_OFF_TYPE = "REGULAR_PICKUP";
    private final String SHIPPER_NAME = "PRINTWAY FACTORY";
    private final String SHIPPER_COMPANY = "FULFILLMENT ONLY, NOT SELLER";
    private final String SHIPPER_STREETLINES = "Trung Van";
    private final String SHIPPER_CITY = "Hanoi";
    private final String SHIPPER_COUNTRYCODE = "VN";
    private final String SHIPPER_PHONE = "0898588077";
    private final String SHIPPER_POSTALCODE = "100000";
    private final String DELIVERY_AT_PLACE = "DAP";
    private final String TIME_ZONE = "GMT+07:00";

    private OrderLabelService orderLabelService = new OrderLabelService();

    private CodeGenerator codeGenerator = new CodeGenerator();
    private Gson gson = new Gson();

    public void sendRequest(OrderInfo orderInfo, List<OrderMaterial> orderMaterialList, String fullDir, String folder_label, OrderMeasure orderMeasure) {
        try {
            boolean hasOrderId = orderLabelService.checkOrderIdInTBLLabel(orderInfo.getOrder_id());
            String code = null;
            String trackingNumber = null;
            String partnerTrackingNumber = null;
            BodyRequest bodyRequest = createRequestBody(orderInfo, orderMeasure, orderMaterialList);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(DHL_EXPRESS_API);
            String auth = USERNAME + ":" + PASSWORD;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            httpPost.setEntity(new StringEntity(gson.toJson(bodyRequest), ContentType.APPLICATION_JSON));
//            System.out.println("- Body Request: " + gson.toJson(bodyRequest));
            HttpResponse httpResponse = client.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("- HTTP Status Code: " + statusCode);
            if (statusCode == 200) {
                String content = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
                JSONObject responseObj = new JSONObject(content);
//                System.out.println("- Response: " + responseObj);
                if (responseObj.has("ShipmentResponse")) {
                    JSONObject shipmentResponse = new JSONObject(responseObj.get("ShipmentResponse").toString());
                    if (shipmentResponse.has("Notification")) {
                        JSONArray notifications = new JSONArray(shipmentResponse.get("Notification").toString());
                        for (int i = 0; i < notifications.length(); i++) {
                            if (notifications.length() == 1) {
                                JSONObject notification = new JSONObject(notifications.get(i).toString());
                                code = notification.get("@code").toString();
                                System.out.println("- Response [@code]: " + code);
                            }
                            if (notifications.length() > 1) {
                                JSONObject notification = new JSONObject(notifications.get(i).toString());
                                System.out.println("- ErrorCode: " + notification.get("@code").toString());
                                System.out.println("- ErrorMessage: " + notification.get("Message").toString());

                            }
                        }
                        orderLabelService.updateLog(shipmentResponse.get("Notification").toString(), orderInfo.getOrder_id());
                    }

                    if (shipmentResponse.has("Notification")) {
                        if (code != null && code.equals("0")) {
                            List<String> trackingNumberList = orderLabelService.findAllTrackingNumberTBLNEWTRACKING();
                            trackingNumber = returnCodeGenerator(trackingNumberList);
                            if (shipmentResponse.has("PackagesResult")) {
                                JSONObject packagesResult = new JSONObject(shipmentResponse.get("PackagesResult").toString());
                                JSONArray packageResultArr = new JSONArray(packagesResult.get("PackageResult").toString());
                                for (int i = 0; i < packageResultArr.length(); i++) {
                                    JSONObject result = new JSONObject(packageResultArr.get(i).toString());
                                    partnerTrackingNumber = result.get("TrackingNumber").toString();
                                }
                                System.out.println("- Tracking Number: " + partnerTrackingNumber);
                            }
                            if (shipmentResponse.has("LabelImage")) {
                                JSONArray labels = new JSONArray(shipmentResponse.get("LabelImage").toString());
                                String labelPDFBase64 = null;
                                String path = null;
                                String pathFileTemp = null;
                                for (int i = 0; i < labels.length(); i++) {
                                    String label = labels.get(i).toString();
                                    JSONObject labelImage = new JSONObject(label);
                                    labelPDFBase64 = labelImage.get("GraphicImage").toString();
                                    byte[] decodedBytes = new BASE64Decoder().decodeBuffer(labelPDFBase64);
                                    String tempFile = i + "-temp";
                                    File of = new File(fullDir + "/" + tempFile + ".pdf");
                                    FileOutputStream osf = new FileOutputStream(of);
                                    osf.write(decodedBytes);
                                    osf.flush();
                                    osf.close();

                                    // Get Only Page 1 of Label PDF File
                                    PdfReader reader = new PdfReader(fullDir + "/" + tempFile + ".pdf");
                                    int pdfPages = reader.getNumberOfPages();
                                    reader.close();

                                    String pathFile = null;
                                    PdfStamper stamper;
                                    for (int j = 1; j <= pdfPages; j++) {
                                        if (j == 1) {
                                            path = fullDir + "/" + orderInfo.getOrder_id() + ".pdf";
                                            pathFileTemp = fullDir + "/" + tempFile + ".pdf";
                                            reader = new PdfReader(fullDir + "/" + tempFile + ".pdf");
                                            reader.selectPages(String.valueOf(j));
                                            pathFile = String.format(fullDir + "/" + orderInfo.getOrder_id() + ".pdf", j);
                                            stamper = new PdfStamper(reader, new FileOutputStream(pathFile));
                                            stamper.close();
                                            reader.close();
                                        }
                                    }
                                }
                                File fileTemp = new File(pathFileTemp);
                                File file = new File(path);
                                if (fileTemp.exists()) {
                                    fileTemp.delete();
                                }
                                if (file.exists()) {
                                    System.out.println("- File: " + path + " is exist!");
                                    orderLabelService.updateTBLNewtracking(orderInfo.getOrder_id(), trackingNumber, partnerTrackingNumber, folder_label, path);
                                    System.out.println("** UPDATED TO TBLNewtracking: [" + orderInfo.getOrder_id() + "], Folder Label: [" + folder_label + "]");
                                } else {
                                    System.out.println("- CAN'T Find Image File: " + path);
                                }
                            }
                        }
                    }
                    checkToUpdateAndSaveTBLLabel(orderInfo, orderMeasure, hasOrderId, trackingNumber, partnerTrackingNumber, responseObj, bodyRequest);
                }
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String returnCodeGenerator(List<String> trackingNumberList) {
        String trackingNumber;
        String stringCode = codeGenerator.codeGenerator();
        if (checkTrackingNumber(trackingNumberList, stringCode)) {
            trackingNumber = codeGenerator.codeGenerator();
        } else {
            trackingNumber = stringCode;
        }
        return trackingNumber;
    }

    public boolean checkTrackingNumber(List<String> trackingNumberList, String codeGenerator) {
        if (trackingNumberList.size() > 0) {
            for (int i = 0; i < trackingNumberList.size(); i++) {
                String trackingNumber = trackingNumberList.get(i);
                if (trackingNumber != null && trackingNumber.equals(codeGenerator)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkToUpdateAndSaveTBLLabel(OrderInfo orderInfo, OrderMeasure orderMeasure, boolean hasOrderId, String trackingNumber, String partnerTrackingNumber, JSONObject responseObj, BodyRequest bodyRequest) {
        Double heightInMM = orderMeasure.getTotalHeight() * 10;
        Double lengthInMM = orderMeasure.getTotalLength() * 10;
        Double widthInMM = orderMeasure.getTotalWidth() * 10;
        if (hasOrderId) {
            System.out.println("- Updating to TBL Label...");
            orderLabelService.updateInfoToTBLLabel(orderInfo, trackingNumber, partnerTrackingNumber, responseObj.toString(), gson.toJson(bodyRequest));
        } else {
            System.out.println("- Inserting to TBL Label...");
            orderLabelService.insertInfoToTBLlabel(orderInfo, trackingNumber, partnerTrackingNumber, responseObj.toString(),
                    gson.toJson(bodyRequest), orderMeasure.getTotalWeightInGram(), heightInMM,
                    lengthInMM, widthInMM, orderMeasure.getMaterial());
        }
    }

    public BodyRequest createRequestBody(OrderInfo orderInfo, OrderMeasure orderMeasure, List<OrderMaterial> orderMaterialList) {
        BodyRequest bodyRequest = new BodyRequest();
        bodyRequest.setShipmentRequest(createShipmentRequest(orderInfo, orderMeasure, orderMaterialList));
        return bodyRequest;
    }

    public ShipmentRequest createShipmentRequest(OrderInfo orderInfo, OrderMeasure orderMeasure, List<OrderMaterial> orderMaterialList) {
        ShipmentRequest shipmentRequest = new ShipmentRequest();
        shipmentRequest.setRequestedShipment(createRequestedShipment(orderInfo, orderMeasure, orderMaterialList));
        return shipmentRequest;
    }

    public InternationalDetail createInternationalDetail(OrderInfo orderInfo, List<OrderMaterial> orderMaterialList) {
        InternationalDetail internationalDetail = new InternationalDetail();
        internationalDetail.setCommodities(createCommodities(orderInfo, orderMaterialList));
        return internationalDetail;
    }

    public Commodities createCommodities(OrderInfo orderInfo, List<OrderMaterial> orderMaterialList) {
        String fillLabel = "";
        for (int i = 0; i < orderMaterialList.size(); i++) {
            if (orderMaterialList.size() == 1) {
                fillLabel = orderMaterialList.get(i).getMaterial();
            } else {
                if (!orderMaterialList.get(0).getMaterial().isEmpty() && i == 0) {
                    fillLabel = orderMaterialList.get(0).getOrder_id();
                    continue;
                }
                fillLabel = fillLabel.concat("/" + orderMaterialList.get(i).getMaterial());
            }
        }
        System.out.println("- Material: " + fillLabel);
        Commodities commodities = new Commodities();
        commodities.setDescription(fillLabel);
        commodities.setCustomsValue(orderInfo.getLine_item_quantity());
        return commodities;
    }

    public ShipmentInfo createShipmentInfo(OrderInfo orderInfo) {
        ShipmentInfo shipmentInfo = new ShipmentInfo();
        shipmentInfo.setDropOffType(DROP_OFF_TYPE);
        shipmentInfo.setServiceType(SERVICE_TYPE);
        shipmentInfo.setAccount(DHL_ACCOUNT);
        shipmentInfo.setCurrency(CURRENCY);
        shipmentInfo.setUnitOfMeasurement(UNIT_OF_MEASUREMENT);
        shipmentInfo.setPackagesCount(orderInfo.getLine_item_quantity());
        shipmentInfo.setLabelType(LABEL_TYPE);
        shipmentInfo.setLabelTemplate(LABEL_TEMPLATE);
        shipmentInfo.setArchiveLabelTemplate(ARCHIVE_LABEL_TEMPLATE);
        return shipmentInfo;
    }

    public Ship createShipInfo(OrderInfo orderInfo) {
        Ship ship = new Ship();
        ship.setShipper(createShipperInfo());
        ship.setRecipient(createRecipientInfo(orderInfo));
        return ship;
    }

    public Packages createPackages(OrderInfo orderInfo, OrderMeasure orderMeasure) {
        Packages packages = new Packages();
        packages.setRequestedPackages(createRequestedPackages(orderInfo, orderMeasure));
        return packages;
    }

    public RequestedShipment createRequestedShipment(OrderInfo orderInfo, OrderMeasure orderMeasure, List<OrderMaterial> orderMaterialList) {
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String strDate = formatter.format(tomorrow);
        RequestedShipment requestedShipment = new RequestedShipment();
        requestedShipment.setShipmentInfo(createShipmentInfo(orderInfo));
        requestedShipment.setShipTimestamp(strDate + TIME_ZONE);
        requestedShipment.setPaymentInfo(DELIVERY_AT_PLACE);
        requestedShipment.setInternationalDetail(createInternationalDetail(orderInfo, orderMaterialList));
        requestedShipment.setShip(createShipInfo(orderInfo));
        requestedShipment.setPackages(createPackages(orderInfo, orderMeasure));
        return requestedShipment;
    }

    public RequestedPackages createRequestedPackages(OrderInfo orderInfo, OrderMeasure orderMeasure) {
        // Weight use Kilogram(kg)
        RequestedPackages requestedPackages = new RequestedPackages();
        requestedPackages.setNumber(orderInfo.getLine_item_quantity());
        requestedPackages.setWeight(orderMeasure.getTotalWeightInKg());
        requestedPackages.setDimensions(createDimensionInfo(orderMeasure));
        requestedPackages.setCustomerReferences(orderInfo.getOrder_code());
        return requestedPackages;
    }

    public Dimensions createDimensionInfo(OrderMeasure orderMeasure) {
        // use Centimet(cm)
        Dimensions dimensions = new Dimensions();
        dimensions.setLength(orderMeasure.getTotalLength());
        dimensions.setWidth(orderMeasure.getTotalWidth());
        dimensions.setHeight(orderMeasure.getTotalHeight());
        return dimensions;
    }

    public Recipient createRecipientInfo(OrderInfo orderInfo) {
        Address addressRecipient = new Address();
        addressRecipient.setStreetLines(orderInfo.getShipping_address1());
        addressRecipient.setCity(orderInfo.getShipping_city());
        addressRecipient.setPostalCode(orderInfo.getShipping_zip());
        addressRecipient.setCountryCode(orderInfo.getShipping_country());
        Contact contactRecipient = new Contact();
        contactRecipient.setPersonName(orderInfo.getShipping_name());
        contactRecipient.setCompanyName(orderInfo.getShipping_name());
        contactRecipient.setPhoneNumber(orderInfo.getShipping_phone());
        Recipient recipient = new Recipient();
        recipient.setAddress(addressRecipient);
        recipient.setContact(contactRecipient);
        return recipient;
    }

    public Shipper createShipperInfo() {
        Address addressShip = new Address();
        addressShip.setStreetLines(SHIPPER_STREETLINES);
        addressShip.setCity(SHIPPER_CITY);
        addressShip.setPostalCode(SHIPPER_POSTALCODE);
        addressShip.setCountryCode(SHIPPER_COUNTRYCODE);
        Contact contactShip = new Contact();
        contactShip.setPersonName(SHIPPER_COMPANY);
        contactShip.setCompanyName(SHIPPER_NAME);
        contactShip.setPhoneNumber(SHIPPER_PHONE);
        Shipper shipper = new Shipper();
        shipper.setAddress(addressShip);
        shipper.setContact(contactShip);
        return shipper;
    }

    public boolean checkPartnerTrackingNumber(List<String> partnerTNList, String codeGenerator) {
        if (partnerTNList.size() > 0) {
            for (int i = 0; i < partnerTNList.size(); i++) {
                String partnerTrackingNumber = partnerTNList.get(i);
                if (partnerTrackingNumber != null && partnerTrackingNumber.equals(codeGenerator)) {
                    return true;
                }
            }
        }
        return false;
    }
}
