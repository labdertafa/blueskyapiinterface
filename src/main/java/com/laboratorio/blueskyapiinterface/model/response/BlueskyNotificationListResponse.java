package com.laboratorio.blueskyapiinterface.model.response;

import com.laboratorio.blueskyapiinterface.model.BlueskyNotification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 04/08/2024
 * @updated 04/08/2024
 */
@Getter @Setter @AllArgsConstructor
public class BlueskyNotificationListResponse {
    private String cursor;
    private List<BlueskyNotification> notifications;
    private boolean priority;
    private String seenAt;
}