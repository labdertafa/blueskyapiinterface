package com.laboratorio.blueskyapiinterface.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/08/2024
 * @updated 10/08/2024
 */
public class BlueskyEmbed {
    private String $type;
    private List<BlueskyImage> images;

    public BlueskyEmbed(BlueskyImageBlob image) {
        this.$type = "app.bsky.embed.images";
        this.images = new ArrayList<>();
        this.images.add(new BlueskyImage(image));
    }

    public String get$type() {
        return $type;
    }

    public void set$type(String $type) {
        this.$type = $type;
    }

    public List<BlueskyImage> getImages() {
        return images;
    }

    public void setImages(List<BlueskyImage> images) {
        this.images = images;
    }
}