package com.printway.repository;

import com.printway.config.DBConnection;
import com.printway.entityDB.Order;
import com.printway.entityDB.OrderInfo;
import com.printway.entityDB.OrderProductType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderLabelRepository {
    public static DBConnection connection = DBConnection.getInstance();

    public OrderLabelRepository() {
    }

    public List<Order> findAllOrders() {
        List<Order> orderList = new ArrayList<>();
        try {
            String sqlQuery = "SELECT DISTINCT NT.order_id, NOR.logistic, NT.order_code " +
                    "FROM tblnewtracking NT " +
                    "INNER JOIN tblneworders NOR ON NT.order_id = NOR.order_id " +
                    "WHERE (LENGTH(NT.partner_tracking_number) = 0 OR NT.partner_tracking_number IS NULL) " +
                    "AND LENGTH(NOR.logistic) > 0 " +
                    "AND NT.shipped = 0;";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrder_id(resultSet.getString("order_id"));
                if (resultSet.getString("order_code") == null || resultSet.getString("order_code").equals("")) {
                    order.setOrder_code(resultSet.getString("order_id"));
                } else {
                    order.setOrder_code(resultSet.getString("order_code"));
                }
                order.setLogistic(resultSet.getString("logistic"));
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public Order findOneOrderByOrderId(String orderId) {
        Order order = new Order();
        try {
            String sqlQuery = "SELECT DISTINCT NT.order_id, NOR.logistic, NT.order_code " +
                    "FROM tblnewtracking NT " +
                    "INNER JOIN tblneworders NOR ON NT.order_id = NOR.order_id " +
                    "WHERE NT.order_id = ?;";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                order.setOrder_id(resultSet.getString("order_id"));
                order.setLogistic(resultSet.getString("logistic"));
                order.setOrder_code(resultSet.getString("order_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public OrderInfo fineOneOrderInfo(String orderId) {
        OrderInfo orderInfo = new OrderInfo();
        try {
            String sqlQuery = "SELECT " +
                    "NT.order_id, NT.order_code, NT.shipping_name, " +
                    "NT.shipping_address1, NT.shipping_address2, " +
                    "NT.shipping_city, NT.shipping_zip, " +
                    "NT.shipping_province, NT.shipping_country, NT.shipping_phone, " +
                    "MA.mate_name, " +
                    "(sum(NOR.line_item_quantity)) as line_item_quantity, " +
                    "(MA.mate_weight * sum(NOR.line_item_quantity)) as totalWeightInGram, " +
                    "((MA.mate_weight * 0.001) * sum(NOR.line_item_quantity)) as totalWeightInKilogram, " +
                    "(MA.mate_height * sum(NOR.line_item_quantity)) as totalHeight, " +
                    "(MA.mate_length * sum(NOR.line_item_quantity)) as totalLength, " +
                    "(MA.mate_width * sum(NOR.line_item_quantity)) as totalWidth " +
                    "FROM tblnewtracking NT " +
                    "INNER JOIN tblmaterials MA " +
                    "ON NT.material = MA.mate_name " +
                    "INNER JOIN tblneworders NOR " +
                    "ON NT.order_id = NOR.order_id " +
                    "WHERE NT.order_id = ?;";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orderInfo.setOrder_id(resultSet.getString("order_id"));
                orderInfo.setOrder_code(resultSet.getString("order_code"));
                orderInfo.setShipping_name(resultSet.getString("shipping_name"));
                orderInfo.setShipping_address1(resultSet.getString("shipping_address1"));
                orderInfo.setShipping_address2(resultSet.getString("shipping_address2"));
                orderInfo.setShipping_city(resultSet.getString("shipping_city"));
                orderInfo.setShipping_zip(resultSet.getString("shipping_zip"));
                orderInfo.setShipping_province(resultSet.getString("shipping_province"));
                orderInfo.setShipping_country(resultSet.getString("shipping_country"));
                orderInfo.setShipping_phone(resultSet.getString("shipping_phone"));
                orderInfo.setMaterial(resultSet.getString("mate_name"));
                orderInfo.setLine_item_quantity(resultSet.getInt("line_item_quantity"));
                orderInfo.setTotalWeightInKilogram(resultSet.getDouble("totalWeightInKilogram"));
                orderInfo.setTotalHeight(resultSet.getInt("totalHeight"));
                orderInfo.setTotalLength(resultSet.getInt("totalLength"));
                orderInfo.setTotalWidth(resultSet.getInt("totalWidth"));
                orderInfo.setTotalWeightInGram(resultSet.getDouble("totalWeightInGram"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderInfo;
    }

    public List<OrderProductType> findOneOrderProductType(String orderId) {
        List<OrderProductType> orderProductTypeList = new ArrayList<>();
        try {
            String sqlQuery = "SELECT order_id, product_type FROM tblneworders WHERE order_id = ?;";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OrderProductType orderItemQuantity = new OrderProductType();
                orderItemQuantity.setOrder_id(resultSet.getString("order_id"));
                orderItemQuantity.setProduct_type(resultSet.getString("product_type"));
                orderProductTypeList.add(orderItemQuantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderProductTypeList;
    }

    public void updateTBLNewtracking(String orderId, String trackingNumber, String partnerTrackingNumber, String urlLabel, String folderLabel) {
        try {
            String sqlQuery = "UPDATE tblnewtracking SET tracking_number = ?, partner_tracking_number = ?, url_label = ?, folder_label = ? WHERE order_id = ?;";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, trackingNumber);
            statement.setString(2, partnerTrackingNumber);
            statement.setString(3, urlLabel);
            statement.setString(4, folderLabel);
            statement.setString(5, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateErrorLog(String error, String orderId) {
        try {
            String sqlQuery = "UPDATE tblnewtracking SET multran_log = ? WHERE order_id = ?;";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, error);
            statement.setString(2, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertInfoToTBLLabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog, String bodySent) {
        try {
            Date dateCurrent = new Date(System.currentTimeMillis());
            String sqlQuery = "INSERT INTO tbllabel " +
                    "(order_id, weight, height, length, width, material, quantity, tracking_number, partner_tracking_number, response_log, body_sent, dtime_entered) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, orderInfo.getOrder_id());
            statement.setFloat(2, Float.parseFloat(orderInfo.getTotalWeightInGram().toString()));
            statement.setFloat(3, orderInfo.getTotalHeight());
            statement.setFloat(4, orderInfo.getTotalLength());
            statement.setFloat(5, orderInfo.getTotalWidth());
            statement.setString(6, orderInfo.getMaterial());
            statement.setInt(7, orderInfo.getLine_item_quantity());
            statement.setString(8, trackingNumber);
            statement.setString(9, partnerTrackingNumber);
            statement.setString(10, responseLog);
            statement.setString(11, bodySent);
            statement.setTimestamp(12, new Timestamp(dateCurrent.getTime()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateInfoToTBLLabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog, String bodySent) {
        try {
            String sqlQuery = "UPDATE tbllabel SET weight = ?, height = ?, length = ?, width = ?, material = ?, quantity = ?, tracking_number = ?, partner_tracking_number = ?, response_log = ?, body_sent = ? WHERE order_id = ?;";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            statement.setFloat(1, Float.parseFloat(orderInfo.getTotalWeightInGram().toString()));
            statement.setFloat(2, orderInfo.getTotalHeight() * 10);
            statement.setFloat(3, orderInfo.getTotalLength() * 10);
            statement.setFloat(4, orderInfo.getTotalWidth() * 10);
            statement.setString(5, orderInfo.getMaterial());
            statement.setInt(6, orderInfo.getLine_item_quantity());
            statement.setString(7, trackingNumber);
            statement.setString(8, partnerTrackingNumber);
            statement.setString(9, responseLog);
            statement.setString(10, bodySent);
            statement.setString(11, orderInfo.getOrder_id());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean checkOrderIdInTBLLabel(String order_id) {
        try {
            String orderId = null;
            String sqlQuery = "SELECT order_id FROM tbllabel WHERE order_id = ?;";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, order_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orderId = resultSet.getString("order_id");
            }
            if (orderId != null) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> findAllTrackingNumberTBLNEWTRACKING() {
        List<String> trackingNumberList = new ArrayList<>();
        try {
            String sqlQuery = "SELECT tracking_number FROM tblnewtracking;";
            PreparedStatement statement = connection.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String trackingNumber = resultSet.getString("tracking_number");
                trackingNumberList.add(trackingNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trackingNumberList;
    }
}
