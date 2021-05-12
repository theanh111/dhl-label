package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ship {
    @SerializedName("Shipper")
    private Shipper Shipper;

    @SerializedName("Recipient")
    private Recipient Recipient;
}
