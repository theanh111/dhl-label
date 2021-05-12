package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    @SerializedName("PersonName")
    private String PersonName;

    @SerializedName("CompanyName")
    private String CompanyName;

    @SerializedName("PhoneNumber")
    private String PhoneNumber;
}
