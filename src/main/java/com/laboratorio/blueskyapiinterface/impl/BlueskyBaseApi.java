package com.laboratorio.blueskyapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.model.BlueskyActor;
import com.laboratorio.blueskyapiinterface.model.BlueskyCreateRecord;
import com.laboratorio.blueskyapiinterface.model.BlueskyDeleteRecord;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyAccountPageResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyCreateRecordResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyFollowListResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyLikePageResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyRepostPageResponse;
import com.laboratorio.blueskyapiinterface.utils.BlueskyApiConfig;
import com.laboratorio.blueskyapiinterface.utils.InstruccionInfo;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 01/08/2024
 * @updated 09/09/2024
 */
public class BlueskyBaseApi {
    protected static final Logger log = LogManager.getLogger(BlueskyBaseApi.class);
    protected final String accessToken;
    protected BlueskyApiConfig apiConfig;

    public BlueskyBaseApi(String accessToken) {
        this.accessToken = accessToken;
        this.apiConfig = BlueskyApiConfig.getInstance();
    }
    
    protected void logException(Exception e) {
        log.error("Error: " + e.getMessage());
        if (e.getCause() != null) {
            log.error("Causa: " + e.getCause().getMessage());
        }
    }
    
    // Función que devuelve una página de seguidores o seguidos de una cuenta
    private String getAccountPage(int okStatus, String endpoint, String nombreParam, String id, int limit, String posicionInicial) throws Exception {
        Client client = ClientBuilder.newClient();
        Response response = null;
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url)
                        .queryParam(nombreParam, id)
                        .queryParam("limit", limit);
            if (posicionInicial != null) {
                target = target.queryParam("cursor", posicionInicial);
            }
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .get();
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskyBaseApi.class.getName(), str);
            }
            
            log.debug("Respuesta JSON recibida: " + jsonStr);
            log.debug("Se ejecutó la query: " + url);

            return jsonStr;
        } catch (BlueskyException e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }
    
    private BlueskyFollowListResponse getFollowAccountPage(int okStatus, String endpoint, String nombreParam, String id, int limit, String posicionInicial) throws Exception {
        try {
            String jsonStr = this.getAccountPage(okStatus, endpoint, nombreParam, id, limit, posicionInicial);
            
            Gson gson = new Gson();
            BlueskyAccountPageResponse accountPageResponse = gson.fromJson(jsonStr, BlueskyAccountPageResponse.class); 
            String maxId = accountPageResponse.getCursor();
            log.debug("Valor del max_id: " + maxId);
            List<BlueskyActor> accounts;
            if (accountPageResponse.getFollowers() != null ) {
                log.debug("Resultados encontrados: " + accountPageResponse.getFollowers().length);
                accounts = Arrays.stream(accountPageResponse.getFollowers())
                        .collect(Collectors.toList());
            } else {
                if (accountPageResponse.getFollows() != null ) {
                    log.debug("Resultados encontrados: " + accountPageResponse.getFollows().length);
                    accounts = Arrays.stream(accountPageResponse.getFollows())
                            .collect(Collectors.toList());
                } else {
                    String str = "Error ejecutando getAccountPage: se recibió una respuesta inesperada (sin followers o follows)";
                    throw new BlueskyException(BlueskyBaseApi.class.getName(), str);
                }
            }

            // return accounts;
            return new BlueskyFollowListResponse(maxId, accounts);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (BlueskyException e) {
            throw e;
        }
    }
    
    private BlueskyFollowListResponse getRepostAccountPage(int okStatus, String endpoint, String nombreParam, String id, int limit, String posicionInicial) throws Exception {
        try {
            String jsonStr = this.getAccountPage(okStatus, endpoint, nombreParam, id, limit, posicionInicial);
            
            Gson gson = new Gson();
            BlueskyRepostPageResponse response = gson.fromJson(jsonStr, BlueskyRepostPageResponse.class); 
            String maxId = response.getCursor();
            log.debug("Valor del max_id: " + maxId);
            log.debug("Resultados encontrados: " + response.getRepostedBy().length);
            List<BlueskyActor> accounts = Arrays.stream(response.getRepostedBy())
                    .collect(Collectors.toList());
            
            // return accounts;
            return new BlueskyFollowListResponse(maxId, accounts);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (BlueskyException e) {
            throw e;
        }
    }
    
    private BlueskyFollowListResponse getLikeAccountPage(int okStatus, String endpoint, String nombreParam, String id, int limit, String posicionInicial) throws Exception {
        try {
            String jsonStr = this.getAccountPage(okStatus, endpoint, nombreParam, id, limit, posicionInicial);
            
            Gson gson = new Gson();
            BlueskyLikePageResponse response = gson.fromJson(jsonStr, BlueskyLikePageResponse.class); 
            String maxId = response.getCursor();
            log.debug("Valor del max_id: " + maxId);
            log.debug("Resultados encontrados: " + response.getLikes().length);
            List<BlueskyActor> accounts = Arrays.stream(response.getLikes())
                    .map(like -> like.getActor())
                    .collect(Collectors.toList());
            
            // return accounts;
            return new BlueskyFollowListResponse(maxId, accounts);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (BlueskyException e) {
            throw e;
        }
    }
    
    protected BlueskyFollowListResponse getBlueskyAccountList(InstruccionInfo instruccionInfo, String id, int quantity, String posicionInicial) throws Exception {
        List<BlueskyActor> accounts = null;
        boolean continuar = true;
        String endpoint = instruccionInfo.getEndpoint();
        int limit = instruccionInfo.getLimit();
        String max_id = posicionInicial;
        
        if (quantity > 0) {
            limit = Math.min(limit, quantity);
        }
        
        try {
            do {
                BlueskyFollowListResponse accountListResponse;
                switch (instruccionInfo.getTypeAccountList()) {
                    case FOLLOW -> accountListResponse = this.getFollowAccountPage(instruccionInfo.getOkStatus(), endpoint, "actor", id, limit, max_id);
                    case REPOST -> accountListResponse = this.getRepostAccountPage(instruccionInfo.getOkStatus(), endpoint, "uri", id, limit, max_id);
                    case LIKE -> accountListResponse = this.getLikeAccountPage(instruccionInfo.getOkStatus(), endpoint, "uri", id, limit, max_id);
                    default -> {
                        String message = "Se recibió una solicitud para obtener un listado de cuentas de un tipo inesperado";
                        log.error(message);
                        throw new BlueskyException(BlueskyBaseApi.class.getName(), message);
                    }

                }

                if (accounts == null) {
                    accounts = accountListResponse.getAccounts();
                } else {
                    accounts.addAll(accountListResponse.getAccounts());
                }
                
                max_id = accountListResponse.getCursor();
                log.debug("getBlueskyAccountList. Cantidad: " + quantity + ". Recuperados: " + accounts.size() + ". Max_id: " + max_id);
                if (quantity > 0) {
                    if ((accounts.size() >= quantity) || (max_id == null)) {
                        continuar = false;
                    }
                } else {
                    if (max_id == null) {
                        continuar = false;
                    }
                }
            } while (continuar);

            if (quantity == 0) {
                return new BlueskyFollowListResponse(max_id, accounts);
            }
            
            return new BlueskyFollowListResponse(max_id, accounts.subList(0, Math.min(quantity, accounts.size())));
        } catch (Exception e) {
            throw e;
        }
    }
    
    public String extractRKey(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        // Divide la cadena por "/"
        String[] parts = input.split("/");

        // Retorna el último elemento del arreglo, que es el resultado deseado
        return parts[parts.length - 1];
    }
    
    public BlueskyCreateRecordResponse createRecord(BlueskyCreateRecord record) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("createRecord_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("createRecord_ok_status"));
        
        try {
            Gson gson = new Gson();
            String recordJson = gson.toJson(record);
            log.info("Record a enviar: " + recordJson);
            
            
            String url = endpoint;
            WebTarget target = client.target(url);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .post(Entity.entity(recordJson, MediaType.APPLICATION_JSON));
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskyAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            return gson.fromJson(jsonStr, BlueskyCreateRecordResponse.class);
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
    
    public boolean deleteRecord(BlueskyDeleteRecord record) {
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("deleteRecord_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("deleteRecord_ok_status"));
        
        try {            
            Gson gson = new Gson();
            String requestJson = gson.toJson(record);
            log.debug("deleteRecord request: " + requestJson);
        
            String url = endpoint;
            WebTarget target = client.target(url);
            
            response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .post(Entity.entity(requestJson, MediaType.APPLICATION_JSON));
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskyAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta JSON recibida: " + jsonStr);
            
            return true;
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