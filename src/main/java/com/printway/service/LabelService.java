package com.printway.service;

import com.printway.api.DHLApi;
import com.printway.config.DBConnection;
import com.printway.entityDB.Order;
import com.printway.entityDB.OrderInfo;
import com.printway.entityDB.OrderMeasure;
import com.printway.entityDB.OrderMaterial;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LabelService {
    private final String PATH = "D:/PrintwayProject/dhl-label/var/www/html/pod/label/";
//    private final String PATH = "/var/www/html/pod/label/";

    private OrderLabelService orderLabelService = new OrderLabelService();

    private DHLApi dhlApi = new DHLApi();

    public void getLabel() {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            Date dateCurrent = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
            String strDate = formatter.format(dateCurrent);
            String strDate2 = formatter2.format(dateCurrent);
            String firstDir = PATH.concat(strDate);
            String fullDir = PATH.concat(strDate + "/" + strDate2);
            String folder_label = strDate + "/" + strDate2;
            File directory = new File(firstDir);
            File directory2 = new File(fullDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            if (!directory2.exists()) {
                directory2.mkdir();
            }
            List<Order> orderList = orderLabelService.findAllOrders();
            System.out.println("*** Total: " + orderList.size());
            for (int i = 0; i < orderList.size(); i++) {
                try {
                    if (orderList.get(i).getLogistic().equals("DHL")) {
                        String orderId = orderList.get(i).getOrder_id();
                        if (orderId != null && !orderId.equals("")) {
                            OrderInfo orderInfo = orderLabelService.findOneOrderInfo(orderId);
                            if (orderInfo.getOrder_id() != null && !orderInfo.getOrder_id().equals("")) {
                                System.out.println("- Order Info: " + orderInfo);
                                List<OrderMaterial> listProductType = orderLabelService.findOneOrderProductType(orderInfo.getOrder_id());
                                List<OrderMeasure> orderMeasureList = orderLabelService.findOrderMeasure(orderId);
                                OrderMeasure orderMeasure = createOrderMeasure(orderId, orderMeasureList);
                                dhlApi.sendRequest(orderInfo, listProductType, fullDir, folder_label, orderMeasure);
                            }
                        }
                        System.out.println();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLabelByOrderId(String orderIdToFind) {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            Date dateCurrent = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
            String strDate = formatter.format(dateCurrent);
            String strDate2 = formatter2.format(dateCurrent);
            String firstDir = PATH.concat(strDate);
            String fullDir = PATH.concat(strDate + "/" + strDate2);
            String folder_label = strDate + "/" + strDate2;
            File directory = new File(firstDir);
            File directory2 = new File(fullDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            if (!directory2.exists()) {
                directory2.mkdir();
            }

            Order order = orderLabelService.findOneOrderByOrderId(orderIdToFind);
            if (order.getOrder_id() == null || order.getOrder_id().equals("")) {
                System.out.println("- CAN'T FIND THIS ORDER [" + orderIdToFind + "]! Please try again!");
            } else {
                List<Order> orderList = new ArrayList<>();
                orderList.add(order);
                System.out.println("*** Total: " + orderList.size());
                for (int i = 0; i < orderList.size(); i++) {
                    try {
                        if (orderList.get(i).getLogistic().equals("DHL")) {
                            String orderId = orderList.get(i).getOrder_id();
                            if (orderId != null && !orderId.equals("")) {
                                OrderInfo orderInfo = orderLabelService.findOneOrderInfo(orderId);
                                if (orderInfo.getOrder_id() != null && !orderInfo.getOrder_id().equals("")) {
                                    System.out.println("- Order Info: " + orderInfo);
                                    List<OrderMaterial> listProductType = orderLabelService.findOneOrderProductType(orderInfo.getOrder_id());
                                    List<OrderMeasure> orderMeasureList = orderLabelService.findOrderMeasure(orderId);
                                    OrderMeasure orderMeasure = createOrderMeasure(orderId, orderMeasureList);
                                    dhlApi.sendRequest(orderInfo, listProductType, fullDir, folder_label, orderMeasure);
                                }
                            }
                            System.out.println();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OrderMeasure createOrderMeasure(String orderId, List<OrderMeasure> orderMeasureList) {
        OrderMeasure orderMeasure = new OrderMeasure();
        try {
            Double totalWeightInGram = new Double(0);
            Double totalWeightInKg = new Double(0);
            Double totalHeight = new Double(0);

            List<Double> length = new ArrayList<>();
            List<Double> width = new ArrayList<>();
            List<String> material = new ArrayList<>();

            for (int i = 0; i < orderMeasureList.size(); i++) {
                totalWeightInGram += orderMeasureList.get(i).getTotalWeightInGram();
                totalWeightInKg += orderMeasureList.get(i).getTotalWeightInKg();
                totalHeight += orderMeasureList.get(i).getTotalHeight();
                length.add(orderMeasureList.get(i).getTotalLength());
                width.add(orderMeasureList.get(i).getTotalWidth());
                material.add(orderMeasureList.get(i).getMaterial());
            }
            Double maxLength = Collections.max(length);
            Double maxWidth = Collections.max(width);

            System.out.println("- totalWeightInGram: " + totalWeightInGram);
            System.out.println("- totalWeightInKg: " + totalWeightInKg);
            System.out.println("- totalHeight (cm): " + totalHeight);
            System.out.println("- Max Length (cm): " + maxLength);
            System.out.println("- Max Width (cm): " + maxWidth);
            System.out.println("- Material(s): " + material);

            orderMeasure.setOrder_id(orderId);
            orderMeasure.setTotalWeightInGram(totalWeightInGram);
            orderMeasure.setTotalWeightInKg(totalWeightInKg);
            orderMeasure.setTotalHeight(totalHeight);
            orderMeasure.setTotalLength(maxLength);
            orderMeasure.setTotalWidth(maxWidth);
            orderMeasure.setMaterial(material.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderMeasure;
    }

}
