package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @SerializedName("StreetLines")
    private String StreetLines;

    @SerializedName("City")
    private String City;

    @SerializedName("PostalCode")
    private String PostalCode;

    @SerializedName("CountryCode")
    private String CountryCode;
}
