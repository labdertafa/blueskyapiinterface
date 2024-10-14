package com.laboratorio.blueskyapiinterface.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 14/10/2024
 * @updated 14/10/2024
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyReason {
    @SerializedName("$type")
    private String type;
    private BlueskyAccount by;
    private String indexedAt;
}