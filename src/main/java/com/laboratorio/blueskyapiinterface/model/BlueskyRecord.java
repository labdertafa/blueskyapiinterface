package com.laboratorio.blueskyapiinterface.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @param <T>
 * @created 03/08/2024
 * @updated 10/08/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BlueskyRecord<T> {
    private T subject;
    private String text;
    private String[] langs;
    private String createdAt;
    private List<BlueskyFacet> facets;
    private BlueskyEmbed embed;

    public BlueskyRecord(T subject) {
        this.subject = subject;
        this.text = null;
        this.createdAt = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"));
    }

    public BlueskyRecord(String text, List<BlueskyFacet> facets) {
        this.subject = null;
        this.text = text;
        String[] idiomas = {"es"};
        this.langs = idiomas;
        this.createdAt = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"));
        this.facets = facets;
    }
    
    public BlueskyRecord(String text, List<BlueskyFacet> facets, BlueskyEmbed embed) {
        this.subject = null;
        this.text = text;
        String[] idiomas = {"es"};
        this.langs = idiomas;
        this.createdAt = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"));
        this.facets = facets;
        this.embed = embed;
    }
}