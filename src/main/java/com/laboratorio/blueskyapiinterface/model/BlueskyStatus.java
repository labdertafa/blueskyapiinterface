package com.laboratorio.blueskyapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 03/08/2024
 * @updated 14/10/2024
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
    private BlueskyLabel[] labels;

    @Override
    public String toString() {
        return "BlueskyStatus{" + "uri=" + uri + ", cid=" + cid + ", replyCount=" + replyCount + ", repostCount=" + repostCount + ", likeCount=" + likeCount + ", indexedAt=" + indexedAt + '}';
    }
}