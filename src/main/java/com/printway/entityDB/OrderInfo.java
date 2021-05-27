package com.printway.entityDB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    private String order_id;
    private String order_code;
    private String shipping_name;
    private String shipping_address1;
    private String shipping_address2;
    private String shipping_city;
    private String shipping_zip;
    private String shipping_province;
    private String shipping_country;
    private String shipping_phone;
    private Integer line_item_quantity;
}
