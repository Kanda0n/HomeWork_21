package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.product.*;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> products = new HashMap<>();
    private final Map<UUID, Article> articles = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(StorageService.class);

    public StorageService() {
        initializeTestData();
    }

    private void initializeTestData() {
        products.computeIfAbsent(UUID.randomUUID(), id -> new SimpleProduct(id, "Гречка", 80));
        products.computeIfAbsent(UUID.randomUUID(), id -> new DiscountedProduct(id, "Кола", 100, 20));

        articles.computeIfAbsent(UUID.randomUUID(), id -> new Article(id,
                "Как готовить гречку", "Гречку нужно варить 20 минут."));
        articles.computeIfAbsent(UUID.randomUUID(), id -> new Article(id,
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

    public Optional<Product> getProductById(UUID id) {
        Product product = products.get(id);
        if (product == null) {
            throw new NoSuchProductException("Продукта с ID " + id
                    + " не найдено.");
        }
        return Optional.of(product);
    }
}
