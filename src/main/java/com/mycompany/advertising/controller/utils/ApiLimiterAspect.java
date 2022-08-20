package com.mycompany.advertising.controller.utils;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.controller.utils.annotations.*;
import com.mycompany.advertising.controller.utils.exceptions.CallRestApiLimitException;
import com.mycompany.advertising.controller.utils.exceptions.CallWebApiLimitException;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.LockerApiService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amir on 8/3/2022.
 */
@Aspect
@Component
public class ApiLimiterAspect {
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    LockerApiService lockerApiService;
    private Map<Method, Integer> lockApiByVariableIndexes = new HashMap<>();

    @Before("@annotation(com.mycompany.advertising.controller.utils.annotations.LockApiByUser)")
    public void lockApiByUser(JoinPoint joinPoint) throws Throwable {//ProceedingJoinPoint
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LockApiByUser lock = method.getAnnotation(LockApiByUser.class);
        TimeLimiter timeLimiter = lock.timeLimiter();
        UserTo user = authenticationFacade.getCurrentUser();
        long watingTime = lockerApiService.getMethodTimeWaitByUser(method, user, timeLimiter.maxRequest(), timeLimiter.inSeconds());
        if (watingTime > 0) {
            throw lock.excptionType().getDeclaredConstructor(String.class).newInstance(lock.exceptionMsg());
            /*if (lock.waitOrErr() == LockerWaitType.WAIT) Thread.sleep(watingTime * 1000);
            else {
                if (lock.returnType() == ReturnType.JSON)
                    throw new CallRestApiLimitException("time limit exceeded to call " + method.getName());
                else throw new CallWebApiLimitException("time limit exceeded to call " + method.getName());
            }*/
        }
        lockerApiService.saveApiCalledWithUserLimit(method, user);
    }

    @Before("@annotation(com.mycompany.advertising.controller.utils.annotations.LockApiByVariable)")
    public void lockApiByVariable(JoinPoint joinPoint) throws Throwable {//ProceedingJoinPoint
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LockApiByVariable lock = method.getAnnotation(LockApiByVariable.class);
        TimeLimiter timeLimiter = lock.timeLimiter();
        if (lockApiByVariableIndexes.get(method) == null) {
            LockApiByVariable annotation = method.getAnnotation(LockApiByVariable.class);
            String parameterName = annotation.variableName();
            int idx = Arrays.asList(signature.getParameterNames()).indexOf(parameterName);
            //Arrays.asList(signature.getParameterNames()).forEach(System.out::println);
            lockApiByVariableIndexes.put(method, idx);
        }
        String value = joinPoint.getArgs()[lockApiByVariableIndexes.get(method)].toString();
        long watingTime = lockerApiService.getMethodTimeWaitByVariable(method, value, timeLimiter.maxRequest(), timeLimiter.inSeconds());
        if (watingTime > 0) {
            throw lock.excptionType().getDeclaredConstructor(String.class).newInstance(lock.exceptionMsg());
            /*if (lock.waitOrErr() == LockerWaitType.WAIT) Thread.sleep(watingTime * 1000);
            else {
                if (lock.returnType() == ReturnType.JSON)
                    throw new CallRestApiLimitException("time limit exceeded to call " + method.getName());
                else throw new CallWebApiLimitException("time limit exceeded to call " + method.getName());
            }*/
        }
        lockerApiService.saveApiCalledWithVariableLimit(method, value);


        /*System.out.println("================================================================");
        if (ttt) throw new CallRestApiLimitException("++++++++++++++");
        ttt = true;
        System.out.println("waiting 5 seconds");
        Thread.sleep(5000);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (lockApiByVariableIndexes.get(method) == null) {
            LockApiByVariable annotation = method.getAnnotation(LockApiByVariable.class);
            String parameterName = annotation.variableName();
            System.out.println("variable name= " + parameterName);
            int idx = Arrays.asList(signature.getParameterNames()).indexOf(parameterName);
            Arrays.asList(signature.getParameterNames()).forEach(System.out::println);
            System.out.println("index= " + idx);
            lockApiByVariableIndexes.put(method, idx);
        }
        System.out.println("IP= " + ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr());
        String value = joinPoint.getArgs()[lockApiByVariableIndexes.get(method)].toString();
        System.out.println("value= " + value);
        LockApiByVariable lock = method.getAnnotation(LockApiByVariable.class);
        System.out.println(method.getParameters());
        System.out.println("================================================================");*/
    }

    @Before("@annotation(com.mycompany.advertising.controller.utils.annotations.LockApiByIP)")
    public void lockApiByIP(JoinPoint joinPoint) throws Throwable {//ProceedingJoinPoint
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LockApiByIP lock = method.getAnnotation(LockApiByIP.class);
        TimeLimiter timeLimiter = lock.timeLimiter();
        String IP = authenticationFacade.getCurrentClientIp();
        long watingTime = lockerApiService.getMethodTimeWaitByIP(method, IP, timeLimiter.maxRequest(), timeLimiter.inSeconds());
        if (watingTime > 0) {
            throw lock.excptionType().getDeclaredConstructor(String.class).newInstance(lock.exceptionMsg());
            /*if (lock.waitOrErr() == LockerWaitType.WAIT) Thread.sleep(watingTime * 1000);
            else {
                if (lock.returnType() == ReturnType.JSON)
                    throw new CallRestApiLimitException("time limit exceeded to call " + method.getName());
                else throw new CallWebApiLimitException("time limit exceeded to call " + method.getName());
            }*/
        }
        lockerApiService.saveApiCalledWithIPLimit(method, IP);
    }
}
