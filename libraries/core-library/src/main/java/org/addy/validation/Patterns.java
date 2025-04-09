package org.addy.validation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Patterns {
    public final String PHONE_NUMBER = "^(?:\\+[1-9]\\d{0,2}[\\- ]?)?(?:\\([1-9]\\d{2}\\)[\\- ]?)?\\d(?:[\\- ]?\\d){1,10}$";
}
