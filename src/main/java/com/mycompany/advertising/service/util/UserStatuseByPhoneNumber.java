package com.mycompany.advertising.service.util;

/**
 * Created by Amir on 11/16/2021.
 */
public enum UserStatuseByPhoneNumber {
    NOT_EXIST,
    EXIST_AND_ACTIVATED,
    EXIST_BUT_NOT_ACTIVATED,
    EXIST_BUT_TOKEN_DID_NOT_SEND,
    PHONE_FORMAT_NOT_CORRECT
}
