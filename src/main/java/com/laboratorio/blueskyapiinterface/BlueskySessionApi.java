package com.laboratorio.blueskyapiinterface;

import com.laboratorio.blueskyapiinterface.model.BlueskySession;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 01/08/2024
 * @updated 23/02/2025
 */
public interface BlueskySessionApi {
    BlueskySession createSession(String user, String password);
    BlueskySession getSession();
    BlueskySession refreshSession();
}