package com.printway.entityDB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductType {
    private String order_id;
    private String product_type;
    private Integer line_item_quantity;
}
