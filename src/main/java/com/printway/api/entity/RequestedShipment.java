package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestedShipment {
    @SerializedName("ShipmentInfo")
    private ShipmentInfo ShipmentInfo;

    @SerializedName("ShipTimestamp")
    private String ShipTimestamp;

    @SerializedName("PaymentInfo")
    private String PaymentInfo;

    @SerializedName("InternationalDetail")
    private InternationalDetail InternationalDetail;

    @SerializedName("Ship")
    private Ship Ship;

    @SerializedName("Packages")
    private Packages Packages;
}
