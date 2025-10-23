package com.livecommerce.order.service.impl;

import com.livecommerce.order.client.ProductClient;
import com.livecommerce.order.domain.*;
import com.livecommerce.order.repository.OrderRepository;
import com.livecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Override
    public Order createOrder(Order order) {
        List<OrderItem> validItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            // Product fetch karo
            ProductClient.ProductDto product = productClient.getProductById(item.getProductId());

            if (product.stock >= item.getQuantity()) {
                // Order item prepare karo
                OrderItem orderItem = OrderItem.builder()
                        .productId(product.id)
                        .productName(product.name)
                        .price(product.price)
                        .quantity(item.getQuantity())
                        .build();
                validItems.add(orderItem);

                // Total amount calculate karo
                totalAmount = totalAmount.add(product.price.multiply(BigDecimal.valueOf(item.getQuantity())));

                // ✅ Stock reduce karo product service me
                productClient.reduceStock(product.id, item.getQuantity());

            } else {
                throw new RuntimeException("Product " + product.name + " stock not sufficient");
            }
        }

        order.setItems(validItems);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(OffsetDateTime.now());
        order.setUpdatedAt(OffsetDateTime.now());

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public Order updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        order.setUpdatedAt(OffsetDateTime.now());
        return orderRepository.save(order);
    }
}
