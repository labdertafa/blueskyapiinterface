package com.laboratorio.blueskyapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laboratorio.blueskyapiinterface.BlueskyAccountApi;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.model.BlueskyAccount;
import com.laboratorio.blueskyapiinterface.model.BlueskyCreateRecord;
import com.laboratorio.blueskyapiinterface.model.BlueskyDeleteRecord;
import com.laboratorio.blueskyapiinterface.model.BlueskyRecord;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyAccountListResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyCreateRecordResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyFollowListResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyRelationshipsResponse;
import com.laboratorio.blueskyapiinterface.utils.InstruccionInfo;
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
 * @created 02/08/2024
 * @updated 17/08/2024
 */
public class BlueskyAccountApiImpl extends BlueskyBaseApi implements BlueskyAccountApi {
   public BlueskyAccountApiImpl(String accessToken) {
        super(accessToken);
    }

    @Override
    public BlueskyAccount getAccountById(String id) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("getAccountById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountById_ok_status"));
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url)
                    .queryParam("actor", id);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskyAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, BlueskyAccount.class);
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
    public List<BlueskyAccount> getAccountsById(List<String> ids) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("getAccountsById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountsById_ok_status"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getAccountsById_max_limit"));
        List<String> idsToProcess = ids.subList(0, Math.min(maxLimit, ids.size()));
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url);
            for (String id: idsToProcess) {
                target = target.queryParam("actors", id);
            }
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskyAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, BlueskyAccountListResponse.class).getProfiles();
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
    public BlueskyAccount getAccountByUsername(String username) {
        return this.getAccountById(username);
    }

    @Override
    public BlueskyFollowListResponse getFollowers(String id) throws Exception {
        return this.getFollowers(id, 0);
    }
    
    @Override
    public BlueskyFollowListResponse getFollowers(String id, int limit) throws Exception {
        return this.getFollowers(id, limit, 0);
    }

    @Override
    public BlueskyFollowListResponse getFollowers(String id, int limit, int quantity) throws Exception {
        return getFollowers(id, limit, quantity, null);
    }

    @Override
    public BlueskyFollowListResponse getFollowers(String id, int limit, int quantity, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getFollowers_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowers_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowers_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowers_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        InstruccionInfo instruccionInfo = new InstruccionInfo(TypeAccountList.FOLLOW, endpoint, okStatus, usedLimit);
        return this.getBlueskyAccountList(instruccionInfo, id, quantity, posicionInicial);
    }

    @Override
    public BlueskyFollowListResponse getFollowings(String id) throws Exception {
        return this.getFollowings(id, 0);
    }
    
    @Override
    public BlueskyFollowListResponse getFollowings(String id, int limit) throws Exception {
        return this.getFollowings(id, limit, 0);
    }

    @Override
    public BlueskyFollowListResponse getFollowings(String id, int limit, int quantity) throws Exception {
        return this.getFollowings(id, limit, quantity, null);
    }

    @Override
    public BlueskyFollowListResponse getFollowings(String id, int limit, int quantity, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getFollowings_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowings_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowings_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowings_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        InstruccionInfo instruccionInfo = new InstruccionInfo(TypeAccountList.FOLLOW, endpoint, okStatus, usedLimit);
        return this.getBlueskyAccountList(instruccionInfo, id, quantity, posicionInicial);
    }

    @Override
    public boolean followAccount(String userId, String id) {
        String collection = this.apiConfig.getProperty("follow_collection");
        BlueskyRecord<String> record = new BlueskyRecord(id);
        BlueskyCreateRecord<String> createRecord = new BlueskyCreateRecord(userId, collection, record);
        
        BlueskyCreateRecordResponse createRecordResponse = this.createRecord(createRecord);
        
        return (createRecordResponse.getUri() != null);
    }
    
    @Override
    public boolean unfollowAccount(String userId, String uri) {
        String collection = this.apiConfig.getProperty("follow_collection");
        String rkey = this.extractRKey(uri);
        BlueskyDeleteRecord unfollowRecord = new BlueskyDeleteRecord(userId, collection, rkey);

        return this.deleteRecord(unfollowRecord);
    }

    @Override
    public BlueskyRelationshipsResponse checkrelationships(String userId, List<String> ids) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("checkrelationships_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("checkrelationships_ok_status"));
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url)
                    .queryParam("actor", userId);
            for (String id : ids) {
                target = target.queryParam("others", id);
            }
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskyAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, BlueskyRelationshipsResponse.class);
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
}