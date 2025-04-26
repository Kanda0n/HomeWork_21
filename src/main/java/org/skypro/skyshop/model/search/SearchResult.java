package org.skypro.skyshop.model.search;

import java.util.UUID;

public record SearchResult(UUID id, String name, String contentType) {
    public static SearchResult fromSearchable(Searchable searchable) {
        return new SearchResult(
                searchable.getId(),
                searchable.getSearchTerm(),
                searchable.getContentType()
        );
    }
}
