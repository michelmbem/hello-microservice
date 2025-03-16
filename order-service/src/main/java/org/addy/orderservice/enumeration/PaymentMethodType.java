package org.addy.orderservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.apache.commons.lang.StringUtils.right;

@RequiredArgsConstructor
@Getter
public enum PaymentMethodType {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD( "Debit Card"),
    PAYPAL( "PayPal");

    private final String englishName;

    public String endOfNumber(String number) {
        return switch (this) {
            case PAYPAL -> right(number, number.indexOf('@'));
            default -> right(number, 4);
        };
    }
}
