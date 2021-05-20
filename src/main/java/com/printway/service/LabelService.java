package com.printway.service;

import com.printway.api.DHLApi;
import com.printway.entityDB.Order;
import com.printway.entityDB.OrderInfo;
import com.printway.entityDB.OrderProductType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LabelService {
    private final String PATH = "D:/PrintwayProject/dhl-label/var/www/html/pod/label/";
//    private final String PATH = "/var/www/html/pod/label/";

    private OrderLabelService orderLabelService = new OrderLabelService();

    private DHLApi dhlApi = new DHLApi();

    public void getLabel() {
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
                            List<OrderProductType> listProductType = orderLabelService.findOneOrderProductType(orderInfo.getOrder_id());
                            System.out.println(orderInfo);
                            dhlApi.sendRequest(orderInfo, listProductType, fullDir, folder_label);
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

    public void getLabelByOrderId(String orderIdToFind) {
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
                            List<OrderProductType> listProductType = orderLabelService.findOneOrderProductType(orderInfo.getOrder_id());
                            System.out.println(orderInfo);
                            dhlApi.sendRequest(orderInfo, listProductType, fullDir, folder_label);
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
}
