package com.mycompany.advertising.service.api;

import com.mycompany.advertising.model.to.UserTo;

import java.lang.reflect.Method;

/**
 * Created by Amir on 8/5/2022.
 */
public interface LockerApiService {
    long getMethodTimeWaitByIP(Method method, String IP, int maxReq, int inSec);
    long getMethodTimeWaitByUser(Method method, UserTo user, int maxReq, int inSec);
    long getMethodTimeWaitByVariable(Method method, String var, int maxReq, int inSec);
    void saveApiCalledWithIPLimit(Method method, String IP);
    void saveApiCalledWithUserLimit(Method method, UserTo user);
    void saveApiCalledWithVariableLimit(Method method, String var);
    void deleteAllExpiredLocker();
    void removeLastLockByVariable(Method method, String var);

}
