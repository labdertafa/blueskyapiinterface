package com.laboratorio.api;

import com.laboratorio.blueskyapiinterface.BlueskyNotificationApi;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.impl.BlueskyNotificationApiImpl;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyNotificationListResponse;
import com.laboratorio.blueskyapiinterface.utils.BlueskyApiConfig;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 04/08/2024
 * @updated 17/08/2024
 */
public class BlueskyNotificationApiTest {
    private BlueskyNotificationApi notificationApi;
    
    @BeforeEach
    public void initNotificationApi() {
        String accessToken = BlueskyApiConfig.getInstance().getProperty("test_access_token");
        this.notificationApi = new BlueskyNotificationApiImpl(accessToken);
    }
    
    @Test
    public void get10Notifications() throws Exception { // Usando limit por defecto
        int cantidad  = 10;
        
        BlueskyNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(0, cantidad);

        assertEquals(cantidad, notificationListResponse.getNotifications().size());
        assertTrue(notificationListResponse.getCursor() != null);
    }
    
    @Test
    public void get10NotificationsWithLimit() throws Exception { // Usando limit definido
        int limit = 80;
        int cantidad  = 10;
        
        BlueskyNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(limit, cantidad);

        assertEquals(cantidad, notificationListResponse.getNotifications().size());
        assertTrue(notificationListResponse.getCursor() != null);
    }
    
    @Test
    public void get110Notifications() throws Exception {
        int cantidad  = 110;
        
        BlueskyNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(cantidad);

        assertTrue(notificationListResponse.getNotifications().size() > 10);
    }
    
    @Test
    public void getAllNotifications() throws Exception {
        int limit = 100;
        
        BlueskyNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(limit);

        assertTrue(notificationListResponse.getNotifications().size() >= 0);
        assertTrue(notificationListResponse.getCursor() != null);
    }
    
    @Test
    public void getNotificationError() {
        this.notificationApi = new BlueskyNotificationApiImpl("INVALID_ACCESS_TOKEN");

        assertThrows(BlueskyException.class, () -> {
            this.notificationApi.getAllNotifications();
        });
    }
    
    @Test
    public void getNotificationsWithSinceId() throws Exception {
        int limit = 100;
        String sinceId = "2024-07-09T15:00:17.180Z";

        BlueskyNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(limit, 0, sinceId);

        assertTrue(notificationListResponse.getNotifications().size() >= 0);
        assertTrue(notificationListResponse.getCursor()!= null);
    }
}