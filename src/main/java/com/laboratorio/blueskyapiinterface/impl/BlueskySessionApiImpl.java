package com.laboratorio.blueskyapiinterface.impl;

import com.laboratorio.blueskyapiinterface.BlueskySessionApi;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.model.BlueskySession;
import com.laboratorio.blueskyapiinterface.model.request.SessionRequest;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;

/**
 *
 * @author Rafael
 * @version 1.3
 * @created 01/08/2024
 * @updated 05/06/2025
 */
public class BlueskySessionApiImpl extends BlueskyBaseApi implements BlueskySessionApi {
    private String refreshToken;
    
    public BlueskySessionApiImpl(String accessToken, String refreshToken) {
        super(accessToken);
        this.refreshToken = refreshToken;
    }
    
    @Override
    public BlueskySession createSession(String user, String password) {
        String endpoint = this.apiConfig.getProperty("createSession_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("createSession_ok_status"));
        SessionRequest sessionRequest = new SessionRequest(user, password);
        
        try {
            String url = endpoint;
            String requestJson = this.gson.toJson(sessionRequest);
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.POST, requestJson);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Accept", "*/*");
            request.addApiHeader("Accept-Encoding", "gzip, deflate, br");
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response createSession: {}", response.getResponseStr());
            BlueskySession blueskySession = this.gson.fromJson(response.getResponseStr(), BlueskySession.class);
            this.accessToken = blueskySession.getAccessJwt();
            this.refreshToken = blueskySession.getRefreshJwt();
            
            return blueskySession;
        } catch (Exception e) {
            throw new BlueskyException("Error creando una sesión en Bluesky para el usuario: " + user, e);
        }
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
            log.debug("Response getSession: {}", response.getResponseStr());
            
            return this.gson.fromJson(response.getResponseStr(), BlueskySession.class);
        } catch (Exception e) {
            throw new BlueskyException("Error recuperando los datos de una sesión Bluesky", e);
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
            log.debug("Response refreshSession: {}", response.getResponseStr());
            BlueskySession blueskySession = this.gson.fromJson(response.getResponseStr(), BlueskySession.class);
            this.accessToken = blueskySession.getAccessJwt();
            this.refreshToken = blueskySession.getRefreshJwt();
            
            return blueskySession;
        } catch (Exception e) {
            throw new BlueskyException("Error refrescando una sesión Bluesky", e);
        }
    }
}