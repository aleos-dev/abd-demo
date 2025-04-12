package org.aleos.transaction;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class TransactionProxy implements InvocationHandler {
    private final Object target;
    private final TransactionManager transactionManager;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Transaction.class)) {
            return method.invoke(target, args);
        }

        try {
            transactionManager.beginTransaction();
            Object result = method.invoke(target, args);
            transactionManager.commitTransaction();
            return result;
        } catch (Throwable e) {
            transactionManager.rollbackTransaction();
            throw e;
        }
    }
}
