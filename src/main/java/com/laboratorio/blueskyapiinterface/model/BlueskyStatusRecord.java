package com.laboratorio.blueskyapiinterface.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 03/08/2024
 * @updated 14/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyStatusRecord {
    @SerializedName("$type")
    private String type;
    private String createdAt;
    private String[] langs;
    private String text;
}