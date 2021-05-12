package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commodities {
    @SerializedName("Description")
    private String Description;

    @SerializedName("CustomsValue")
    private Integer CustomsValue;
}
