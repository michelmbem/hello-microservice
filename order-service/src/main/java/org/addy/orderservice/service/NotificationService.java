package org.addy.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.messaging.Notification;
import org.addy.orderservice.dto.OrderDto;
import org.addy.orderservice.notification.NotificationSender;
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

    private Map<String, Object> getSubstitutions(OrderDto orderDto) {
        return Map.of(
                "orderId", orderDto.getId(),
                "orderDate", date2str(orderDto.getCreatedOn()),
                "deliveryDate", date2str(orderDto.getDeliveryDate()),
                "customer", Map.of(
                        "name", orderDto.getCustomer().getName(),
                        "address", orderDto.getCustomer().getAddress(),
                        "city", orderDto.getCustomer().getCity(),
                        "state", orderDto.getCustomer().getState(),
                        "postalCode", orderDto.getCustomer().getPostalCode()),
                "paymentMethod", Map.of(
                        "type", orderDto.getPaymentMethod().getType().getEnglishName(),
                        "number", orderDto.getPaymentMethod().getNumberEnd()),
                "items", orderDto.getItems().stream().map(item -> Map.of(
                        "productName", item.getProduct().getName(),
                        "quantity", item.getQuantity(),
                        "price", item.getTotalPrice()))
                        .toList(),
                "totalPrice", orderDto.getTotalPrice());
    }

    private static String date2str(LocalDateTime dateTime) {
        return dateTime != null
                ? dateTime.format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' HH:mm").localizedBy(Locale.ENGLISH))
                : "";
    }
}
