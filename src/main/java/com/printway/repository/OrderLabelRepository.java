package com.printway.repository;

import com.printway.config.Database;
import com.printway.entityDB.Order;
import com.printway.entityDB.OrderInfo;
import com.printway.entityDB.OrderProductType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderLabelRepository {
    private final String url = getDatabase().getUrl();
    private final String username = getDatabase().getUsername();
    private final String password = getDatabase().getPassword();

    public OrderLabelRepository() {
    }

    public Database getDatabase() {
        Database database = new Database();
        database.getUrl();
        database.getUsername();
        database.getPassword();
        return database;
    }

    public List<Order> findAllOrders() {
        List<Order> orderList = new ArrayList<>();
        try {

            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlModeQuery = "SET SESSION sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';";
            String sqlQuery = "SELECT NT.order_id, NOR.logistic, NT.order_code " +
                    "FROM tblnewtracking NT " +
                    "INNER JOIN tblneworders NOR ON NT.order_id = NOR.order_id " +
                    "WHERE (length(NT.tracking_number) = 0 OR NT.tracking_number IS NULL) " +
                    "AND (length(NOR.logistic) > 0 OR NOR.logistic IS NOT NULL) " +
                    "AND NT.shipped = 0;";
            PreparedStatement sqlMode = conn.prepareStatement(sqlModeQuery);
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            sqlMode.execute();
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
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return orderList;
    }

    public OrderInfo fineOneOrderInfo(String orderId) {
        OrderInfo orderInfo = new OrderInfo();
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlModeQuery = "SET SESSION sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';";
            String sqlQuery = "SELECT " +
                    "NT.order_id, NT.order_code, NT.shipping_name, " +
                    "NT.shipping_address1, NT.shipping_address2, " +
                    "NT.shipping_city, NT.shipping_zip, " +
                    "NT.shipping_province, NT.shipping_country, NT.shipping_phone, " +
                    "MA.mate_name, " +
                    "(sum(NOR.line_item_quantity)) as line_item_quantity, " +
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
            PreparedStatement sqlMode = conn.prepareStatement(sqlModeQuery);
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            sqlMode.execute();
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
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return orderInfo;
    }

    public List<OrderProductType> findOneOrderProductType(String orderId) {
        List<OrderProductType> orderProductTypeList = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlModeQuery = "SET SESSION sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';";
            String sqlQuery = "select order_id, product_type from tblneworders where order_id = ?;";
            PreparedStatement sqlMode = conn.prepareStatement(sqlModeQuery);
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            sqlMode.execute();
            statement.setString(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OrderProductType orderItemQuantity = new OrderProductType();
                orderItemQuantity.setOrder_id(resultSet.getString("order_id"));
                orderItemQuantity.setProduct_type(resultSet.getString("product_type"));
                orderProductTypeList.add(orderItemQuantity);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return orderProductTypeList;
    }

    public void updateTBLNewtracking(String orderId, String trackingNumber, String partnerTrackingNumber, String urlLabel, String folderLabel) {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlQuery = "UPDATE tblnewtracking SET tracking_number = ?, partner_tracking_number = ?, url_label = ?, folder_label = ? WHERE order_id = ?;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, trackingNumber);
            statement.setString(2, partnerTrackingNumber);
            statement.setString(3, urlLabel);
            statement.setString(4, folderLabel);
            statement.setString(5, orderId);
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void updateErrorLog(String error, String orderId) {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlQuery = "UPDATE tblnewtracking SET multran_log = ? WHERE order_id = ?;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, error);
            statement.setString(2, orderId);
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void insertInfoToTBLLabel(OrderInfo orderInfo, String trackingNumber, String partnerTrackingNumber, String responseLog, String bodySent) {
        try {
            Date dateCurrent = new Date(System.currentTimeMillis());
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlQuery = "INSERT INTO tbllabel " +
                    "(order_id, weight, height, length, width, material, quantity, tracking_number, partner_tracking_number, response_log, body_sent, dtime_entered) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, orderInfo.getOrder_id());
            statement.setFloat(2, Float.parseFloat(orderInfo.getTotalWeightInKilogram().toString()));
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
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void updateInfoToTBLLabel(OrderInfo orderInfo, String responseLog, String bodySent) {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlQuery = "UPDATE tbllabel SET response_log = ?, body_sent = ? WHERE order_id = ?;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, responseLog);
            statement.setString(2, bodySent);
            statement.setString(3, orderInfo.getOrder_id());
            statement.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public boolean checkOrderIdInTBLLabel(String order_id) {
        try {
            String orderId = null;
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlQuery = "SELECT order_id FROM tbllabel WHERE order_id = ?;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, order_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orderId = resultSet.getString("order_id");
            }
            conn.close();
            if (orderId != null) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return false;
    }

    public List<String> findAllPartnerTrackingNumber() {
        List<String> partnerTNList = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlQuery = "SELECT partner_tracking_number FROM tblnewtracking;";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String partnerTrackingNumber = resultSet.getString("partner_tracking_number");
                partnerTNList.add(partnerTrackingNumber);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return partnerTNList;
    }
}
