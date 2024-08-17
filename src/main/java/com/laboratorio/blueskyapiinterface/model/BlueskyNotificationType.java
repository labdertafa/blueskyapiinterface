package com.laboratorio.blueskyapiinterface.model;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 04/08/2024
 * @updated 04/08/2024
 */
public enum BlueskyNotificationType {
    LIKE, REPOST, FOLLOW, MENTION, REPLY, QUOTE, STARTERPACK_JOINED;
    
    public static BlueskyNotificationType fromString(String value) {
        for (BlueskyNotificationType type : BlueskyNotificationType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant " + BlueskyNotificationType.class.getCanonicalName() + "." + value);
    }
}