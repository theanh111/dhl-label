package com.printway.service;

import com.printway.entityDB.Order;
import com.printway.entityDB.OrderInfo;
import com.printway.entityDB.OrderMeasure;
import com.printway.entityDB.OrderMaterial;
import com.printway.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderLabelService {
    private OrderLabelRepository orderLabelRepository = new OrderLabelRepository();

    public List<Order> findAllOrders() {
        return orderLabelRepository.findAllOrders();
    }

    public Order findOneOrderByOrderId(String orderId) {
        return orderLabelRepository.findOneOrderByOrderId(orderId);
    }

    public OrderInfo findOneOrderInfo(String orderId) {
        return orderLabelRepository.findOrderInfo(orderId);
    }

    public List<OrderMeasure> findOrderMeasure(String orderId) {
        return orderLabelRepository.findOrderMeasure(orderId);
    }

    public void updateTBLNewtracking(String orderId, String trackingNumber, String partnerTrackingNumber, String folderLabel, String urlLabel) {
        orderLabelRepository.updateTBLNewtracking(orderId, trackingNumber, partnerTrackingNumber, folderLabel, urlLabel);
    }


    public void updateLog(String error, String orderId) {
        orderLabelRepository.updateErrorLog(error, orderId);
    }

    public void insertInfoToTBLlabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog,
                                     String bodySent, Double totalWeightInGram, Double totalHeight, Double totalLength, Double totalWidth, String material) {
        orderLabelRepository.insertTBLLabel(orderInfo, trackingNumber, partnerTrackingNumber, responseLog, bodySent,
                                        totalWeightInGram, totalHeight, totalLength, totalWidth, material);
    }

    public void updateInfoToTBLLabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog, String bodySent) {
        orderLabelRepository.updateInfoToTBLLabel(orderInfo, trackingNumber, partnerTrackingNumber, responseLog, bodySent);
    }


    public boolean checkOrderIdInTBLLabel(String order_id) {
        return orderLabelRepository.checkOrderIdInTBLLabel(order_id);
    }

    public List<OrderMaterial> findOneOrderProductType(String orderId) {
        return orderLabelRepository.findOneOrderProductType(orderId);
    }

    public List<String> findAllTrackingNumberTBLNEWTRACKING() {
        return orderLabelRepository.findAllTrackingNumberTBLNEWTRACKING();
    }
}
