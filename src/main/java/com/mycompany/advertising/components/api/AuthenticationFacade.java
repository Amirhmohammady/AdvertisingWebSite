package com.mycompany.advertising.service.api;

import com.mycompany.advertising.model.to.UserTo;

/**
 * Created by Amir on 6/24/2020.
 */
public interface AuthenticationFacade {

    UserTo getUserToDetails();
    boolean hasRole(String role);
}
