package com.joojn.utils;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MethodReflector  {

    private Method method;
    private final Object instance;

    public MethodReflector(
            Class<?> clazz,
            Object instance,
            boolean searchAll,
            String returning,
            int index,
            Object[] params,
            String name
    ) throws RuntimeException {
        this.instance = instance;

        AtomicInteger currentIndex = new AtomicInteger(-1);

        ClassUtil.walk(
                searchAll,
                clazz,
                (Class<?> c) -> {
                    for (Method method : c.getDeclaredMethods()) {
                        if(name != null && !method.getName().equals(name))
                            continue;

                        if (returning != null && !method.getReturnType().getName().equals(returning))
                            continue;

                        if (params != null && !Arrays.equals(method.getParameterTypes(), params))
                            continue;

                        if (currentIndex.getAndIncrement() > index)
                            continue;

                        this.method = method;
                        return true;
                    }

                    return false;
                }
        );

        if(this.method == null)
            throw new RuntimeException("Method not found");

        this.method.setAccessible(true);
    }

    public static class Builder {

        private Class<?>   targetClass = null;
        private boolean    searchAll   = false;
        private String     returningClassName = null;
        private int        index       = 0;
        private Class<?>[] parameters  = null;
        private String     name        = null;
        private Object     instance = null;

        public Builder targetClass(Class<?> targetClass) {
            this.targetClass = targetClass;
            return this;
        }

        public Builder searchAll(boolean searchAll) {
            this.searchAll = searchAll;
            return this;
        }

        public Builder returning(Class<?> returningClass) {
            return returning(returningClass.getName());
        }

        public Builder returning(String returningClassName) {
            this.returningClassName = returningClassName;
            return this;
        }

        public Builder index(int index) {
            this.index = index;
            return this;
        }

        public Builder parameters(Class<?>... parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder instance(@Nullable Object instance) {
            this.instance = instance;
            return this;
        }

        public MethodReflector build() {
            return new MethodReflector(
                    this.targetClass,
                    this.instance,
                    this.searchAll,
                    this.returningClassName,
                    this.index,
                    this.parameters,
                    this.name
            );
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T invoke(Object... params) {
        try
        {
            return (T) method.invoke(instance, params);
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getMethod() {
        return method;
    }
}
