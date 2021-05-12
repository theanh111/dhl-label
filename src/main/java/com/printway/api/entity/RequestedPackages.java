package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestedPackages {
    @SerializedName("@number")
    private Integer number;

    @SerializedName("Weight")
    private Double Weight;

    @SerializedName("Dimensions")
    private Dimensions Dimensions;

    @SerializedName("CustomerReferences")
    private String CustomerReferences;
}
