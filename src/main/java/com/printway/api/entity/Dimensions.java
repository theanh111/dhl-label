package com.printway.api.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dimensions {
    @SerializedName("Length")
    private Integer Length;

    @SerializedName("Width")
    private Integer Width;

    @SerializedName("Height")
    private Integer Height;
}
