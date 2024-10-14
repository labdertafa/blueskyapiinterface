package com.laboratorio.blueskyapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.blueskyapiinterface.BlueskyStatusApi;
import com.laboratorio.blueskyapiinterface.model.BlueskyActor;
import com.laboratorio.blueskyapiinterface.model.BlueskyCreateRecord;
import com.laboratorio.blueskyapiinterface.model.BlueskyDeleteRecord;
import com.laboratorio.blueskyapiinterface.model.BlueskyEmbed;
import com.laboratorio.blueskyapiinterface.model.BlueskyFacet;
import com.laboratorio.blueskyapiinterface.model.BlueskyFeed;
import com.laboratorio.blueskyapiinterface.model.BlueskyPostList;
import com.laboratorio.blueskyapiinterface.model.BlueskyRecord;
import com.laboratorio.blueskyapiinterface.model.BlueskyStatus;
import com.laboratorio.blueskyapiinterface.model.BlueskySubject;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyCreateRecordResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyFollowListResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyTimelineResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyUploadImageResponse;
import com.laboratorio.blueskyapiinterface.utils.BlueskyApiConfig;
import com.laboratorio.blueskyapiinterface.utils.InstruccionInfo;
import com.laboratorio.clientapilibrary.exceptions.ApiClientException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.clientapilibrary.utils.ElementoPost;
import com.laboratorio.clientapilibrary.utils.PostUtils;
import com.laboratorio.clientapilibrary.utils.TipoElementoPost;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rafael
 * @version 1.3
 * @created 03/08/2024
 * @updated 14/10/2024
 */
public class BlueskyStatusApiImpl extends BlueskyBaseApi implements BlueskyStatusApi {
    public BlueskyStatusApiImpl(String accessToken) {
        super(accessToken);
    }

    @Override
    public BlueskyStatus getStatusById(String id) {
        String[] ids = {id};
        
        List<BlueskyStatus> statuses = this.getStatusByIds(ids);
        if (statuses.isEmpty()) {
            return null;
        }
        
        return statuses.get(0);
    }
    
    @Override
    public List<BlueskyStatus> getStatusByIds(String[] ids) {
        String endpoint = this.apiConfig.getProperty("getStatusById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getStatusById_ok_status"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getStatusById_max_limit"));
        
        try {
            String url = endpoint;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.GET);
            for (int i = 0; (i < ids.length) && (i < maxLimit); i++) {
                request.addApiPathParam("uris", ids[i]);
            }
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            BlueskyPostList postList = gson.fromJson(response.getResponseStr(), BlueskyPostList.class);
            return Arrays.asList(postList.getPosts());
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (ApiClientException e) {
            throw e;
        }
    }

    @Override
    public BlueskyCreateRecordResponse postStatus(String userId, String text) {
        return this.postStatus(userId, text, null, null);
    }

    @Override
    public boolean deleteStatus(String userId, String uri) {
        String collection = this.apiConfig.getProperty("post_collection");
        String rkey = this.extractRKey(uri);
        BlueskyDeleteRecord deleteRecord = new BlueskyDeleteRecord(userId, collection, rkey);

        return this.deleteRecord(deleteRecord);
    }
    
    @Override
    public BlueskyUploadImageResponse uploadImage(String filePath, String mediaType) throws Exception {
        String endpoint = this.apiConfig.getProperty("UploadImage_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("UploadImage_ok_status"));
        
        try {
            String url = endpoint;
            File imageFile = new File(filePath);
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.POST, imageFile);
            request.addApiHeader("Content-Type", mediaType);
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), BlueskyUploadImageResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (ApiClientException e) {
            throw  e;
        }
    }
    
    private BlueskyUploadImageResponse cargarimagen(String imagePath, String mediaType) {
        try {
            return this.uploadImage(imagePath, mediaType);
        } catch (Exception e) {
            log.error("Error subiendo imagen a Bluesky: " + e.getMessage());
            if (e.getCause() != null) {
                log.error("Causa del error: " + e.getCause().getMessage());
            }
            
            return null;
        }
    }
    
    @Override
    public BlueskyCreateRecordResponse postStatus(String userId, String text, String imagePath, String mediaType) {
        // Se extraen los distintos elementos del post (Links y Hastags)
        List<ElementoPost> elementos = PostUtils.extraerElementosPost(text);
        List<BlueskyFacet> facets = elementos.stream()
                .filter(elem -> elem.getType() != TipoElementoPost.Normal)
                .map(elem -> new BlueskyFacet(elem))
                .collect(Collectors.toList());
        
        String collection = this.apiConfig.getProperty("post_collection");
        BlueskyRecord<String> record = null;
        // Si no hay imagen, se verifica si hay alguna URL de youtube y se recupera su Thumbnail para agregarlo al post
        if (imagePath == null) {
            String destination = BlueskyApiConfig.getInstance().getProperty("temporal_image_path");
            String thumbnail = PostUtils.getThumbnail(elementos, destination);
            if (thumbnail != null) {
                // Se sube la imagen a Bluesky
                BlueskyUploadImageResponse uploadImageResponse = this.cargarimagen(thumbnail, mediaType);
                
                // Si se logró subir la imagen a Bluesky
                if (uploadImageResponse != null) {
                    BlueskyEmbed embed = new BlueskyEmbed(uploadImageResponse.getBlob());
                    record = new BlueskyRecord<>(text, facets, embed);
                }
            }
        } else {    // Si hay imagen se debe cargar a Bluesky
            BlueskyUploadImageResponse uploadImageResponse = this.cargarimagen(imagePath, mediaType);
            if (uploadImageResponse != null) {
                BlueskyEmbed embed = new BlueskyEmbed(uploadImageResponse.getBlob());
                record = new BlueskyRecord<>(text, facets, embed);
            }
        }
        
        if (record == null) {
            record = new BlueskyRecord(text, facets);
        }
        BlueskyCreateRecord<String> createRecord = new BlueskyCreateRecord(userId, collection, record);
        return this.createRecord(createRecord);
    }

    @Override
    public List<BlueskyActor> getRebloggedBy(String uri) throws Exception {
        return this.getRebloggedBy(uri, 0);
    }
    
    @Override
    public List<BlueskyActor> getRebloggedBy(String uri, int limit) throws Exception {
        return this.getRebloggedBy(uri, limit, 0);
    }

    @Override
    public List<BlueskyActor> getRebloggedBy(String uri, int limit, int quantity) throws Exception {
        String endpoint = this.apiConfig.getProperty("getRebloggedBy_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getRebloggedBy_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getRebloggedBy_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getRebloggedBy_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        InstruccionInfo instruccionInfo = new InstruccionInfo(TypeAccountList.REPOST, endpoint, okStatus, usedLimit);
        BlueskyFollowListResponse response = this.getBlueskyAccountList(instruccionInfo, uri, quantity, null);
        return response.getAccounts();
    }

    @Override
    public List<BlueskyActor> getFavouritedBy(String uri) throws Exception {
        return getFavouritedBy(uri, 0);
    }
    
    @Override
    public List<BlueskyActor> getFavouritedBy(String uri, int limit) throws Exception {
        return getFavouritedBy(uri, limit, 0);
    }

    @Override
    public List<BlueskyActor> getFavouritedBy(String uri, int limit, int quantity) throws Exception {
        String endpoint = this.apiConfig.getProperty("getFavouritedBy_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFavouritedBy_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getFavouritedBy_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getFavouritedBy_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        InstruccionInfo instruccionInfo = new InstruccionInfo(TypeAccountList.LIKE, endpoint, okStatus, usedLimit);
        BlueskyFollowListResponse response = this.getBlueskyAccountList(instruccionInfo, uri, quantity, null);
        return response.getAccounts();
    }

    @Override
    public BlueskyCreateRecordResponse reblogStatus(String userId, BlueskySubject subject) {
        String collection = this.apiConfig.getProperty("repost_collection");
        BlueskyRecord<BlueskySubject> record = new BlueskyRecord(subject);
        BlueskyCreateRecord<BlueskySubject> createRecord = new BlueskyCreateRecord(userId, collection, record);
        return this.createRecord(createRecord);
    }

    @Override
    public boolean unreblogStatus(String userId, String uri) {
        String collection = this.apiConfig.getProperty("repost_collection");
        String rkey = this.extractRKey(uri);
        BlueskyDeleteRecord deleteRecord = new BlueskyDeleteRecord(userId, collection, rkey);

        return this.deleteRecord(deleteRecord);
    }

    @Override
    public BlueskyCreateRecordResponse favouriteStatus(String userId, BlueskySubject subject) {
        String collection = this.apiConfig.getProperty("like_collection");
        BlueskyRecord<BlueskySubject> record = new BlueskyRecord(subject);
        BlueskyCreateRecord<BlueskySubject> createRecord = new BlueskyCreateRecord(userId, collection, record);
        return this.createRecord(createRecord);
    }

    @Override
    public boolean unfavouriteStatus(String userId, String uri) {
        String collection = this.apiConfig.getProperty("like_collection");
        String rkey = this.extractRKey(uri);
        BlueskyDeleteRecord deleteRecord = new BlueskyDeleteRecord(userId, collection, rkey);

        return this.deleteRecord(deleteRecord);
    }
    
    private BlueskyTimelineResponse getTimelinePage(String uri, int okStatus, int limit, String cursor) {
        try {
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            request.addApiPathParam("limit", Integer.toString(limit));
            if (cursor != null) {
                request.addApiPathParam("cursor", cursor);
            }
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), BlueskyTimelineResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (ApiClientException e) {
            throw  e;
        }
    }

    @Override
    public List<BlueskyStatus> getGlobalTimeline(int quantity) {
        String endpoint = this.apiConfig.getProperty("getGlobalTimeLine_endpoint");
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getGlobalTimeLine_default_limit"));
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getGlobalTimeLine_ok_status"));
        
        List<BlueskyStatus> statuses = new ArrayList<>();
        boolean continuar = true;
        String cursor = null;
        
        try {
            String uri = endpoint;
            
            do {
                BlueskyTimelineResponse timelineResponse = this.getTimelinePage(uri, okStatus, defaultLimit, cursor);
                log.debug("Elementos recuperados total: " + timelineResponse.getFeed().size());
                for (BlueskyFeed feed : timelineResponse.getFeed()) {
                    statuses.add(feed.getPost());
                }
                
                cursor = timelineResponse.getCursor();
                log.debug("getGlobalTimeline. Recuperados: " + statuses.size() + ". Cursor: " + cursor);
                if (timelineResponse.getFeed().isEmpty()) {
                    continuar = false;
                } else {
                    if ((cursor == null) || (statuses.size() >= quantity)) {
                        continuar = false;
                    }
                }
            } while (continuar);
            
            return statuses.subList(0, Math.min(quantity, statuses.size()));
        } catch (Exception e) {
            throw e;
        }
    }
}