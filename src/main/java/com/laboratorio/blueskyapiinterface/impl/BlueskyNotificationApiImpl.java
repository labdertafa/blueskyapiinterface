package com.laboratorio.blueskyapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.blueskyapiinterface.BlueskyNotificationApi;
import com.laboratorio.blueskyapiinterface.model.BlueskyNotification;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyNotificationListResponse;
import com.laboratorio.clientapilibrary.exceptions.ApiClientException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 04/08/2024
 * @updated 04/10/2024
 */
public class BlueskyNotificationApiImpl extends BlueskyBaseApi implements BlueskyNotificationApi {
    public BlueskyNotificationApiImpl(String accessToken) {
        super(accessToken);
    }

    @Override
    public BlueskyNotificationListResponse getAllNotifications() throws Exception {
        return this.getAllNotifications(0);
    }
    
    @Override
    public BlueskyNotificationListResponse getAllNotifications(int limit) throws Exception {
        return this.getAllNotifications(limit, null);
    }

    // Función que devuelve una página de notificaciones de una cuenta
    private BlueskyNotificationListResponse getNotificationPage(String url, int limit, int okStatus, String posicionInicial) throws Exception {
        try {
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.GET);
            request.addApiPathParam("limit", Integer.toString(limit));
            if (posicionInicial != null) {
                request.addApiPathParam("cursor", posicionInicial);
            }
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            BlueskyNotificationListResponse notificationListResponse = gson.fromJson(response.getResponseStr(), BlueskyNotificationListResponse.class);
            log.debug("Resultados encontrados: " + notificationListResponse.getNotifications().size());
            log.debug("Valor del seenAt: " + notificationListResponse.getSeenAt());
            log.debug("Valor del max_id: " + notificationListResponse.getCursor());

            // return accounts;
            return notificationListResponse;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (ApiClientException e) {
            throw e;
        }
    }

    @Override
    public BlueskyNotificationListResponse getAllNotifications(int limit, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getNotifications_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getNotifications_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getNotifications_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getNotifications_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        List<BlueskyNotification> notifications = null;
        boolean continuar = true;
        String max_id = null;
        boolean priority;
        String seenAt;
        
        try {
            do {
                BlueskyNotificationListResponse notificationListResponse = this.getNotificationPage(endpoint, usedLimit, okStatus, max_id);
                if (notifications == null) {
                    notifications = notificationListResponse.getNotifications();
                } else {
                    notifications.addAll(notificationListResponse.getNotifications());
                }
                
                max_id = notificationListResponse.getCursor();
                priority = notificationListResponse.isPriority();
                seenAt = notificationListResponse.getSeenAt();
                log.debug("getFollowers. Posicion inicial: " +  posicionInicial + " Recuperados: " + notifications.size() + ". Max_id: " + max_id);
                if (posicionInicial != null) {
                    // Se mira si hay que pedir otra página
                    BlueskyNotification firstNotification = notifications.get(notifications.size() - 1);
                    if (firstNotification.getIndexedAt().compareTo(posicionInicial) <= 0) {
                        continuar = false;
                    }
                } else {    // Se están pidiendo todas las notificaciones
                    if (max_id == null) {
                        continuar = false;
                    }
                }
            } while (continuar);

            if (posicionInicial == null) {
                return new BlueskyNotificationListResponse(max_id, notifications, priority, seenAt);
            }
            
            for (int i = notifications.size() - 1; i >=0 ; i--) {
                if (notifications.get(i).getIndexedAt().compareTo(posicionInicial) <= 0) {
                    notifications.remove(i);
                } else {
                    break;
                }
            }
            
            return new BlueskyNotificationListResponse(max_id, notifications, priority, seenAt);
        } catch (Exception e) {
            throw e;
        }
    }
}