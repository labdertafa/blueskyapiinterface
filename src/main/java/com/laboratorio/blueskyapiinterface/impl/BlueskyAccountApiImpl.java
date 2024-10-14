package com.laboratorio.blueskyapiinterface.impl;

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
import com.laboratorio.blueskyapiinterface.model.response.BlueskySuggestionsListResponse;
import com.laboratorio.blueskyapiinterface.utils.InstruccionInfo;
import com.laboratorio.clientapilibrary.exceptions.ApiClientException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 02/08/2024
 * @updated 14/10/2024
 */
public class BlueskyAccountApiImpl extends BlueskyBaseApi implements BlueskyAccountApi {
   public BlueskyAccountApiImpl(String accessToken) {
        super(accessToken);
    }

    @Override
    public BlueskyAccount getAccountById(String id) {
        String endpoint = this.apiConfig.getProperty("getAccountById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountById_ok_status"));
        
        try {
            String url = endpoint;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.GET);
            request.addApiPathParam("actor", id);
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), BlueskyAccount.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (ApiClientException e) {
            throw e;
        }
    }
    
    @Override
    public List<BlueskyAccount> getAccountsById(List<String> ids) {
        String endpoint = this.apiConfig.getProperty("getAccountsById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountsById_ok_status"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getAccountsById_max_limit"));
        List<String> idsToProcess = ids.subList(0, Math.min(maxLimit, ids.size()));
        
        try {
            String url = endpoint;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.GET);
            for (String id: idsToProcess) {
                request.addApiPathParam("actors", id);
            }
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), BlueskyAccountListResponse.class).getProfiles();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (ApiClientException e) {
            throw e;
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
        String endpoint = this.apiConfig.getProperty("checkrelationships_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("checkrelationships_ok_status"));
        
        try {
            String url = endpoint;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.GET);
            request.addApiPathParam("actor", userId);
            for (String id : ids) {
                request.addApiPathParam("others", id);
            }
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), BlueskyRelationshipsResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (ApiClientException e) {
            throw e;
        }
    }
    
    private BlueskySuggestionsListResponse getSuggestionPage(String uri, int okStatus, int limit, String cursor) {
        try {
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            request.addApiPathParam("limit", Integer.toString(limit));
            if (cursor != null) {
                request.addApiPathParam("cursor", cursor);
            }
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), BlueskySuggestionsListResponse.class);
        } catch (BlueskyException e) {
            throw e;
        }
    }

    @Override
    public List<BlueskyAccount> getSuggestions(int quantity) {
        String endpoint = this.apiConfig.getProperty("getSuggestions_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getSuggestions_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getSuggestions_default_limit"));
        
        List<BlueskyAccount> accounts = null;
        boolean continuar = true;
        String cursor = null;
        
        try {
            String uri = endpoint;
            
            do {
                BlueskySuggestionsListResponse suggestionsListResponse = this.getSuggestionPage(uri, okStatus, defaultLimit, cursor);
                log.debug("Elementos recuperados total: " + suggestionsListResponse.getActors().size());
                List<String> usersId = suggestionsListResponse.getActors().stream()
                        .map(a -> a.getDid())
                        .collect(Collectors.toList());
                List<BlueskyAccount> accountsDetails = this.getAccountsById(usersId);
                if (accounts == null) {
                    accounts = accountsDetails;
                } else {
                    accounts.addAll(accountsDetails);
                }
                
                cursor = suggestionsListResponse.getCursor();
                log.debug("getSuggestions. Recuperados: " + accounts.size() + ". Cursor: " + cursor);
                if (suggestionsListResponse.getActors().isEmpty()) {
                    continuar = false;
                } else {
                    if ((cursor == null) || (accounts.size() >= quantity)) {
                        continuar = false;
                    }
                }
            } while (continuar);
            
            return accounts.subList(0, Math.min(quantity, accounts.size()));
        } catch (Exception e) {
            throw e;
        }
    }
}