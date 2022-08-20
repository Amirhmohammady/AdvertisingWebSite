package com.mycompany.advertising.service;

import com.mycompany.advertising.controller.utils.annotations.LockApiByIP;
import com.mycompany.advertising.controller.utils.annotations.LockApiByUser;
import com.mycompany.advertising.controller.utils.annotations.LockApiByVariable;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.Dto.LockApiByIPDto;
import com.mycompany.advertising.service.Dto.LockApiByUserDto;
import com.mycompany.advertising.service.Dto.LockApiByVariableDto;
import com.mycompany.advertising.service.api.LockerApiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 8/6/2022.
 */
@Service
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class LockerApiServiceImpl implements LockerApiService {
    private static final Logger logger = Logger.getLogger(LockerApiServiceImpl.class);

    private List<LockApiByIPDto> lockApiByIPDtos = new ArrayList<>();
    private List<LockApiByUserDto> lockApiByUserDtos = new ArrayList<>();
    private List<LockApiByVariableDto> lockApiByVariableDtos = new ArrayList<>();

    @Override
    public long getMethodTimeWaitByIP(Method method, String IP, int maxReq, int inSec) {
        LocalDateTime tempTime = LocalDateTime.now().minusSeconds(inSec);
        LocalDateTime lastCall = LocalDateTime.now();
        int methodCalls = 0;
        for (int i = lockApiByIPDtos.size() - 1; i >= 0; i--) {
            if (lockApiByIPDtos.get(i).getTime().isAfter(tempTime)) {
                if (lockApiByIPDtos.get(i).getIP().equals(IP) && lockApiByIPDtos.get(i).getMethod().equals(method)) {
                    methodCalls++;
                    if (lockApiByIPDtos.get(i).getTime().isBefore(lastCall))
                        lastCall = lockApiByIPDtos.get(i).getTime();
                }
            } else break;
        }
        long result;
        if (methodCalls < maxReq) result = 0;
        else result = inSec - Duration.between(lastCall, LocalDateTime.now()).getSeconds();
        logger.info("method " + method + " called " + methodCalls + " times by IP " + IP + " in last " + inSec + " seconds and you need to wait " + result + " seconds");
        return result;
    }

    @Override
    public long getMethodTimeWaitByUser(Method method, UserTo user, int maxReq, int inSec) {
        LocalDateTime tempTime = LocalDateTime.now().minusSeconds(inSec);
        LocalDateTime lastCall = LocalDateTime.now();
        int methodCalls = 0;
        for (int i = lockApiByUserDtos.size() - 1; i >= 0; i--) {
            if (lockApiByUserDtos.get(i).getTime().isAfter(tempTime)) {
                if (lockApiByUserDtos.get(i).getUser() == user && lockApiByUserDtos.get(i).getMethod().equals(method)) {
                    methodCalls++;
                    if (lockApiByUserDtos.get(i).getTime().isBefore(lastCall))
                        lastCall = lockApiByUserDtos.get(i).getTime();
                }
            } else break;
        }
        long result;
        if (methodCalls < maxReq) result = 0;
        else result = inSec - Duration.between(lastCall, LocalDateTime.now()).getSeconds();
        logger.info("method " + method + " called " + methodCalls + " times by User " + user + " in last " + inSec + " seconds and you need to wait " + result + " seconds");
        return result;
    }

    @Override
    public long getMethodTimeWaitByVariable(Method method, String var, int maxReq, int inSec) {
        LocalDateTime tempTime = LocalDateTime.now().minusSeconds(inSec);
        LocalDateTime lastCall = LocalDateTime.now();
        int methodCalls = 0;
        for (int i = lockApiByVariableDtos.size() - 1; i >= 0; i--) {
            if (lockApiByVariableDtos.get(i).getTime().isAfter(tempTime)) {
                if (lockApiByVariableDtos.get(i).getValue().equals(var) && lockApiByVariableDtos.get(i).getMethod().equals(method)) {
                    methodCalls++;
                    if (lockApiByVariableDtos.get(i).getTime().isBefore(lastCall))
                        lastCall = lockApiByVariableDtos.get(i).getTime();
                }
            } else break;
        }
        long result;
        if (methodCalls < maxReq) result = 0;
        else result = inSec - Duration.between(lastCall, LocalDateTime.now()).getSeconds();
        logger.info("method " + method + " called " + methodCalls + " times by value " + var + " in last " + inSec + " seconds and you need to wait " + result + " seconds");
        return result;
    }

    @Override
    public void saveApiCalledWithIPLimit(Method method, String IP) {
        LockApiByIPDto lockByIP = new LockApiByIPDto();
        lockByIP.setMethod(method);
        lockByIP.setTime(LocalDateTime.now());
        lockByIP.setIP(IP);
        lockApiByIPDtos.add(lockByIP);
    }

    @Override
    public void saveApiCalledWithUserLimit(Method method, UserTo user) {
        LockApiByUserDto lockByUser = new LockApiByUserDto();
        lockByUser.setMethod(method);
        lockByUser.setTime(LocalDateTime.now());
        lockByUser.setUser(user);
        lockApiByUserDtos.add(lockByUser);
    }

    @Override
    public void saveApiCalledWithVariableLimit(Method method, String var) {
        LockApiByVariableDto lockByVar = new LockApiByVariableDto();
        lockByVar.setMethod(method);
        lockByVar.setTime(LocalDateTime.now());
        lockByVar.setValue(var);
        lockApiByVariableDtos.add(lockByVar);
    }

    @Override
    public void deleteAllExpiredLocker() {
        int i;
        for (i = 0; i < lockApiByIPDtos.size(); i++) {
            LockApiByIP annotation = lockApiByIPDtos.get(i).getMethod().getDeclaredAnnotation(LockApiByIP.class);
            if (Duration.between(lockApiByIPDtos.get(i).getTime(), LocalDateTime.now()).getSeconds() > annotation.timeLimiter().inSeconds()) {
                lockApiByIPDtos.remove(i);
                i--;
            }
        }
        logger.debug(String.valueOf(i) + " saved methods locked by IP deleted from memory");
        for (i = 0; i < lockApiByUserDtos.size(); i++) {
            LockApiByUser annotation = lockApiByUserDtos.get(i).getMethod().getDeclaredAnnotation(LockApiByUser.class);
            if (Duration.between(lockApiByUserDtos.get(i).getTime(), LocalDateTime.now()).getSeconds() > annotation.timeLimiter().inSeconds()) {
                lockApiByUserDtos.remove(i);
                i--;
            }
        }
        logger.debug(String.valueOf(i) + " saved methods locked by user deleted from memory");
        for (i = 0; i < lockApiByVariableDtos.size(); i++) {
            LockApiByVariable annotation = lockApiByVariableDtos.get(i).getMethod().getDeclaredAnnotation(LockApiByVariable.class);
            if (Duration.between(lockApiByVariableDtos.get(i).getTime(), LocalDateTime.now()).getSeconds() > annotation.timeLimiter().inSeconds()) {
                lockApiByVariableDtos.remove(i);
                i--;
            }
        }
        logger.debug(String.valueOf(i) + " saved methods locked by variable deleted from memory");
    }

    @Override
    public void removeLastLockByVariable(Method method, String var) {
        int i;
        for (i = lockApiByVariableDtos.size() - 1; i >= 0; i--)
            if (lockApiByVariableDtos.get(i).getValue().equals(var) && lockApiByVariableDtos.get(i).getMethod().equals(method)) {
                lockApiByVariableDtos.remove(i);
                break;
            }
        if (i >= 0) logger.info("last call method " + method + " by variable " + var + " is removed");
        else logger.info("can not remove last call method " + method + " by variable " + var);
    }
}