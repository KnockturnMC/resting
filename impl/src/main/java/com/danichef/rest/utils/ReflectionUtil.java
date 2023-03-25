package com.danichef.rest.utils;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public class ReflectionUtil {

    @NotNull
    public static MethodHandle getter(@NotNull final Field field) {
        try {
            field.setAccessible(true);
            return MethodHandles.lookup().unreflectGetter(field);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("failed to retrieve getter for field", e);
        }
    }

}
