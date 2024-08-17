package com.laboratorio.blueskyapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laboratorio.blueskyapiinterface.BlueskyNotificationApi;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.model.BlueskyNotification;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyNotificationListResponse;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 04/08/2024
 * @updated 17/08/2024
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
        return this.getAllNotifications(limit, 0);
    }

    @Override
    public BlueskyNotificationListResponse getAllNotifications(int limit, int quantity) throws Exception {
        return this.getAllNotifications(limit, quantity, null);
    }
    
    // Función que devuelve una página de notificaciones de una cuenta
    private BlueskyNotificationListResponse getNotificationPage(String url, int limit, int okStatus, String posicionInicial) throws Exception {
        Client client = ClientBuilder.newClient();
        Response response = null;
        
        try {
            WebTarget target = client.target(url)
                        .queryParam("limit", limit);
            if (posicionInicial != null) {
                target = target.queryParam("cursor", posicionInicial);
            }
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            log.debug("Se ejecutó la query: " + url);
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskyNotificationApiImpl.class.getName(), str);
            }
            
            Gson gson = new Gson();
            BlueskyNotificationListResponse notificationListResponse = gson.fromJson(jsonStr, BlueskyNotificationListResponse.class);
            log.debug("Resultados encontrados: " + notificationListResponse.getNotifications().size());
            log.debug("Valor del max_id: " + notificationListResponse.getCursor());

            // return accounts;
            return notificationListResponse;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (BlueskyException e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }

    @Override
    public BlueskyNotificationListResponse getAllNotifications(int limit, int quantity, String posicionInicial) throws Exception {
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
        String max_id = posicionInicial;
        boolean priority;
        String seenAt;
        
        if (quantity > 0) {
            usedLimit = Math.min(usedLimit, quantity);
        }
        
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
                log.debug("getFollowers. Cantidad: " + quantity + ". Recuperados: " + notifications.size() + ". Max_id: " + max_id);
                if (quantity > 0) {
                    if ((notifications.size() >= quantity) || (max_id == null)) {
                        continuar = false;
                    }
                } else {
                    if (max_id == null) {
                        continuar = false;
                    }
                }
            } while (continuar);

            if (quantity == 0) {
                String ultimaPosicion = posicionInicial;
                // Se buscar el índice de la última notificación
                if (!notifications.isEmpty()) {
                    BlueskyNotification bn = notifications.get(notifications.size() - 1);
                    ultimaPosicion = bn.getIndexedAt();
                }
                return new BlueskyNotificationListResponse(ultimaPosicion, notifications, priority, seenAt);
            }
            
            return new BlueskyNotificationListResponse(max_id, notifications.subList(0, Math.min(quantity, notifications.size())), priority, seenAt);
        } catch (Exception e) {
            throw e;
        }
    }
}