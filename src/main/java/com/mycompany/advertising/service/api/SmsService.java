package com.mycompany.advertising.service.api;

/**
 * Created by Amir on 9/16/2021.
 */
public interface SmsService {
    public int sendSms(String message, String phonenumber);
}
