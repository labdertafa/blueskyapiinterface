package com.laboratorio.blueskyapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 03/08/2024
 * @updated 03/08/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyStatus {
    private String uri;
    private String cid;
    private BlueskyAccount author;
    private BlueskyStatusRecord record;
    private int replyCount;
    private int repostCount;
    private int likeCount;
    private String indexedAt;
    // private BlueskyPostViewer viewer;
    private BlueskyLabel[] labels;
    // private BlueskyThreadgate threadgate;
}