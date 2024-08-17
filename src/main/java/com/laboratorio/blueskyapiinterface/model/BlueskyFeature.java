package com.laboratorio.blueskyapiinterface.model;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/08/2024
 * @updated 07/08/2024
 */
public class BlueskyFeature {
    private String $type;
    private String uri;
    private String tag;

    public BlueskyFeature(String $type, String uri, String tag) {
        this.$type = $type;
        this.uri = uri;
        this.tag = tag;
    }

    public String get$type() {
        return $type;
    }

    public void set$type(String $type) {
        this.$type = $type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}