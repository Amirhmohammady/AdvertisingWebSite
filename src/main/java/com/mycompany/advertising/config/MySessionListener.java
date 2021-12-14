package com.mycompany.advertising.config;

import com.mycompany.advertising.model.to.UserTo;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Amir on 12/4/2021.
 */
public class MySessionListener implements HttpSessionListener {
    private final static Logger logger = Logger.getLogger(MySessionListener.class);
    private int maxinactiveinterval;

    public MySessionListener(int maxinactiveinterval) {
        this.maxinactiveinterval = maxinactiveinterval;
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(maxinactiveinterval);
        UserTo userTo = getUserTo();
        if (userTo != null)
            logger.debug("session ID " + event.getSession().getId() + " created for max inactive interval = "
                    + maxinactiveinterval + "for user phone number: " + userTo.getUsername());
        else
            logger.debug("session ID " + event.getSession().getId() + " created for max inactive interval = " + maxinactiveinterval);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        logger.debug("session ID " + event.getSession().getId() + " destroyed");
    }

    private UserTo getUserTo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object myUser = auth.getPrincipal();
            if (myUser instanceof UserTo)
                return (UserTo) myUser;
        }
        return null;
    }
}
