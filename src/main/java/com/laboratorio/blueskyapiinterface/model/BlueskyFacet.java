package com.laboratorio.blueskyapiinterface.model;

import com.laboratorio.blueskyapiinterface.utils.ElementoPost;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/08/2024
 * @updated 07/08/2024
 */

@Getter @Setter @AllArgsConstructor
public class BlueskyFacet {
    private BlueskyIndex index;
    private List<BlueskyFeature> features;
    
    public BlueskyFacet(ElementoPost elemento) {
        this.index = new BlueskyIndex(elemento.getByteStart(), elemento.getByteEnd());
        BlueskyFeature feature = null;
        switch (elemento.getType()) {
            case Link -> feature = new BlueskyFeature("app.bsky.richtext.facet#link", elemento.getContenido(), null);
            case Tag -> feature = new BlueskyFeature("app.bsky.richtext.facet#tag", null, elemento.getContenido());
        }
        this.features = null;
        if (feature != null) {
            this.features = new ArrayList<>();
            this.features.add(feature);
        }
    }
}