package com.laboratorio.blueskyapiinterface.model;

import com.laboratorio.blueskyapiinterface.utils.BlueskyApiConfig;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 02/08/2024
 * @updated 04/08/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyAccount {
    private String did;
    private String handle;
    private String displayName;
    private String description;
    private String avatar;
    private String banner;
    private int followersCount;
    private int followsCount;
    private int postsCount;
    BlueskyAssociated associated;
    private String indexedAt;
    private String createdAt;
    BlueskyViewer viewer;
    BlueskyLabel[] labels;
    
    public boolean isSeguidorPotencial() {
        if (this.viewer.isBlockedBy()) {
            return false;
        }
        
        if (this.followersCount < 2) {
            return false;
        }

        if (2 * followsCount < this.followersCount) {
            return false;
        }

        return this.postsCount != 0;
    }
    
    public boolean isFuenteSeguidores() {
        BlueskyApiConfig config = BlueskyApiConfig.getInstance();
        int umbral = Integer.parseInt(config.getProperty("umbral_fuente_seguidores"));
        return this.followersCount >= umbral;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.did);
        hash = 61 * hash + Objects.hashCode(this.handle);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BlueskyAccount other = (BlueskyAccount) obj;
        if (!Objects.equals(this.did, other.did)) {
            return false;
        }
        return Objects.equals(this.handle, other.handle);
    }
}