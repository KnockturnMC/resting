package com.danichef.rest.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LynxPlay on 13.02.2017.
 */
public class ReflectedObject {

    private final HashMap<String, Field> fields = new HashMap<>();
    private final Class<?> clazz;
    private final Object reflected;

    public ReflectedObject(Object reflected) {
        clazz = reflected.getClass();
        this.reflected = reflected;
        findAllFields();
    }

    public ReflectedObject with(String name, Object value) {
        if (fields.containsKey(name)) {
            try {
                fields.get(name).set(reflected, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private void findAllFields() {
        Class<?> workingOn = clazz;
        List<Field> fields = new ArrayList<>();

        while (workingOn != null) {
            fields.addAll(Arrays.asList(workingOn.getFields()));
            fields.addAll(Arrays.asList(workingOn.getDeclaredFields()));

            workingOn = workingOn.getSuperclass();
        }

        fields.forEach(field -> this.fields.put(field.getName(), field)); //Find fields with annotation
        this.fields.values().forEach(field -> field.setAccessible(true)); //Set them Accessible
    }

    public Object get() {
        return reflected;
    }

}