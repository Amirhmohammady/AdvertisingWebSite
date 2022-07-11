package com.mycompany.advertising.components.api;

import com.mycompany.advertising.model.to.UserTo;

import java.util.Locale;

/**
 * Created by Amir on 6/24/2020.
 */
public interface AuthenticationFacade {

    UserTo getCurrentUser();
    boolean hasRole(String role);
    String getDomainName();
    Locale getCurrentLocale();
}
