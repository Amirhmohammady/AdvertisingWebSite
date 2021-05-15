package com.mycompany.advertising.entity;

/**
 * Created by Amir on 6/7/2020.
 */
public class UserEmailIsAvailableException extends Exception {
    public UserEmailIsAvailableException(String errorMessage) {
        super(errorMessage);
    }
}
