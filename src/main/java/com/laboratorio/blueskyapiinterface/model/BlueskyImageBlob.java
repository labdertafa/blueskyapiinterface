package com.laboratorio.blueskyapiinterface.model;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/08/2024
 * @updated 10/08/2024
 */
public class BlueskyImageBlob {
    private String $type;
    private BlueskyReference ref;
    private String mimeType;
    private int size;

    public BlueskyImageBlob(String $type, BlueskyReference ref, String mimeType, int size) {
        this.$type = $type;
        this.ref = ref;
        this.mimeType = mimeType;
        this.size = size;
    }

    public String get$type() {
        return $type;
    }

    public void set$type(String $type) {
        this.$type = $type;
    }

    public BlueskyReference getRef() {
        return ref;
    }

    public void setRef(BlueskyReference ref) {
        this.ref = ref;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}