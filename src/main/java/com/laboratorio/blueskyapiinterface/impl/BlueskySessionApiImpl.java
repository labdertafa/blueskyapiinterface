package com.laboratorio.blueskyapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laboratorio.blueskyapiinterface.BlueskySessionApi;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.model.BlueskySession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 01/08/2024
 * @updated 17/08/2024
 */
public class BlueskySessionApiImpl extends BlueskyBaseApi implements BlueskySessionApi {
    private final String refreshToken;
    
    public BlueskySessionApiImpl(String accessToken, String refreshToken) {
        super(accessToken);
        this.refreshToken = refreshToken;
    }

    @Override
    public BlueskySession getSession() {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("getSession_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getSession_ok_status"));
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskySessionApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, BlueskySession.class);
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
    public BlueskySession refreshSession() {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("refreshSession_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("refreshSession_ok_status"));
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.refreshToken)
                    .post(Entity.text(""));
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskySessionApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, BlueskySession.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (BlueskyException e) {
            throw  e;
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }   
}