package com.mycompany.advertising.controller;

import com.mycompany.advertising.controller.utils.exceptions.CallRestApiLimitException;
import com.mycompany.advertising.controller.utils.exceptions.CallWebApiLimitException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
}
