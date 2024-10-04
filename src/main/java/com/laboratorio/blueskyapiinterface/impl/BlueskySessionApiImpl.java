package com.laboratorio.blueskyapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.blueskyapiinterface.BlueskySessionApi;
import com.laboratorio.blueskyapiinterface.model.BlueskySession;
import com.laboratorio.clientapilibrary.exceptions.ApiClientException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 01/08/2024
 * @updated 04/10/2024
 */
public class BlueskySessionApiImpl extends BlueskyBaseApi implements BlueskySessionApi {
    private final String refreshToken;
    
    public BlueskySessionApiImpl(String accessToken, String refreshToken) {
        super(accessToken);
        this.refreshToken = refreshToken;
    }

    @Override
    public BlueskySession getSession() {
        String endpoint = this.apiConfig.getProperty("getSession_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getSession_ok_status"));
        
        try {
            String url = endpoint;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.GET);
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), BlueskySession.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (ApiClientException e) {
            throw e;
        }
    }

    @Override
    public BlueskySession refreshSession() {
        String endpoint = this.apiConfig.getProperty("refreshSession_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("refreshSession_ok_status"));
        
        try {
            String url = endpoint;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.POST);
            request.addApiHeader("Authorization", "Bearer " + this.refreshToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), BlueskySession.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (ApiClientException e) {
            throw  e;
        }
    }   
}