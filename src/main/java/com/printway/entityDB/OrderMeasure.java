package com.printway.entityDB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMeasure {
    private String order_id;
    private String material;
    private Integer line_item_quantity;
    private Double totalWeightInGram;
    private Double totalWeightInKg;
    private Double totalHeight;
    private Double totalLength;
    private Double totalWidth;
}
