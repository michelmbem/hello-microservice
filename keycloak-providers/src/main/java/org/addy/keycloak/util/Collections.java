package org.addy.keycloak.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Collections {

    @SafeVarargs
    public <T> List<T> listOf(T... elements) {
        return elements == null || elements.length == 0 || elements[0] == null
                ? List.of()
                : List.of(elements);

    }
}
