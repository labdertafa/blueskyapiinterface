package com.laboratorio.blueskyapiinterface.model.response;

import com.laboratorio.blueskyapiinterface.model.BlueskyLike;
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
public class BlueskyLikePageResponse {
    private String uri;
    private String cid;
    private String cursor;
    private BlueskyLike[] likes;
}