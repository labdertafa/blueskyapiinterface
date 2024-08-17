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
 * @updated 03/08/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyKnownFollowers {
    private int count;
    private BlueskyActor[] followers;
}