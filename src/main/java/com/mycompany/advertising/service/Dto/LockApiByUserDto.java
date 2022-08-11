package com.mycompany.advertising.service.Dto;

import com.mycompany.advertising.model.to.UserTo;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Created by Amir on 8/5/2022.
 */
public class LockApiByUserDto {
    private Method method;
    private UserTo user;
    private LocalDateTime time;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public UserTo getUser() {
        return user;
    }

    public void setUser(UserTo user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
