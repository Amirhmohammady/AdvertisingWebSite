package com.mycompany.advertising.components.utils;

/**
 * Created by Amir on 10/24/2021.
 */
public class AViewableException extends RuntimeException {
    public AViewableException(String message){
        super(message);
    }

    public AViewableException(String message, Throwable cause) {
        super(message, cause);
    }

    public AViewableException(Throwable cause) {
        super(cause);
    }
}
