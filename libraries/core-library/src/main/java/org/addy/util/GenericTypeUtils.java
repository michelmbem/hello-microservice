package org.addy.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;

@UtilityClass
public class GenericTypeUtils {

    public ParameterizedType findParameterizedType(@NonNull Class<?> clazz) {
        do {
            if (clazz.getGenericSuperclass() instanceof ParameterizedType parameterizedType) {
                return parameterizedType;
            }

            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);

        return null;
    }
}
