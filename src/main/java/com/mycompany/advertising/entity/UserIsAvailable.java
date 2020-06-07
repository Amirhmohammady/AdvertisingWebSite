package com.mycompany.advertising.entity;

/**
 * Created by Amir on 6/7/2020.
 */
public class UserIsAvailable extends Exception {
    public UserIsAvailable(String errorMessage) {
        super(errorMessage);
    }
}
