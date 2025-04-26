package org.skypro.skyshop.service;

import org.skypro.skyshop.model.product.*;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> products = new HashMap<>();
    private final Map<UUID, Article> articles = new HashMap<>();

    public StorageService() {
        initializeTestData();
    }

    private void initializeTestData() {
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(), "Гречка", 80));
        products.put(UUID.randomUUID(), new DiscountedProduct(UUID.randomUUID(), "Кола", 100, 20));

        articles.put(UUID.randomUUID(), new Article(UUID.randomUUID(),
                "Как готовить гречку", "Гречку нужно варить 20 минут."));
        articles.put(UUID.randomUUID(), new Article(UUID.randomUUID(),
                "История Кока-Колы", "Кока-Кола была изобретена в 1886 году."));
    }

    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(products.values());
    }

    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articles.values());
    }

    public Collection<Searchable> getAllSearchables() {
        List<Searchable> result = new ArrayList<>();
        result.addAll(products.values());
        result.addAll(articles.values());
        return result;
    }
}
