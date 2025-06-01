package org.skypro.skyshop.exception;

public record ShopError(
        String code,
        String message
) {
}
