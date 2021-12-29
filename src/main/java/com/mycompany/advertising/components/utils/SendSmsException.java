package com.mycompany.advertising.components.utils;

/**
 * Created by Amir on 12/29/2021.
 */
public class SendSmsException extends Exception {
    public SendSmsException(String message) {
        super(message);
    }

    public SendSmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendSmsException(Throwable cause) {
        super(cause);
    }
}