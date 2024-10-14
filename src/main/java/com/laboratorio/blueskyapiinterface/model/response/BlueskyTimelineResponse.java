package com.laboratorio.blueskyapiinterface.model.response;

import com.laboratorio.blueskyapiinterface.model.BlueskyFeed;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 14/10/2024
 * @updated 14/10/2024
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyTimelineResponse {
    private List<BlueskyFeed> feed;
    private String cursor;
}