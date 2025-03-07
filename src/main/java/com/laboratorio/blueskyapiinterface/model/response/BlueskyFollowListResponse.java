package com.laboratorio.blueskyapiinterface.model.response;

import com.laboratorio.blueskyapiinterface.model.BlueskyAccount;
import com.laboratorio.blueskyapiinterface.model.BlueskyActor;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 02/08/2024
 * @updated 22/10/2024
 */

@Getter @Setter @AllArgsConstructor
public class BlueskyFollowListResponse {
    private String cursor;
    private List<BlueskyAccount> accounts;
}