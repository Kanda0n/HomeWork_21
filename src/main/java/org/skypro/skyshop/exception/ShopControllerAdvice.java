package org.skypro.skyshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShopControllerAdvice {
    @ExceptionHandler(NoSuchProductException.class)
    public ResponseEntity<ShopError> handleNoSuchProduct(NoSuchProductException ex) {
        ShopError error = new ShopError(
                "PRODUCT_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ShopError> handleAll(Exception ex) {
        ShopError error = new ShopError(
                "INTERNAL_ERROR",
                "Внутренняя ошибка сервера."
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
