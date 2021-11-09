package com.mycompany.advertising.components.utils;

/**
 * Created by Amir on 11/9/2021.
 */
public class PhoneNumberFormatException extends Exception {
    public PhoneNumberFormatException(String message) {
        super(message);
    }

    public PhoneNumberFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNumberFormatException(Throwable cause) {
        super(cause);
    }
}
