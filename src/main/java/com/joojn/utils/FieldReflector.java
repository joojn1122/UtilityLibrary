package com.joojn.utils;

import com.sun.istack.internal.Nullable;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class FieldReflector  {

    private Field field;
    private final Object instance;

    public FieldReflector(
            Class<?> clazz,
            Object instance,
            boolean searchAll,
            String type,
            int index,
            String name
    ) throws RuntimeException {
        this.instance = instance;

        AtomicInteger currentIndex = new AtomicInteger(-1);

        ClassUtil.walk(
                searchAll,
                clazz,
                (Class<?> c) -> {
                    for (Field field : c.getDeclaredFields()) {
                        if(name != null && !field.getName().equals(name))
                            continue;

                        if (type != null && !field.getType().getName().equals(type))
                            continue;

                        if (currentIndex.getAndIncrement() > index)
                            continue;

                        this.field = field;
                        return true;
                    }

                    return false;
                }
        );

        if(this.field == null)
            throw new RuntimeException("Method not found");

        this.field.setAccessible(true);
    }

    public static class Builder {

        private Class<?>   targetClass = null;
        private boolean    searchAll   = false;
        private String     type        = null;
        private int        index       = 0;
        private String     name        = null;
        private Object     instance    = null;

        public Builder targetClass(Class<?> targetClass) {
            this.targetClass = targetClass;
            return this;
        }

        public Builder searchAll(boolean searchAll) {
            this.searchAll = searchAll;
            return this;
        }

        public Builder type(Class<?> returnTypeClass) {
            return type(returnTypeClass.getName());
        }

        public Builder type(String returnTypeClassName) {
            this.type = returnTypeClassName;
            return this;
        }

        public Builder index(int index) {
            this.index = index;
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

        public FieldReflector build() {
            return new FieldReflector(
                    this.targetClass,
                    this.instance,
                    this.searchAll,
                    this.type,
                    this.index,
                    this.name
            );
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        try
        {
            return (T) field.get(instance);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(Object value) {
        try
        {
            field.set(instance, value);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Field getField() {
        return field;
    }
}
