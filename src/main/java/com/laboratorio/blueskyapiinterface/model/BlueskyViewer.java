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
 * @updated 02/08/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyViewer {
    private boolean muted;
    private BlueskyRequestingAccountsList mutedByList;
    private boolean blockedBy;
    private String blocking;
    private BlueskyRequestingAccountsList blockingByList;
    private String following;
    private String followedBy;
    private BlueskyKnownFollowers knownFollowers;
}