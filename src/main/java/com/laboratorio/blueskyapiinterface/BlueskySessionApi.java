package com.laboratorio.blueskyapiinterface;

import com.laboratorio.blueskyapiinterface.model.BlueskySession;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 01/08/2024
 * @updated 01/08/2024
 */
public interface BlueskySessionApi {
    BlueskySession getSession();
    BlueskySession refreshSession();
}