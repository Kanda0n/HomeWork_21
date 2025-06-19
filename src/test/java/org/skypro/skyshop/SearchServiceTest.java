package org.skypro.skyshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    private final Product testProduct = new SimpleProduct(UUID.randomUUID(), "Гречка", 80);
    private final Article testArticle = new Article(UUID.randomUUID(), "Как варить гречку", "Инструкция");

    @Test
    void search_shouldReturnEmptyList_whenNoObjectsInStorage() {
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        Collection<SearchResult> result = searchService.search("гречк");

        assertTrue(result.isEmpty());
    }

    @Test
    void search_shouldReturnEmptyList_whenNoMatches() {
        when(storageService.getAllSearchables()).thenReturn(List.of(testProduct, testArticle));

        Collection<SearchResult> result = searchService.search("несуществующий запрос");

        assertTrue(result.isEmpty());
    }

    @Test
    void search_shouldReturnProduct_whenMatchFound() {
        when(storageService.getAllSearchables()).thenReturn(List.of(testProduct, testArticle));

        Collection<SearchResult> result = searchService.search("гречк");

        assertEquals(1, result.size());
        assertTrue(result.contains(testProduct));
    }

    @Test
    void search_shouldBeCaseInsensitive() {
        when(storageService.getAllSearchables()).thenReturn(List.of(testProduct));

        Collection<SearchResult> result = searchService.search("ГРЕЧК");

        assertEquals(1, result.size());
    }
}
