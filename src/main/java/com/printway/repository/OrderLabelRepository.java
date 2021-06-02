package com.printway.repository;

import com.printway.config.DBConnection;
import com.printway.entityDB.Order;
import com.printway.entityDB.OrderInfo;
import com.printway.entityDB.OrderMeasure;
import com.printway.entityDB.OrderMaterial;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderLabelRepository {
    public static DBConnection connection = DBConnection.getInstance();


    public List<OrderMeasure> findOrderMeasure(String orderId) {
        return DBConnection.getInstance().findOrderMeasure(orderId);
    }

    public List<Order> findAllOrders() {
        return DBConnection.getInstance().findAllOrders();
    }

    public Order findOneOrderByOrderId(String orderId) {
        return DBConnection.getInstance().findOneOrderByOrderId(orderId);
    }

    public OrderInfo findOrderInfo(String orderId) {
        return DBConnection.getInstance().findOrderInfo(orderId);
    }

    public List<OrderMaterial> findOneOrderProductType(String orderId) {
        return DBConnection.getInstance().findOneOrderProductType(orderId);
    }

    public void insertTBLLabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog,
                               String bodySent, Double totalWeightInGram, Double totalHeight, Double totalLength, Double totalWidth, String material) {
        DBConnection.getInstance().insertTBLLabel(orderInfo, trackingNumber, partnerTrackingNumber, responseLog, bodySent, totalWeightInGram, totalHeight, totalLength, totalWidth, material);
    }

    public void updateTBLNewtracking(String orderId, String trackingNumber, String partnerTrackingNumber, String folderLabel, String urlLabel) {
        DBConnection.getInstance().updateTBLNewtracking(orderId, trackingNumber, partnerTrackingNumber, folderLabel, urlLabel);
    }

    public void updateErrorLog(String error, String orderId) {
        DBConnection.getInstance().updateErrorLog(error, orderId);
    }

    public void updateInfoToTBLLabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog, String bodySent) {
        DBConnection.getInstance().updateInfoToTBLLabel(orderInfo, trackingNumber, partnerTrackingNumber, responseLog, bodySent);
    }

    public boolean checkOrderIdInTBLLabel(String order_id) {
        return DBConnection.getInstance().checkOrderIdInTBLLabel(order_id);
    }

    public List<String> findAllTrackingNumberTBLNEWTRACKING() {
        return DBConnection.getInstance().findAllTrackingNumberTBLNEWTRACKING();
    }

//    public List<OrderMeasure> findOrderMeasure(String orderId) {
//        List<OrderMeasure> orderMeasureList = new ArrayList<>();
//        try {
//            String sqlQuery = "SELECT " +
//                    "NOR.order_id, " +
//                    "NOR.material, " +
//                    "(sum(NOR.line_item_quantity)) as line_item_quantity, " +
//                    "(MA.mate_weight * sum(NOR.line_item_quantity)) as totalWeightInGram, " +
//                    "((MA.mate_weight * 0.001) * sum(NOR.line_item_quantity)) as totalWeightInKg, " +
//                    "(MA.mate_height * sum(NOR.line_item_quantity)) as totalHeight, " +
//                    "MA.mate_length as totalLength,  " +
//                    "MA.mate_width as totalWidth " +
//                    "FROM orderdesk.tblneworders NOR " +
//                    "INNER JOIN orderdesk.tblmaterials MA  " +
//                    "ON NOR.material = MA.mate_name " +
//                    "WHERE NOR.order_id = ? " +
//                    "GROUP BY MA.mate_name;";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            statement.setString(1, orderId);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                OrderMeasure orderMeasure = new OrderMeasure();
//                orderMeasure.setOrder_id(resultSet.getString("order_id"));
//                orderMeasure.setMaterial(resultSet.getString("material"));
//                orderMeasure.setLine_item_quantity(resultSet.getInt("line_item_quantity"));
//                orderMeasure.setTotalWeightInGram(resultSet.getDouble("totalWeightInGram"));
//                orderMeasure.setTotalWeightInKg(resultSet.getDouble("totalWeightInKg"));
//                orderMeasure.setTotalHeight(resultSet.getDouble("totalHeight"));
//                orderMeasure.setTotalLength(resultSet.getDouble("totalLength"));
//                orderMeasure.setTotalWidth(resultSet.getDouble("totalWidth"));
//                orderMeasureList.add(orderMeasure);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return orderMeasureList;
//    }
//
//    public List<Order> findAllOrders() {
//        List<Order> orderList = new ArrayList<>();
//        try {
//            String sqlQuery = "SELECT DISTINCT NT.order_id, NOR.logistic, NT.order_code " +
//                    "FROM tblnewtracking NT " +
//                    "INNER JOIN tblneworders NOR ON NT.order_id = NOR.order_id " +
//                    "WHERE (length(NT.tracking_number) = 0 OR NT.tracking_number IS NULL) " +
//                    "AND (length(NOR.logistic) > 0 OR NOR.logistic IS NOT NULL) " +
//                    "AND NT.shipped = 0 " +
//                    "GROUP BY NT.order_id;";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Order order = new Order();
//                order.setOrder_id(resultSet.getString("order_id"));
//                if (resultSet.getString("order_code") == null || resultSet.getString("order_code").equals("")) {
//                    order.setOrder_code(resultSet.getString("order_id"));
//                } else {
//                    order.setOrder_code(resultSet.getString("order_code"));
//                }
//                order.setLogistic(resultSet.getString("logistic"));
//                orderList.add(order);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return orderList;
//    }
//
//    public Order findOneOrderByOrderId(String orderId) {
//        Order order = new Order();
//        try {
//            String sqlQuery = "SELECT DISTINCT NT.order_id, NOR.logistic, NT.order_code " +
//                    "FROM tblnewtracking NT " +
//                    "INNER JOIN tblneworders NOR ON NT.order_id = NOR.order_id " +
//                    "WHERE NT.order_id = ? " +
//                    "GROUP BY NT.order_id;";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            statement.setString(1, orderId);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                order.setOrder_id(resultSet.getString("order_id"));
//                order.setLogistic(resultSet.getString("logistic"));
//                order.setOrder_code(resultSet.getString("order_code"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return order;
//    }
//
//    public OrderInfo findOrderInfo(String orderId) {
//        OrderInfo orderInfo = new OrderInfo();
//        try {
//            String sqlQuery = "SELECT " +
//                    "NT.order_id, NT.order_code, NT.shipping_name, " +
//                    "NT.shipping_address1, NT.shipping_address2, " +
//                    "NT.shipping_city, NT.shipping_zip, " +
//                    "NT.shipping_province, NT.shipping_country, NOR.shipping_phone, " +
//                    "(sum(NOR.line_item_quantity)) as line_item_quantity " +
//                    "FROM tblnewtracking NT " +
//                    "INNER JOIN tblneworders NOR " +
//                    "ON NT.order_id = NOR.order_id " +
//                    "WHERE NT.order_id = ?;";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            statement.setString(1, orderId);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                orderInfo.setOrder_id(resultSet.getString("order_id"));
//                if (resultSet.getString("order_code") == null || resultSet.getString("order_code").equals("")) {
//                    orderInfo.setOrder_code(resultSet.getString("order_id"));
//                } else {
//                    orderInfo.setOrder_code(resultSet.getString("order_code"));
//                }
//                orderInfo.setShipping_name(resultSet.getString("shipping_name"));
//                orderInfo.setShipping_address1(resultSet.getString("shipping_address1"));
//                orderInfo.setShipping_address2(resultSet.getString("shipping_address2"));
//                orderInfo.setShipping_city(resultSet.getString("shipping_city"));
//                orderInfo.setShipping_zip(resultSet.getString("shipping_zip"));
//                orderInfo.setShipping_province(resultSet.getString("shipping_province").toUpperCase());
//                orderInfo.setShipping_country(resultSet.getString("shipping_country").toUpperCase());
//                orderInfo.setShipping_phone(resultSet.getString("shipping_phone"));
//                orderInfo.setLine_item_quantity(resultSet.getInt("line_item_quantity"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return orderInfo;
//    }
//
//    public List<OrderMaterial> findOneOrderProductType(String orderId) {
//        List<OrderMaterial> orderMaterialList = new ArrayList<>();
//        try {
//            String sqlQuery = "SELECT order_id, material FROM tblneworders WHERE order_id = ?;";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            statement.setString(1, orderId);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                OrderMaterial orderItemQuantity = new OrderMaterial();
//                orderItemQuantity.setOrder_id(resultSet.getString("order_id"));
//                orderItemQuantity.setMaterial(resultSet.getString("material"));
//                orderMaterialList.add(orderItemQuantity);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return orderMaterialList;
//    }
//
//    public void insertTBLLabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog,
//                               String bodySent, Double totalWeightInGram, Double totalHeight, Double totalLength, Double totalWidth, String material) {
//        try {
//            Date dateCurrent = new Date(System.currentTimeMillis());
//            String sqlQuery = "INSERT INTO tbllabel " +
//                    "(order_id, weight, height, length, width, material, quantity, tracking_number, partner_tracking_number, response_log, body_sent, dtime_entered) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            statement.setString(1, orderInfo.getOrder_id());
//            statement.setFloat(2, Float.parseFloat(totalWeightInGram.toString()));
//            statement.setFloat(3, Float.parseFloat(totalHeight.toString()));
//            statement.setFloat(4, Float.parseFloat(totalLength.toString()));
//            statement.setFloat(5, Float.parseFloat(totalWidth.toString()));
//            statement.setString(6, material);
//            statement.setInt(7, orderInfo.getLine_item_quantity());
//            statement.setString(8, trackingNumber);
//            statement.setString(9, partnerTrackingNumber);
//            statement.setString(10, responseLog);
//            statement.setString(11, bodySent);
//            statement.setTimestamp(12, new Timestamp(dateCurrent.getTime()));
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateTBLNewtracking(String orderId, String trackingNumber, String partnerTrackingNumber, String folderLabel, String urlLabel) {
//        try {
//            String sqlQuery = "UPDATE tblnewtracking SET tracking_number = ?, partner_tracking_number = ?, folder_label = ?, url_label = ?, multran_log = ? WHERE order_id = ?";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            statement.setString(1, trackingNumber);
//            statement.setString(2, partnerTrackingNumber);
//            statement.setString(3, folderLabel);
//            statement.setString(4, urlLabel);
//            statement.setString(5, null);
//            statement.setString(6, orderId);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateErrorLog(String error, String orderId) {
//        try {
//            String sqlQuery = "UPDATE tblnewtracking SET multran_log = ? WHERE order_id = ?;";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            statement.setString(1, error);
//            statement.setString(2, orderId);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateInfoToTBLLabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog, String bodySent) {
//        try {
//            String sqlQuery = "UPDATE tbllabel SET tracking_number = ?, partner_tracking_number = ?, response_log = ?, body_sent = ? WHERE order_id = ?;";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            statement.setString(1, trackingNumber);
//            statement.setString(2, partnerTrackingNumber);
//            statement.setString(3, responseLog);
//            statement.setString(4, bodySent);
//            statement.setString(5, orderInfo.getOrder_id());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public boolean checkOrderIdInTBLLabel(String order_id) {
//        try {
//            String orderId = null;
//            String sqlQuery = "SELECT order_id FROM tbllabel WHERE order_id = ?;";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            statement.setString(1, order_id);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                orderId = resultSet.getString("order_id");
//            }
//            if (orderId != null) {
//                return true;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public List<String> findAllTrackingNumberTBLNEWTRACKING() {
//        List<String> trackingNumberList = new ArrayList<>();
//        try {
//            String sqlQuery = "SELECT tracking_number FROM tblnewtracking;";
//            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                String trackingNumber = resultSet.getString("tracking_number");
//                trackingNumberList.add(trackingNumber);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return trackingNumberList;
//    }
}
