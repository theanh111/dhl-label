package com.printway.service;

import com.printway.entityDB.Order;
import com.printway.entityDB.OrderInfo;
import com.printway.entityDB.OrderProductType;
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
        return orderLabelRepository.fineOneOrderInfo(orderId);
    }

    public void update(String orderId, String trackingNumber, String partnerTrackingNumber, String urlLabel, String folderLabel) {
        orderLabelRepository.updateTBLNewtracking(orderId, trackingNumber, partnerTrackingNumber, urlLabel, folderLabel);
    }

    public void updateLog(String error, String orderId) {
        orderLabelRepository.updateErrorLog(error, orderId);
    }

    public void insertInfoToTBLlabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog, String bodySent) {
        orderLabelRepository.insertInfoToTBLLabel(orderInfo, trackingNumber, partnerTrackingNumber, responseLog, bodySent);
    }

    public void updateInfoToTBLLabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog, String bodySent) {
        orderLabelRepository.updateInfoToTBLLabel(orderInfo, trackingNumber, partnerTrackingNumber, responseLog, bodySent);
    }


    public boolean checkOrderIdInTBLLabel(String order_id) {
        return orderLabelRepository.checkOrderIdInTBLLabel(order_id);
    }

    public List<OrderProductType> findOneOrderProductType(String orderId) {
        return orderLabelRepository.findOneOrderProductType(orderId);
    }

    public List<String> findAllTrackingNumberTBLNEWTRACKING() {
        return orderLabelRepository.findAllTrackingNumberTBLNEWTRACKING();
    }
}
