package org.addy.orderservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethodType {
    CREDIT_CARD("credit card"),
    DEBIT_CARD( "debit card"),
    PAYPAL( "PayPal"),
    UNKNOWN("<unknown payment method>");

    private final String englishName;
}
