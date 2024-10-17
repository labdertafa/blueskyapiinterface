package com.laboratorio.blueskyapiinterface;

import com.laboratorio.blueskyapiinterface.model.BlueskyActor;
import com.laboratorio.blueskyapiinterface.model.BlueskyStatus;
import com.laboratorio.blueskyapiinterface.model.BlueskySubject;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyCreateRecordResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyUploadImageResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 03/08/2024
 * @updated 17/10/2024
 */
public interface BlueskyStatusApi {
    // Consultar un status por su id
    BlueskyStatus getStatusById(String id);
    List<BlueskyStatus> getStatusByIds(String[] ids);
    
    // Postear o eliminar un status.
    BlueskyStatus postStatus(String userId, String text);
    boolean deleteStatus(String userId, String uri);
    
    // Postear un status con imagen
    BlueskyUploadImageResponse uploadImage(String filePath, String mediaType) throws Exception;
    BlueskyStatus postStatus(String userId, String text, String imagePath, String mediaType);
    
    // Ver las cuentas que han impulsado o marcado como favorito un status
    List<BlueskyActor> getRebloggedBy(String uri) throws Exception;
    List<BlueskyActor> getRebloggedBy(String uri, int limit) throws Exception;
    List<BlueskyActor> getRebloggedBy(String uri, int limit, int quantity) throws Exception;
    List<BlueskyActor> getFavouritedBy(String uri) throws Exception;
    List<BlueskyActor> getFavouritedBy(String uri, int limit) throws Exception;
    List<BlueskyActor> getFavouritedBy(String uri, int limit, int quantity) throws Exception;
    
    // Impulsar o dejar de impulsar un status del usuario "userId"
    BlueskyCreateRecordResponse reblogStatus(String userId, BlueskySubject subject);
    boolean unreblogStatus(String userId, String uri);
    
    // Marcar y desmarcar un status  del usuario "userId" como favorito
    BlueskyCreateRecordResponse favouriteStatus(String userId, BlueskySubject subject);
    boolean unfavouriteStatus(String userId, String uri);
    
    // Permite recuperar el timeline global
    List<BlueskyStatus> getGlobalTimeline(int quantity);
}