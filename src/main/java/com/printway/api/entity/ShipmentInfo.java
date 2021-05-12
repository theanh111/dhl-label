package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentInfo {
    @SerializedName("DropOffType")
    private String DropOffType;

    @SerializedName("ServiceType")
    private String ServiceType;

    @SerializedName("Account")
    private String Account;

    @SerializedName("Currency")
    private String Currency;

    @SerializedName("UnitOfMeasurement")
    private String UnitOfMeasurement;

    @SerializedName("PackagesCount")
    private Integer PackagesCount;

    @SerializedName("LabelType")
    private String LabelType;

    @SerializedName("LabelTemplate")
    private String LabelTemplate;

    @SerializedName("ArchiveLabelTemplate")
    private String ArchiveLabelTemplate;
}
