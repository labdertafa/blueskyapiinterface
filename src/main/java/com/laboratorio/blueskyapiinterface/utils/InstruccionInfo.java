package com.laboratorio.blueskyapiinterface.utils;

import com.laboratorio.blueskyapiinterface.impl.TypeAccountList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 16/08/2024
 * @updated 17/08/2024
 */

@Getter @Setter @AllArgsConstructor
public class InstruccionInfo {
    private TypeAccountList typeAccountList;
    private String endpoint;
    private int okStatus;
    private int limit;
}