package com.laboratorio.blueskyapiinterface.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 02/08/2024
 * @updated 03/08/2024
 */

@Getter @Setter @AllArgsConstructor
public class BlueskyCreateRecordResponse {
    private String uri;
    private String cid;
}