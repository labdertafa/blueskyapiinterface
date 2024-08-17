package com.laboratorio.api;

import com.laboratorio.blueskyapiinterface.BlueskyAccountApi;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.impl.BlueskyAccountApiImpl;
import com.laboratorio.blueskyapiinterface.model.BlueskyAccount;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyFollowListResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyRelationshipsResponse;
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
 * @created 02/08/2024
 * @updated 17/08/2024
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlueskyAccountApiTest {
    private BlueskyAccountApi accountApi;
    
    @BeforeEach
    public void initApi() {
        String accessToken = BlueskyApiConfig.getInstance().getProperty("test_access_token");
        this.accountApi = new BlueskyAccountApiImpl(accessToken);
    }
    
    @Test
    public void getAccountById() {
        String did = "did:plc:r3mpahk677b3m3iuub7p35zc";
        
        BlueskyAccount account = this.accountApi.getAccountById(did);
        assertEquals(did, account.getDid());
    }
    
    @Test
    public void findAccountByInvalidId() {
        String did = "did:plc:r3mpahk677b3m3iuubXXXXXXXX";
        
        assertThrows(BlueskyException.class, () -> {
            this.accountApi.getAccountById(did);
        });
    }
    
    @Test
    public void getAccountsById() {
        List<String> ids = List.of("did:plc:zkxkfxfchezxypxrtgc7yaqz", "did:plc:r3mpahk677b3m3iuub7p35zc");
        
        List<BlueskyAccount> accounts = this.accountApi.getAccountsById(ids);
        assertEquals(2, accounts.size());
    }
    
    @Test
    public void getAccountsByInvalidId() {
        List<String> ids = List.of("diXXplc:zkxkfxfchezxypxrtgc7yaqz", "diXXplc:r3mpahk677b3m3iuub7p35zc");
        
        assertThrows(BlueskyException.class, () -> {
            this.accountApi.getAccountsById(ids);
        });
    }
    
    @Test
    public void getAccountByUsername() {
        String username = "bakercilla.bsky.social";
        
        BlueskyAccount account = this.accountApi.getAccountByUsername(username);
        assertEquals(username, account.getHandle());
    }
    
    @Test
    public void getAccountByInvalidUsername() {
        String username = "bakercilla.bsky.socialAXMNLXJ";
        
        assertThrows(BlueskyException.class, () -> {
            this.accountApi.getAccountByUsername(username);
        });
    }
    
    @Test
    public void get40Followers() throws Exception {     // USando limit por defecto
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        int cantidad = 40;
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowers(did, 0, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getCursor().isEmpty());
    }
    
    @Test
    public void get40FollowersWithLimit() throws Exception {     // Usando un limit
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        int limit = 80;
        int cantidad = 40;
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowers(did, limit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getCursor().isEmpty());
    }
    
    @Test
    public void get100Followers() throws Exception {
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        int limit = 100;
        int cantidad = 100;
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowers(did, limit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getCursor().isEmpty());
    }
    
    @Test
    public void get101Followers() throws Exception {
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        int limit = 100;
        int cantidad = 101;
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowers(did, limit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getCursor().isEmpty());
    }
    
    @Test
    public void getAllFollowers() throws Exception {
        int limit = 80;
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowers(did, limit);

        assertTrue(!accountListResponse.getAccounts().isEmpty());
        assertTrue(accountListResponse.getCursor() == null);
    }
    
    @Test
    public void getAllFollowersInvalidId() {
        String did = "did:plc:zkxkfxfchezxypxrtgc7yXXXXXX";
        
        assertThrows(BlueskyException.class, () -> {
            this.accountApi.getFollowers(did);
        });
    }
    
    @Test
    public void get40Followings() throws Exception {    // Con limit por defecto
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        int cantidad = 40;
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowings(did, 0, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getCursor().isEmpty());
    }
    
    @Test
    public void get40FollowingsWithLimit() throws Exception {    // Con limit definido
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        int limit = 80;
        int cantidad = 40;
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowings(did, limit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getCursor().isEmpty());
    }
    
    @Test
    public void get100Followings() throws Exception {
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        int limit = 100;
        int cantidad = 100;
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowings(did, limit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getCursor().isEmpty());
    }
    
    @Test
    public void get101Followings() throws Exception {
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        int limit = 100;
        int cantidad = 101;
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowings(did, limit, cantidad);

        assertEquals(cantidad, accountListResponse.getAccounts().size());
        assertTrue(!accountListResponse.getCursor().isEmpty());
    }
    
    @Test
    public void getAllFollowings() throws Exception {
        String did = "did:plc:zkxkfxfchezxypxrtgc7yaqz";
        int limit = 80;
        
        BlueskyFollowListResponse accountListResponse = this.accountApi.getFollowings(did, limit);

        assertTrue(!accountListResponse.getAccounts().isEmpty());
        assertTrue(accountListResponse.getCursor() == null);
    }
    
    @Test
    public void getAllFollowingsInvalidId() {
        String did = "did:plc:zkxkfxfchezxypxrtgc7yXXXXXX";
        
        assertThrows(BlueskyException.class, () -> {
            this.accountApi.getFollowings(did);
        });
    }
    
    @Test @Order(1)
    public void followAccount() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String subject = "did:plc:rfplejykf2neyod46je5e7sm";
        
        boolean result = this.accountApi.followAccount(userId, subject);
        assertTrue(result);
    }
    
    @Test
    public void followInvalidAccount() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String subject = "diXXXlc:ytanl7b6yr5rswhnygnr3a7j";
        
        assertThrows(BlueskyException.class, () -> {
            this.accountApi.followAccount(userId, subject);
        });
    }
    
    @Test @Order(2)
    public void unfollowAccount() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String id = "did:plc:rfplejykf2neyod46je5e7sm";
        
        BlueskyAccount account = this.accountApi.getAccountById(id);
        String uri = account.getViewer().getFollowing();
                
        boolean result = this.accountApi.unfollowAccount(userId, uri);
        assertTrue(result);
    }
    
    @Test
    public void unfollowInvalidAccount() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        String uri = "";
        
        assertThrows(BlueskyException.class, () -> {
            this.accountApi.unfollowAccount(userId, uri);
        });
    }
    
    @Test
    public void checkRelationships() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        List<String> ids = List.of("did:plc:zkxkfxfchezxypxrtgc7yaqz", "did:plc:r3mpahk677b3m3iuub7p35zc");
        
        BlueskyRelationshipsResponse response = this.accountApi.checkrelationships(userId, ids);
        assertTrue(response.getRelationships().size() == 2);
    }
    
    @Test
    public void checkInvalidRelationship() {
        String userId = "did:plc:vli2z522aj2bancr24qugm7n";
        List<String> ids = List.of("diXXplc:zkxkfxfchezxypxrtgc7yaqz");
        
        assertThrows(BlueskyException.class, () -> {
            this.accountApi.checkrelationships(userId, ids);
        });
    }
}