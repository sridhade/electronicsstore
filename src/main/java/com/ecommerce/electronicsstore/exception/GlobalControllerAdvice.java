package com.ecommerce.electronicsstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException e) {
        ErrorResponse error = new ErrorResponse( e.getMessage(),String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BasketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBasketNotFoundException(BasketNotFoundException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(),String.valueOf(HttpStatus.NOT_FOUND.value()) );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
