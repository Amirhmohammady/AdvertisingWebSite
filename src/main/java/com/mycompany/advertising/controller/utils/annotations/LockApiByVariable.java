package com.mycompany.advertising.controller.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Amir on 8/3/2022.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LockApiByVariable {
    TimeLimiter timeLimiter();

    String variableName();

    LockerWaitType waitOrErr();

    ReturnType returnType();
}
