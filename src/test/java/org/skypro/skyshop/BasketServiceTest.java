package org.skypro.skyshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.StorageService;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {

    @Mock
    private ProductBasket basket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    private final UUID existingProductId = UUID.randomUUID();
    private final UUID nonExistingProductId = UUID.randomUUID();
    private final Product testProduct = new SimpleProduct(existingProductId, "Кола", 100);

    @Test
    void addProduct_shouldThrow_whenProductNotFound() {
        when(storageService.getProductById(nonExistingProductId))
                .thenThrow(new NoSuchProductException("Товар не найден"));

        assertThrows(NoSuchProductException.class,
                () -> basketService.addProduct(nonExistingProductId));
    }

    @Test
    void addProduct_shouldAddToBasket_whenProductExists() {
        when(storageService.getProductById(existingProductId))
                .thenReturn(Optional.of(testProduct));

        basketService.addProduct(existingProductId);

        verify(basket).addProduct(existingProductId);
    }

    @Test
    void getUserBasket_shouldReturnEmptyBasket_whenNoProducts() {
        when(basket.getProducts()).thenReturn(Collections.emptyMap());
        when(storageService.getProductById(any())).thenReturn(Optional.empty());

        UserBasket result = basketService.getUserBasket();

        assertEquals(0, result.items().size());
        assertEquals(0, result.total());
    }

    @Test
    void getUserBasket_shouldCalculateTotalCorrectly() {
        Map<UUID, Integer> basketContent = Map.of(
                existingProductId, 2
        );

        when(basket.getProducts()).thenReturn(basketContent);
        when(storageService.getProductById(existingProductId))
                .thenReturn(Optional.of(testProduct));

        UserBasket result = basketService.getUserBasket();

        assertEquals(1, result.items().size());
        assertEquals(200, result.total()); // 2 * 100
    }

    @Test
    void getUserBasket_shouldSkipNotFoundProducts() {
        Map<UUID, Integer> basketContent = Map.of(
                existingProductId, 1,
                nonExistingProductId, 1
        );

        when(basket.getProducts()).thenReturn(basketContent);
        when(storageService.getProductById(existingProductId))
                .thenReturn(Optional.of(testProduct));
        when(storageService.getProductById(nonExistingProductId))
                .thenReturn(Optional.empty());

        UserBasket result = basketService.getUserBasket();

        assertEquals(1, result.items().size());
        assertEquals(100, result.total());
    }
}
