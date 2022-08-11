package com.mycompany.advertising.controller.utils.exceptions;

/**
 * Created by Amir on 8/5/2022.
 */
public class CallRestApiLimitException extends RuntimeException {
    public CallRestApiLimitException(String message) {
        super(message);
    }

    public CallRestApiLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public CallRestApiLimitException(Throwable cause) {
        super(cause);
    }
}