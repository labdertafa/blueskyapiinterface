package com.laboratorio.blueskyapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/08/2024
 * @updated 10/08/2024
 */

@Getter @Setter @AllArgsConstructor
public class BlueskyImage {
    private String alt;
    private BlueskyImageBlob image;

    public BlueskyImage(BlueskyImageBlob image) {
        this.alt = "";
        this.image = image;
    }
}