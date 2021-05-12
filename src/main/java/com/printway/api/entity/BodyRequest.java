package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyRequest {
    @SerializedName("ShipmentRequest")
    private ShipmentRequest ShipmentRequest;
}
