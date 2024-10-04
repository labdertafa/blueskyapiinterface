package com.laboratorio.api;

import com.laboratorio.blueskyapiinterface.BlueskySessionApi;
import com.laboratorio.blueskyapiinterface.impl.BlueskySessionApiImpl;
import com.laboratorio.blueskyapiinterface.model.BlueskySession;
import com.laboratorio.blueskyapiinterface.utils.BlueskyApiConfig;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 01/08/2024
 * @updated 04/10/2024
 */
public class BlueskySessionApiTest {
    private BlueskySessionApi sessionApi;
    
    @BeforeEach
    public void initApi() {
        String accessToken = BlueskyApiConfig.getInstance().getProperty("test_access_token");
        String refreshToken = BlueskyApiConfig.getInstance().getProperty("test_refresh_token");
        this.sessionApi = new BlueskySessionApiImpl(accessToken, refreshToken);
    }
    
    @Test
    public void getSession() {
        String did = "did:plc:vli2z522aj2bancr24qugm7n";
        
        BlueskySession session = this.sessionApi.getSession();
        assertEquals(did, session.getDid());
    }
    
/*    @Test
    public void refreshSession() {
        String did = "did:plc:vli2z522aj2bancr24qugm7n";
        
        BlueskySession session = this.sessionApi.refreshSession();
        assertEquals(did, session.getDid());
    } */
}