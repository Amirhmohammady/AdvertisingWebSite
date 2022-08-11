package com.mycompany.advertising.service.Dto;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Created by Amir on 8/5/2022.
 */
public class LockApiByIPDto {
    private Method method;
    private String IP;
    private LocalDateTime time;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
