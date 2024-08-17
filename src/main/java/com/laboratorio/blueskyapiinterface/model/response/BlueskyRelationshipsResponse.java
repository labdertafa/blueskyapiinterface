package com.laboratorio.blueskyapiinterface.model.response;

import com.laboratorio.blueskyapiinterface.model.BlueskyRelationship;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 02/08/2024
 * @updated 04/08/2024
 */

@Getter @Setter @AllArgsConstructor
public class BlueskyRelationshipsResponse {
    private String actor;
    private List<BlueskyRelationship> relationships;
}