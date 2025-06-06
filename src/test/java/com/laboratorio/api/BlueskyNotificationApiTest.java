package com.laboratorio.api;

import com.laboratorio.blueskyapiinterface.BlueskyNotificationApi;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.impl.BlueskyNotificationApiImpl;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyNotificationListResponse;
import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 04/08/2024
 * @updated 05/06/2025
 */
public class BlueskyNotificationApiTest {
    private BlueskyNotificationApi notificationApi;
    
    @BeforeEach
    public void initNotificationApi() {
        ReaderConfig config = new ReaderConfig("config//bluesky_api.properties");
        String accessToken = config.getProperty("test_access_token");
        this.notificationApi = new BlueskyNotificationApiImpl(accessToken);
    }
    
/*    @Test
    public void getNotificationsWithSinceIdAndDefaultLimit() throws Exception { // Usando limit por defecto
        String sinceId = "2024-08-31T15:00:17.180Z";
        
        BlueskyNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(0, sinceId);

        assertTrue(!notificationListResponse.getNotifications().isEmpty());
    } */
    
/*    @Test 
    public void getAllNotificationsWithLimit() throws Exception { // Usando limit definido
        int limit = 80;
        
        BlueskyNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(limit);

        assertTrue(!notificationListResponse.getNotifications().isEmpty());
    } */
    
    @Test
    public void getNotificationError() {
        this.notificationApi = new BlueskyNotificationApiImpl("INVALID_ACCESS_TOKEN");

        assertThrows(BlueskyException.class, () -> {
            this.notificationApi.getAllNotifications();
        });
    }
    
    /* @Test
    public void getNotificationsWithSinceId() throws Exception {
        int limit = 100;
        String sinceId = "2024-08-31T15:00:17.180Z";

        BlueskyNotificationListResponse notificationListResponse = this.notificationApi.getAllNotifications(limit, sinceId);

        assertTrue(notificationListResponse.getNotifications().size() >= 0);
    } */
}