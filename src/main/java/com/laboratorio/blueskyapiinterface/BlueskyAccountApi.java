package com.laboratorio.blueskyapiinterface;

import com.laboratorio.blueskyapiinterface.model.BlueskyAccount;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyFollowListResponse;
import com.laboratorio.blueskyapiinterface.model.response.BlueskyRelationshipsResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 02/08/2024
 * @updated 17/08/2024
 */
public interface BlueskyAccountApi {
    // Obtiene la información de un usuario a partir de su ID
    BlueskyAccount getAccountById(String id);
    List<BlueskyAccount> getAccountsById(List<String> ids);
    // Obtiene la información de un usuario a partir de su username
    BlueskyAccount getAccountByUsername(String username);
    // Obtiene los seguidores de un usuario a partir de su id. Quantity indica el número de registros a recuperar (cero significa todos). Se puede indicar una posición inicial.
    BlueskyFollowListResponse getFollowers(String id) throws Exception;
    BlueskyFollowListResponse getFollowers(String id, int limit) throws Exception;
    BlueskyFollowListResponse getFollowers(String id, int limit, int quantity) throws Exception;
    BlueskyFollowListResponse getFollowers(String id, int limit, int quantity, String posicionInicial) throws Exception;
    // Obtiene los seguidos de un usuario a partir de su id. Quantity indica el número de registros a recuperar (cero significa todos). Se puede indicar una posición inicial.
    BlueskyFollowListResponse getFollowings(String id) throws Exception;
    BlueskyFollowListResponse getFollowings(String id, int limit) throws Exception;
    BlueskyFollowListResponse getFollowings(String id, int limit, int quantity) throws Exception;
    BlueskyFollowListResponse getFollowings(String id, int limit, int quantity, String posicionInicial) throws Exception;
    // El usuario "userID" sigue al usuario identificado por "id"
    boolean followAccount(String userId, String id);
    // El usuario "userID" deja de seguir al usuario que siguió con el Record de la "uri"
    boolean unfollowAccount(String userId, String uri);
    // Chequea la relación entre el usuario identificado por "userId" y un listado de cuentas identificadas por su id
    BlueskyRelationshipsResponse checkrelationships(String userId, List<String> ids);
}