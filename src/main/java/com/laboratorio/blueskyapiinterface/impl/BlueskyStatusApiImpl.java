package com.laboratorio.blueskyapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laboratorio.blueskyapiinterface.BlueskyStatusApi;
import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import com.laboratorio.blueskyapiinterface.model.BlueskyActor;
import com.laboratorio.blueskyapiinterface.model.BlueskyCreateRecord;
import com.laboratorio.blueskyapiinterface.model.BlueskyDeleteRecord;
import com.laboratorio.blueskyapiinterface.model.BlueskyEmbed;
import com.laboratorio.blueskyapiinterface.model.BlueskyFacet;
import com.laboratorio.blueskyapiinterface.model.BlueskyPostList;
import com.laboratorio.blueskyapiinterface.model.BlueskyRecord;
import com.laboratorio.blueskyapiinterface.model.BlueskyStatus;
import com.laboratorio.blueskyapiinterface.model.BlueskySubject;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyCreateRecordResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyFollowListResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyUploadImageResponse;
import com.laboratorio.blueskyapiinterface.utils.ElementoPost;
import com.laboratorio.blueskyapiinterface.utils.InstruccionInfo;
import com.laboratorio.blueskyapiinterface.utils.PostUtils;
import com.laboratorio.blueskyapiinterface.utils.TipoElementoPost;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
 * @created 03/08/2024
 * @updated 17/08/2024
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
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("getStatusById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getStatusById_ok_status"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getStatusById_max_limit"));
        
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url);
            for (int i = 0; (i < ids.length) && (i < maxLimit); i++) {
                target = target.queryParam("uris", ids[i]);
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
            BlueskyPostList postList = gson.fromJson(jsonStr, BlueskyPostList.class);
            return Arrays.asList(postList.getPosts());
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
    public BlueskyCreateRecordResponse postStatus(String userId, String text) {
        return this.postStatus(userId, text, null);
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
        Client client = ClientBuilder.newClient();
        Response response = null;
        String endpoint = this.apiConfig.getProperty("UploadImage_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("UploadImage_ok_status"));
        
        try {
            String url = endpoint;
            WebTarget target = client.target(url);
            
            File imageFile = new File(filePath);
            InputStream fileStream = new FileInputStream(imageFile);
            
            response = target.request()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .header(HttpHeaders.CONTENT_TYPE, mediaType)
                    .post(Entity.entity(fileStream, MediaType.APPLICATION_OCTET_STREAM_TYPE));
            
            String jsonStr = response.readEntity(String.class);
            if (response.getStatus() != okStatus) {
                log.error(String.format("Respuesta del error %d: %s", response.getStatus(), jsonStr));
                String str = "Error ejecutando: " + url + ". Se obtuvo el código de error: " + response.getStatus();
                throw new BlueskyException(BlueskyAccountApiImpl.class.getName(), str);
            }
            
            log.debug("Se ejecutó la query: " + url);
            log.debug("Respuesta recibida: " + jsonStr);
            
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, BlueskyUploadImageResponse.class);
        } catch (JsonSyntaxException | FileNotFoundException e) {
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
    
    private String getThumbnail(List<ElementoPost> elementos) {
        try {
            for (ElementoPost elem : elementos) {
                if (elem.getType() == TipoElementoPost.Link) {
                    if (elem.getContenido().contains("youtube.com")) {
                        Map<String, String> metadata = PostUtils.getYouTubeMetadata(elem.getContenido());
                        String imageUrl = metadata.get("og:image");
                        if (imageUrl != null) {
                            String destination = this.apiConfig.getProperty("temporal_image_path");
                            if (PostUtils.saveImageUrl(imageUrl, destination)) {
                                return destination;
                            }
                        }
                    }
                }
            }
        } catch (BlueskyException e) {
            return null;
        }
        
        return null;
    }
    
    private BlueskyUploadImageResponse cargarimagen(String imagePath) {
        String mediaType = this.apiConfig.getProperty("image_media_type");
        
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
    public BlueskyCreateRecordResponse postStatus(String userId, String text, String imagePath) {
        // Se extraen los distintos elementos del post (Links y Hastags)
        List<ElementoPost> elementos = PostUtils.extraerElementosPost(text);
        List<BlueskyFacet> facets = elementos.stream()
                .map(elem -> new BlueskyFacet(elem))
                .collect(Collectors.toList());
        
        String collection = this.apiConfig.getProperty("post_collection");
        BlueskyRecord<String> record = null;
        // Si no hay imagen, se verifica si hay alguna URL de youtube y se recupera su Thumbnail para agregarlo al post
        if (imagePath == null) {
            String thumbnail = getThumbnail(elementos);
            if (thumbnail != null) {
                // Se sube la imagen a Bluesky
                BlueskyUploadImageResponse uploadImageResponse = this.cargarimagen(thumbnail);
                
                // Si se logró subir la imagen a Bluesky
                if (uploadImageResponse != null) {
                    BlueskyEmbed embed = new BlueskyEmbed(uploadImageResponse.getBlob());
                    record = new BlueskyRecord<>(text, facets, embed);
                }
            }
        } else {    // Si hay imagen se debe cargar a Bluesky
            BlueskyUploadImageResponse uploadImageResponse = this.cargarimagen(imagePath);
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
}