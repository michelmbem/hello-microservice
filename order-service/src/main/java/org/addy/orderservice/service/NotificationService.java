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
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationSender notificationSender;

    @Value("${mail.sender}")
    private String mailSender;

    public void orderReceived(OrderDto orderDto) {
        var notification = new Notification(
                mailSender,
                orderDto.getCustomer().getEmail(),
                "Order received",
                "order-received",
                getSubstitutions(orderDto)
        );

        notificationSender.send(notification);
    }

    public void orderUpdated(OrderDto orderDto) {
        var notification = new Notification(
                mailSender,
                orderDto.getCustomer().getEmail(),
                "Order updated",
                "order-updated",
                getSubstitutions(orderDto)
        );

        notificationSender.send(notification);
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
                        "number", orderDto.getPaymentMethod().getType().endOfNumber(
                                orderDto.getPaymentMethod().getNumber())),
                "items", orderDto.getItems().stream().map(item -> Map.of(
                        "productName", item.getProduct().getName(),
                        "quantity", item.getQuantity(),
                        "price", item.getTotalPrice()))
                        .toList(),
                "totalPrice", orderDto.getTotalPrice());
    }

    private static String date2str(LocalDateTime dateTime) {
        return dateTime != null
                ? dateTime.format(DateTimeFormatter.ofPattern("d MMM yyyy 'at' HH:mm"))
                : "";
    }
}
