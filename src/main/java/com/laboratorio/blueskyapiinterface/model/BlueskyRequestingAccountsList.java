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
public class BlueskyRequestingAccountsList {
    private String uri;
    private String cid;
    private String name;
    // BlueskyPurpose purpose;
    private String avatar;
    private int listItemCount;
    BlueskyLabel[] labels;
    private String indexedAt;
}