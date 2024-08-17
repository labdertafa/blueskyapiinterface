package com.laboratorio.blueskyapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 01/08/2024
 * @updated 01/08/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskySession {
    private String did;
    // private DidDoc didDoc;
    private String handle;
    private String accessJwt;
    private String refreshJwt;
    private String email;
    private boolean emailConfirmed;
    private boolean emailAuthFactor;
    private boolean active;
    private String status;
}