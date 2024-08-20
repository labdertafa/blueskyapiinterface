package com.laboratorio.utils;

import com.laboratorio.blueskyapiinterface.utils.BlueskyApiConfig;
import com.laboratorio.blueskyapiinterface.utils.ElementoPost;
import com.laboratorio.blueskyapiinterface.utils.PostUtils;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/08/2024
 * @updated 17/08/2024
 */
public class PostUtilsTest {
    @Test
    public void extraerElementosPost() {
        String text = "Estado de prueba enviado desde la aplicación SocialImprove con un test unitario JUNIT. Tiene un link: https://laboratoriorafa.mooo.com y 2 hashtags: #SiguemeYTeSigo #Followback";
        
        List<ElementoPost> elementosPost = PostUtils.extraerElementosPost(text);
        
        assertEquals(3, elementosPost.size());
        assertEquals("https://laboratoriorafa.mooo.com", elementosPost.get(0).getContenido());
        assertEquals("SiguemeYTeSigo", elementosPost.get(1).getContenido());
    }
    
    @Test
    public void getYouTubeMetadata() {
        String url = "https://www.youtube.com/watch?v=zkSz6gkY2hk&list=PLtdeXn2f7ZbPXR2R0JC0Qc8OURym5-Zze";
        
        Map<String, String> metadata = PostUtils.getYouTubeMetadata(url);
        
        String image = metadata.get("og:image");
        assertTrue(image != null);
    }

    @Test
    public void saveImageUrl() {
        String url = "https://www.youtube.com/watch?v=zkSz6gkY2hk&list=PLtdeXn2f7ZbPXR2R0JC0Qc8OURym5-Zze";
        
        Map<String, String> metadata = PostUtils.getYouTubeMetadata(url);
        String image = metadata.get("og:image");
        assertTrue(image != null);
        if (image == null) {
            return;
        }
        
        String destination = BlueskyApiConfig.getInstance().getProperty("temporal_image_path");
        
        boolean result = PostUtils.saveImageUrl(image, destination);
        
        assertTrue(result);
    }
}