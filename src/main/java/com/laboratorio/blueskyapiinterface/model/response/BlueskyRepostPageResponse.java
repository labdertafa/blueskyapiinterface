package com.laboratorio.blueskyapiinterface.model.response;

import com.laboratorio.blueskyapiinterface.model.BlueskyActor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 03/08/2024
 * @updated 03/08/2024
 */

@Getter @Setter @AllArgsConstructor
public class BlueskyRepostPageResponse {
    private String uri;
    private String cid;
    private String cursor;
    private BlueskyActor[] repostedBy;
}