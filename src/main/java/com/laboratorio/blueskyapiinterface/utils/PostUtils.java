package com.laboratorio.blueskyapiinterface.utils;

import com.laboratorio.blueskyapiinterface.exception.BlueskyException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/08/2024
 * @updated 27/08/2024
 */
public class PostUtils {
    private PostUtils() {
    }
    
    public static List<ElementoPost> extraerElementosPost(String texto) {
        List<ElementoPost> resultados = new ArrayList<>();
        if (texto == null) {
            return resultados;
        }

        // Expresión regular para URLs y hashtags
        Pattern pattern = Pattern.compile("(https?://\\S+)|(#\\w+)");
        Matcher matcher = pattern.matcher(texto);

        // Buscar elmentos en el texto
        while (matcher.find()) {
            String found = matcher.group();
            // Se busca la posición en bytes del elementos
            int start = texto.substring(0, matcher.start()).getBytes(StandardCharsets.UTF_8).length;
            int end = texto.substring(0, matcher.end()).getBytes(StandardCharsets.UTF_8).length;
            TipoElementoPost type = TipoElementoPost.Link;
            if (found.startsWith("#")) {
                type = TipoElementoPost.Tag;
                found = matcher.group().substring(1);
            }
            
            resultados.add(new ElementoPost(start, end, type, found));
        }

        return resultados;
    }
    
    // Obtiene los metadatos de una URL de Youtube
    public static Map<String, String> getYouTubeMetadata(String url) throws BlueskyException {
        // Crear una instancia de HttpClient
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Convertir la respuesta en un String
                String html = EntityUtils.toString(response.getEntity());

                // Parsear el HTML usando JSoup
                Document doc = Jsoup.parse(html);

                // Crear un mapa para almacenar los metadatos
                Map<String, String> metadata = new HashMap<>();

                // Extraer metadatos Open Graph
                for (Element metaTag : doc.select("meta[property^=og:]")) {
                    String property = metaTag.attr("property");
                    String content = metaTag.attr("content");
                    metadata.put(property, content);
                }

                // Devolver los metadatos
                return metadata;
            }
        } catch (IOException | ParseException e) {
            throw new BlueskyException(PostUtils.class.getName(), "Error extrayendo la metadata de la URL " + url, e);
        }
    }
    
    // Graba una imagen a partir de una URL
    public static boolean saveImageUrl(String imageUrl, String destinationFile) throws BlueskyException {
        // Crear una instancia de HttpClient
        try {
            // Si existe el fichero se borra
            File fichero = new File(destinationFile);
            if (fichero.exists()) {
                fichero.delete();
            }
            
            CloseableHttpClient httpClient = HttpClients.createDefault();
                    
            HttpGet request = new HttpGet(imageUrl);

            try {
                CloseableHttpResponse response = httpClient.execute(request);
                // Obtener el InputStream de la imagen
                InputStream imageStream = response.getEntity().getContent();

                // Guardar la imagen en el sistema de archivos
                Path destinationPath = Paths.get(destinationFile);
                Files.copy(imageStream, destinationPath);
            } catch (IOException | UnsupportedOperationException e) {
                throw new BlueskyException(PostUtils.class.getName(), "Error almacenando la imagen de la URL " + imageUrl, e);
            }
        } catch (BlueskyException e) {
            throw  e;
        }
        
            return true;
    }
    
    public static String getThumbnail(List<ElementoPost> elementos) {
        try {
            for (ElementoPost elem : elementos) {
                if (elem.getType() == TipoElementoPost.Link) {
                    if ((elem.getContenido().contains("youtube.com")) || (elem.getContenido().contains("https://youtu.be"))) {
                        Map<String, String> metadata = PostUtils.getYouTubeMetadata(elem.getContenido());
                        String imageUrl = metadata.get("og:image");
                        if (imageUrl != null) {
                            String destination = BlueskyApiConfig.getInstance().getProperty("temporal_image_path");
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
}