package com.mycompany.advertising.entity;

/**
 * Created by Amir on 9/10/2021.
 */
public class TokenNotFoundtException extends RuntimeException {
    public TokenNotFoundtException() {
        super();
    }

    public TokenNotFoundtException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TokenNotFoundtException(final String message) {
        super(message);
    }

    public TokenNotFoundtException(final Throwable cause) {
        super(cause);
    }
}
