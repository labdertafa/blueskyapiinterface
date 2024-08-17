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
public class BlueskyLabel {
    private int ver;
    private String src;
    private String uri;
    private String cid;
    private String val;
    private boolean neg;
    private String cts;
    private String exp;
    private int sig;
}