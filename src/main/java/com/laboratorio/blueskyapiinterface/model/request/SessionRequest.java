package com.laboratorio.blueskyapiinterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 23/02/2025
 * @updated 23/02/2025
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SessionRequest {
    private String identifier;
    private String password;
}