package org.addy.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.model.messaging.Notification;
import org.addy.orderservice.dto.CustomerDto;
import org.addy.orderservice.dto.OrderDto;
import org.addy.orderservice.dto.OrderItemDto;
import org.addy.orderservice.dto.PaymentMethodDto;
import org.addy.orderservice.enumeration.PaymentMethodType;
import org.addy.orderservice.notification.NotificationSender;
import org.addy.orderservice.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationSender notificationSender;

    @Value("${mail.from}")
    private String mailFrom;

    public void orderReceived(OrderDto orderDto) {
        notificationSender.send(new Notification(
                mailFrom,
                orderDto.getCustomer().getEmail(),
                "Order received",
                "order-received",
                getSubstitutions(orderDto)
        ));
    }

    public void orderUpdated(OrderDto orderDto) {
        notificationSender.send(new Notification(
                mailFrom,
                orderDto.getCustomer().getEmail(),
                "Order updated",
                "order-updated",
                getSubstitutions(orderDto)
        ));
    }

    private static Map<String, Object> getSubstitutions(OrderDto orderDto) {
        return Map.of(
                "orderId", orderDto.getId(),
                "orderDate", date2str(orderDto.getCreatedOn()),
                "deliveryDate", date2str(orderDto.getDeliveryDate()),
                "customer", customerAsMap(orderDto.getCustomer()),
                "paymentMethod", paymentMethodAsMap(orderDto.getPaymentMethod()),
                "items", orderDto.getItems().stream().map(NotificationService::orderItemAsMap).toList(),
                "totalPrice", orderDto.getTotalPrice()
        );
    }

    private static Map<String, Object> customerAsMap(CustomerDto customerDto) {
        return Map.of(
                "name", customerDto.getName(),
                "address", String.valueOf(customerDto.getAddress()),
                "city", String.valueOf(customerDto.getCity()),
                "state", String.valueOf(customerDto.getState()),
                "postalCode", String.valueOf(customerDto.getPostalCode())
        );
    }

    private static Map<String, Object> paymentMethodAsMap(PaymentMethodDto pm) {
        if (pm != null) {
            return Map.of(
                    "type", pm.getType().getEnglishName(),
                    "number", pm.getNumberEnd()
            );
        }

        return Map.of(
                "type", PaymentMethodType.UNKNOWN.name(),
                "number", Constants.UNKNOWN_ATTRIBUTE_VALUE
        );
    }

    private static Map<String, Object> orderItemAsMap(OrderItemDto orderItemDto) {
        return Map.of(
                "productName", orderItemDto.getProduct().getName(),
                "quantity", orderItemDto.getQuantity(),
                "price", orderItemDto.getTotalPrice()
        );
    }

    private static String date2str(LocalDateTime dateTime) {
        return dateTime != null
                ? dateTime.format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' HH:mm").localizedBy(Locale.ENGLISH))
                : "";
    }
}
