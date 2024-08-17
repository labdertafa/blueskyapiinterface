package com.laboratorio.blueskyapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 02/08/2024
 * @updated 05/08/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyRelationship {
    private String did;
    private String following;
    private String followedBy;
    
    public boolean isFollowedBy() {
        return (this.followedBy != null);
    }
    
    public boolean isFollowing() {
        return (this.following != null);
    }
    
    public boolean canFollow() {
        return !isFollowing();
    }
}