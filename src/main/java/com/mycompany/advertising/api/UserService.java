package com.mycompany.advertising.api;

import com.mycompany.advertising.entity.UserIsAvailable;
import com.mycompany.advertising.model.to.UserTo;

/**
 * Created by Amir on 6/7/2020.
 */
public interface UserService {
    void svaeUser(UserTo userTo) throws UserIsAvailable;
}
