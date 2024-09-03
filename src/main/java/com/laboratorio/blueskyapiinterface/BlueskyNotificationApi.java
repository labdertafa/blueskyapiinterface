package com.laboratorio.blueskyapiinterface;

import com.laboratorio.blueskyapiinterface.model.response.BlueskyNotificationListResponse;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 04/08/2024
 * @updated 03/09/2024
 */
public interface BlueskyNotificationApi {
    // Obtiene las notificaciones del usuario. Quantity indica el número de registros a recuperar (cero significa todos). Se puede indicar una posición inicial (en su ausencia se asume que es nula).
    BlueskyNotificationListResponse getAllNotifications() throws Exception;
    BlueskyNotificationListResponse getAllNotifications(int limit) throws Exception;
    BlueskyNotificationListResponse getAllNotifications(int limit, String posicionInicial) throws Exception;
}