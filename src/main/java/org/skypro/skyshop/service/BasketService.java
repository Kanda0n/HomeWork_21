package org.skypro.skyshop.service;

import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final ProductBasket basket;
    private final StorageService storage;
    private static final Logger log = LoggerFactory.getLogger(BasketService.class);

    public BasketService(ProductBasket basket, StorageService storage) {
        this.basket = basket;
        this.storage = storage;
    }

    public void addProduct(UUID id) {
        Product product = storage.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
        basket.addProduct(id);
    }


    public UserBasket getUserBasket() {
        Map<UUID, Integer> basketItems = basket.getProducts();
        List<BasketItem> items = basketItems.entrySet().stream()
                .map(entry -> new BasketItem(
                        storage.getProductById(entry.getKey()).orElseThrow(),
                        entry.getValue()
                ))
                .collect(Collectors.toList());

        int total = items.stream()
                .mapToInt(item -> item.product().getPrice() * item.amount())
                .sum();

        return new UserBasket(items, total);
    }
}
