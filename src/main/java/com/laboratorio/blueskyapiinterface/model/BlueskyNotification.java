package com.laboratorio.blueskyapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 04/08/2024
 * @updated 04/08/2024
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyNotification {
    private String uri;
    private String cid;
    private BlueskyActor author;
    // Possible values: [like, repost, follow, mention, reply, quote, starterpack-joined]
    private String reason; 
    private String reasonSubject;
    // private BlueskyRecord<> record;
    private boolean isRead;
    private String indexedAt;
    private BlueskyLabel[] labels;
}