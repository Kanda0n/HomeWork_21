package org.skypro.skyshop.model.basket;

import java.util.List;

public record UserBasket(List<BasketItem> items, int total) {
    public UserBasket(List<BasketItem> items) {
        this(items, items.stream()
                .mapToInt(item -> item.product().getPrice() * item.amount())
                .sum());
    }
}
