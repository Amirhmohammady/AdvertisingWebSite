package com.mycompany.advertising.controller;

import com.mycompany.advertising.controller.utils.exceptions.CallRestApiLimitException;
import com.mycompany.advertising.controller.utils.exceptions.CallWebApiLimitException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 8/4/2022.
 */
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CallRestApiLimitException.class})
    public ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {CallWebApiLimitException.class})
    public String webApiLimitException(RuntimeException ex, WebRequest request) {
        return "error_pages/error-403.html";
    }

//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<String> handleBindException2(BindException ex,WebRequest request)
//    {
//        System.out.println("handleBindException<<<<<<<<<<<<<<<<<<<<<<<");
//        ex.printStackTrace();
//        System.out.println("handleBindException<<<<<<<<<<<<<<<<<<<<<<<");
//        System.out.println(ex.getMessage());
//        System.out.println("handleBindException<<<<<<<<<<<<<<<<<<<<<<<");
//        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
//    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> result = new ArrayList<>();
        for (FieldError fe : fieldErrors) {
            result.add(fe.getDefaultMessage());
        }
        return new ResponseEntity<>(result, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>(ex.getMessage(), status);
    }
}
