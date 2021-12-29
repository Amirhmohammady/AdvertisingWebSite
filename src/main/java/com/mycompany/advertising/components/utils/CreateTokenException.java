package com.mycompany.advertising.components.utils;

/**
 * Created by Amir on 12/17/2021.
 */
public class CreateTokenException extends Exception {
    public CreateTokenException(String message) {
        super(message);
    }

    public CreateTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateTokenException(Throwable cause) {
        super(cause);
    }
}