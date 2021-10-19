package com.mycompany.advertising.service.api;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Amir on 10/16/2021.
 */
public interface SSLRESTClient<T> {
    public T callWebService(String url, Class<T> type) throws NoSuchAlgorithmException,KeyManagementException,KeyStoreException;
}
