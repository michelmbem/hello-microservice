package org.addy.orderservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethodType {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD( "Debit Card"),
    PAYPAL( "PayPal");

    private final String englishName;
}
