package com.laboratorio.blueskyapiinterface.model;

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
public class BlueskyActor {
    private String did;
    private String handle;
    private String displayName;
    private String description;
    private String avatar;
    private BlueskyAssociated associated;
    private String indexedAt;
    private String createdAt;
    private BlueskyLabel[] labels;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.did);
        hash = 83 * hash + Objects.hashCode(this.handle);
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
        final BlueskyActor other = (BlueskyActor) obj;
        if (!Objects.equals(this.did, other.did)) {
            return false;
        }
        return Objects.equals(this.handle, other.handle);
    }
}