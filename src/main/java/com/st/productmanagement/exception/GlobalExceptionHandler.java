package com.st.productmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // it is the combination of both  controller advice and response body
public class GlobalExceptionHandler {
    //product not found

    @ExceptionHandler(ProductNotFoundException.class) // method handler for product not found exception in built method
    //  create the method
    public ResponseEntity<String> handleProductNotFound(ProductNotFoundException productNotFoundException){
        return  new ResponseEntity<>(productNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<String> handlerProductAlreadyExistException(ProductAlreadyExistException productAlreadyExistException){
        return  new ResponseEntity<>(productAlreadyExistException.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        ));

        return new ResponseEntity<>(
                errors,
                HttpStatus.BAD_REQUEST);
    }
}
