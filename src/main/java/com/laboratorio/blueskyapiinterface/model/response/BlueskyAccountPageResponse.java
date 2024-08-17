package com.laboratorio.blueskyapiinterface.model.response;

import com.laboratorio.blueskyapiinterface.model.BlueskyAccount;
import com.laboratorio.blueskyapiinterface.model.BlueskyActor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 01/08/2024
 * @updated 02/08/2024
 */

@Getter @Setter @AllArgsConstructor
public class BlueskyAccountPageResponse {
    private BlueskyAccount subject;
    private String cursor;
    private BlueskyActor[] followers;
    private BlueskyActor[] follows;
}