package com.mycompany.advertising.controller.events;

import com.mycompany.advertising.model.to.UserTo;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Amir on 9/14/2021.
 */
public class OnSigningUpCompleteEvent extends ApplicationEvent {
    private UserTo user;

    public OnSigningUpCompleteEvent(UserTo user) {
        super(user);

        this.user = user;
    }

    public UserTo getUser() {
        return user;
    }

    public void setUser(UserTo user) {
        this.user = user;
    }
}
