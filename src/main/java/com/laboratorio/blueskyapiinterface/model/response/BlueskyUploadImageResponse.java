package com.laboratorio.blueskyapiinterface.model.response;

import com.laboratorio.blueskyapiinterface.model.BlueskyImageBlob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/08/2024
 * @updated 10/08/2024
 */
@Getter @Setter @AllArgsConstructor
public class BlueskyUploadImageResponse {
    private BlueskyImageBlob blob;
}