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
import com.laboratorio.blueskyapiinterface.model.response.BlueskyUserListResponse;
import com.laboratorio.blueskyapiinterface.utils.BlueskyApiConfig;
import com.laboratorio.blueskyapiinterface.utils.InstruccionInfo;
import com.laboratorio.clientapilibrary.ApiClient;
import com.laboratorio.clientapilibrary.exceptions.ApiClientException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 01/08/2024
 * @updated 22/10/2024
 */
public class BlueskyBaseApi {
    protected static final Logger log = LogManager.getLogger(BlueskyBaseApi.class);
    protected final ApiClient client;
    protected final String accessToken;
    protected BlueskyApiConfig apiConfig;
    protected final Gson gson;

    public BlueskyBaseApi(String accessToken) {
        this.client = new ApiClient();
        this.accessToken = accessToken;
        this.apiConfig = BlueskyApiConfig.getInstance();
        this.gson = new Gson();
    }
    
    protected void logException(Exception e) {
        log.error("Error: " + e.getMessage());
        if (e.getCause() != null) {
            log.error("Causa: " + e.getCause().getMessage());
        }
    }
    
    // Función que devuelve una página de seguidores o seguidos de una cuenta
    private String getAccountPage(int okStatus, String endpoint, String nombreParam, String id, int limit, String posicionInicial) throws Exception {
        try {
            String url = endpoint;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.GET);
            request.addApiPathParam(nombreParam, id);
            request.addApiPathParam("limit", Integer.toString(limit));
            if (posicionInicial != null) {
                request.addApiPathParam("cursor", posicionInicial);
            }
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return response.getResponseStr();
        } catch (BlueskyException e) {
            throw e;
        }
    }
    
    private BlueskyUserListResponse getFollowAccountPage(int okStatus, String endpoint, String nombreParam, String id, int limit, String posicionInicial) throws Exception {
        try {
            String jsonStr = this.getAccountPage(okStatus, endpoint, nombreParam, id, limit, posicionInicial);
            
            BlueskyAccountPageResponse accountPageResponse = this.gson.fromJson(jsonStr, BlueskyAccountPageResponse.class); 
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
            return new BlueskyUserListResponse(maxId, accounts);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (BlueskyException e) {
            throw e;
        }
    }
    
    private BlueskyUserListResponse getRepostAccountPage(int okStatus, String endpoint, String nombreParam, String id, int limit, String posicionInicial) throws Exception {
        try {
            String jsonStr = this.getAccountPage(okStatus, endpoint, nombreParam, id, limit, posicionInicial);
            
            BlueskyRepostPageResponse response = this.gson.fromJson(jsonStr, BlueskyRepostPageResponse.class); 
            String maxId = response.getCursor();
            log.debug("Valor del max_id: " + maxId);
            log.debug("Resultados encontrados: " + response.getRepostedBy().length);
            List<BlueskyActor> accounts = Arrays.stream(response.getRepostedBy())
                    .collect(Collectors.toList());
            
            // return accounts;
            return new BlueskyUserListResponse(maxId, accounts);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (BlueskyException e) {
            throw e;
        }
    }
    
    private BlueskyUserListResponse getLikeAccountPage(int okStatus, String endpoint, String nombreParam, String id, int limit, String posicionInicial) throws Exception {
        try {
            String jsonStr = this.getAccountPage(okStatus, endpoint, nombreParam, id, limit, posicionInicial);
            
            BlueskyLikePageResponse response = this.gson.fromJson(jsonStr, BlueskyLikePageResponse.class); 
            String maxId = response.getCursor();
            log.debug("Valor del max_id: " + maxId);
            log.debug("Resultados encontrados: " + response.getLikes().length);
            List<BlueskyActor> accounts = Arrays.stream(response.getLikes())
                    .map(like -> like.getActor())
                    .collect(Collectors.toList());
            
            // return accounts;
            return new BlueskyUserListResponse(maxId, accounts);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (BlueskyException e) {
            throw e;
        }
    }
    
    protected BlueskyUserListResponse getBlueskyAccountList(InstruccionInfo instruccionInfo, String id, int quantity, String posicionInicial) throws Exception {
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
                BlueskyUserListResponse accountListResponse;
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
                return new BlueskyUserListResponse(max_id, accounts);
            }
            
            return new BlueskyUserListResponse(max_id, accounts.subList(0, Math.min(quantity, accounts.size())));
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
        String endpoint = this.apiConfig.getProperty("createRecord_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("createRecord_ok_status"));
        
        try {
            String requestJson = gson.toJson(record);
            log.debug("Record a enviar: " + requestJson);
            
            String url = endpoint;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.POST, requestJson);
            request.addApiHeader("Accept", "*/*");
            request.addApiHeader("Accept-Encoding", "gzip, deflate, br");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), BlueskyCreateRecordResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (ApiClientException e) {
            throw  e;
        }
    }
    
    public boolean deleteRecord(BlueskyDeleteRecord record) {
        String endpoint = this.apiConfig.getProperty("deleteRecord_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("deleteRecord_ok_status"));
        
        try {            
            String requestJson = gson.toJson(record);
            log.debug("deleteRecord request: " + requestJson);
        
            String url = endpoint;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.POST, requestJson);
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            this.client.executeApiRequest(request);
            
            return true;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (ApiClientException e) {
            throw  e;
        }
    }
}