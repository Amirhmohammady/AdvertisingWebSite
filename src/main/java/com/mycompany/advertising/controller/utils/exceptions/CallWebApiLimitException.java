package com.mycompany.advertising.controller.utils.exceptions;

/**
 * Created by Amir on 8/4/2022.
 */
public class CallWebApiLimitException extends RuntimeException {
    public CallWebApiLimitException(String message){
        super(message);
    }

    public CallWebApiLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public CallWebApiLimitException(Throwable cause) {
        super(cause);
    }
}