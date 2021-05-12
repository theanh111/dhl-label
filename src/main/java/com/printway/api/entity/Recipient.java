package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipient {
    @SerializedName("Contact")
    private Contact Contact;

    @SerializedName("Address")
    private Address Address;
}
