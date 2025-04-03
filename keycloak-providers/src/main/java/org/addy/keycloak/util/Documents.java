package org.addy.keycloak.util;

import lombok.experimental.UtilityClass;
import org.bson.Document;

import java.util.Objects;

@UtilityClass
public class Documents {

    public Long getLong(Document document, String field) {
        try {
            return document.getLong(field);
        } catch (ClassCastException e) {
            return document.getDouble(field).longValue();
        }
    }

    public boolean getBoolean(Document document, String field, boolean defaultValue) {
        return Objects.requireNonNullElse(document.getBoolean(field), defaultValue);
    }
}
