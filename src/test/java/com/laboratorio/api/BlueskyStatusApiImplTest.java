package com.laboratorio.api;

import com.laboratorio.blueskyapiinterface.BlueskyStatusApi;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.impl.BlueskyStatusApiImpl;
import com.laboratorio.blueskyapiinterface.model.BlueskyActor;
import com.laboratorio.blueskyapiinterface.model.BlueskyStatus;
import com.laboratorio.blueskyapiinterface.model.BlueskySubject;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyCreateRecordResponse;
import com.laboratorio.blueskyapiinterface.utils.BlueskyApiConfig;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 03/08/2024
 * @updated 17/08/2024
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlueskyStatusApiImplTest {
    private BlueskyStatusApi statusApi;
    private static String uriElim = "";
    
    @BeforeEach
    public void initApi() {
        String accessToken = BlueskyApiConfig.getInstance().getProperty("test_access_token");
        this.statusApi = new BlueskyStatusApiImpl(accessToken);
    }
    
    @Test
    public void getStatusById() {
        String uri = "at://did:plc:vli2z522aj2bancr24qugm7n/app.bsky.feed.post/3kyslpcmpxd2v";
        
        BlueskyStatus status = this.statusApi.getStatusById(uri);
        assertEquals(uri, status.getUri());
    }
    
    @Test
    public void getInvalidStatusId() {
        String uri = "XX://did:plc:vli2z522aj2bancr24qugm7n/app.bsky.feed.post/3kyslpcmpxd2v";
        
        assertThrows(BlueskyException.class, () -> {
            this.statusApi.getStatusById(uri);
        });
    }
    
    @Test
    public void getStatusByIds() {
        String[] ids = {"at://did:plc:vli2z522aj2bancr24qugm7n/app.bsky.feed.post/3kyslpcmpxd2v", "at://did:plc:vli2z522aj2bancr24qugm7n/app.bsky.feed.post/3kypxauazbl2z"};
        
        List<BlueskyStatus> statuses = this.statusApi.getStatusByIds(ids);
        assertTrue(statuses.size() == 2);
    }
    
    @Test @Order(1)
    public void postStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String text = "Estado de prueba enviado desde la aplicación SocialImprove con un test unitario JUNIT. Tiene un link: https://laboratoriorafa.mooo.com y 2 hashtags #SiguemeYTeSigo #Followback";
        
        BlueskyCreateRecordResponse response = this.statusApi.postStatus(userId, text);
        uriElim = response.getUri();
        assertTrue(response.getUri() != null);
    }
    
    @Test
    public void postInvalidStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String text = null;
        
        assertThrows(BlueskyException.class, () -> {
            this.statusApi.postStatus(userId, text);
        });
    }
    
    @Test @Order(2)
    public void deleteStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        // uriElim = "at://did:plc:vli2z522aj2bancr24qugm7n/app.bsky.feed.post/3kysvm6ut3t2v";
        
        boolean result = this.statusApi.deleteStatus(userId, uriElim);
        assertTrue(result);
    }
    
    @Test
    public void deleteInvalidStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String uri = null;
        
        assertThrows(BlueskyException.class, () -> {
            this.statusApi.deleteStatus(userId, uri);
        });
    }
    
    @Test @Order(7)
    public void postImage() throws Exception {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String imagen = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        String text = "Estado de prueba enviado desde la aplicación SocialImprove con un test unitario JUNIT. Tiene un link: https://laboratoriorafa.mooo.com y 2 hashtags #SiguemeYTeSigo #Followback";
        
        BlueskyCreateRecordResponse response = this.statusApi.postStatus(userId, text, imagen);
        uriElim = response.getUri();
        assertTrue(response.getUri() != null);
    }
    
    @Test @Order(8)
    public void deleteImageStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        // uriElim = "at://did:plc:vli2z522aj2bancr24qugm7n/app.bsky.feed.post/3kysvm6ut3t2v";
        
        boolean result = this.statusApi.deleteStatus(userId, uriElim);
        assertTrue(result);
    }
    
    @Test @Order(9)
    public void postYoutubeStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String text = "Esta es la lista con los capítulos del tutorial del lenguaje #Java:  https://www.youtube.com/watch?v=zkSz6gkY2hk&list=PLtdeXn2f7ZbPXR2R0JC0Qc8OURym5-Zze. Suscríbete a mi canal. #Programacion #Objetos #SiguemeYTeSigo #Followback";
        
        BlueskyCreateRecordResponse response = this.statusApi.postStatus(userId, text);
        uriElim = response.getUri();
        assertTrue(response.getUri() != null);
    }
    
    @Test @Order(10)
    public void deleteYoutubeStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        // uriElim = "at://did:plc:vli2z522aj2bancr24qugm7n/app.bsky.feed.post/3kysvm6ut3t2v";
        
        boolean result = this.statusApi.deleteStatus(userId, uriElim);
        assertTrue(result);
    }
    
    @Test
    public void getRebloggedBy() throws Exception {
        String uri = "at://did:plc:inttb7q4wphod7lj7x4o3b7x/app.bsky.feed.post/3kyqdlkrzij2k";
        
        List<BlueskyActor> accounts = this.statusApi.getRebloggedBy(uri);
        assertTrue(!accounts.isEmpty());
    }
    
    @Test
    public void getInvalidRebloggedBy() {
        String uri = "atXXXdid:plc:inttb7q4wphod7lj7x4o3b7x/app.bsky.feed.post/3kyqdlkrzij2k";
        
        assertThrows(BlueskyException.class, () -> {
            this.statusApi.getRebloggedBy(uri);
        });
    }
    
    @Test
    public void getFavouritedBy() throws Exception {
        String uri = "at://did:plc:inttb7q4wphod7lj7x4o3b7x/app.bsky.feed.post/3kyqdlkrzij2k";
        
        List<BlueskyActor> accounts = this.statusApi.getFavouritedBy(uri);
        assertTrue(!accounts.isEmpty());
    }
    
    @Test
    public void getInvalidFavouritedBy() {
        String uri = "atXXXdid:plc:inttb7q4wphod7lj7x4o3b7x/app.bsky.feed.post/3kyqdlkrzij2k";
        
        assertThrows(BlueskyException.class, () -> {
            this.statusApi.getFavouritedBy(uri);
        });
    }
    
    
    @Test @Order(3)
    public void reblogStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        BlueskySubject subject = new BlueskySubject("bafyreichier7pufq2xikomrzon6fi2myjxqjxv3gxuhirewzwxxl7orvta", "at://did:plc:tefnke5f2ym4pww43jboqx2o/app.bsky.feed.post/3kyqjsntbpx2s");
        
        BlueskyCreateRecordResponse response = this.statusApi.reblogStatus(userId, subject);
        uriElim = response.getUri();
        assertTrue(response.getUri() != null);
    }
    
    @Test
    public void reblogInvalidStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        BlueskySubject subject = new BlueskySubject("bafyreichier7pufq2xikomrzon6fi2myjxqjxv3gxuhirewzwxxl7orvta", null);
        
        assertThrows(BlueskyException.class, () -> {
            this.statusApi.reblogStatus(userId, subject);
        });
    }
    
    @Test @Order(4)
    public void unreblogStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        // uriElim = "at://did:plc:vli2z522aj2bancr24qugm7n/app.bsky.feed.repost/3kysxqqrxkc2k";
        
        boolean result = this.statusApi.unreblogStatus(userId, uriElim);
        assertTrue(result);
    }
    
    @Test
    public void unreblogInvalidStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String uri = null;
        
        assertThrows(BlueskyException.class, () -> {
            this.statusApi.unreblogStatus(userId, uri);
        });
    }
    
    @Test @Order(5)
    public void favouriteStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        BlueskySubject subject = new BlueskySubject("bafyreichier7pufq2xikomrzon6fi2myjxqjxv3gxuhirewzwxxl7orvta", "at://did:plc:tefnke5f2ym4pww43jboqx2o/app.bsky.feed.post/3kyqjsntbpx2s");
        
        BlueskyCreateRecordResponse response = this.statusApi.favouriteStatus(userId, subject);
        uriElim = response.getUri();
        assertTrue(response.getUri() != null);
    }
    
    @Test
    public void favouriteInvalidStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        BlueskySubject subject = new BlueskySubject("bafyreichier7pufq2xikomrzon6fi2myjxqjxv3gxuhirewzwxxl7orvta", null);
        
        assertThrows(BlueskyException.class, () -> {
            this.statusApi.favouriteStatus(userId, subject);
        });
    }
    
    @Test @Order(6)
    public void unfavouriteStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        // uriElim = "at://did:plc:vli2z522aj2bancr24qugm7n/app.bsky.feed.like/3kysyozg2kd2l";
        
        boolean result = this.statusApi.unfavouriteStatus(userId, uriElim);
        assertTrue(result);
    }
    
    @Test
    public void unfavouriteInvalidStatus() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String uri = null;
        
        assertThrows(BlueskyException.class, () -> {
            this.statusApi.unfavouriteStatus(userId, uri);
        });
    }
}