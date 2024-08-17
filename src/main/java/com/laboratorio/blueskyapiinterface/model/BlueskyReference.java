package com.laboratorio.blueskyapiinterface.model;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/08/2024
 * @updated 10/08/2024
 */
public class BlueskyReference {
    private String $link;

    public BlueskyReference(String $link) {
        this.$link = $link;
    }

    public String get$link() {
        return $link;
    }

    public void set$link(String $link) {
        this.$link = $link;
    }
}