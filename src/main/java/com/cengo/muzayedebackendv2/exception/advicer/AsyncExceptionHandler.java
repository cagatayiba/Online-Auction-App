package com.cengo.muzayedebackendv2.exception.advicer;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(AsyncExceptionHandler.class);


    @Override
    public void handleUncaughtException(Throwable ex, Method method, @Nullable Object... params) {
        var log = "Method: " + method.getName() + " | " + ex.getMessage();
        logger.error(log);
    }
}
