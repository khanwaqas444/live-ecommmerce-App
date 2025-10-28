package com.livecommerce.order.service.impl;

import com.livecommerce.order.client.ProductClient;
import com.livecommerce.order.domain.*;
import com.livecommerce.order.repository.OrderItemRepository;
import com.livecommerce.order.repository.OrderRepository;
import com.livecommerce.order.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
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

        @Override
        public Order cancelOrder(UUID orderId) {
            // 1️⃣ Fetch order
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
            // 2️⃣ Check if already cancelled or delivered (not allowed)
            if (order.getStatus() == OrderStatus.CANCELLED) {
                throw new IllegalStateException("Order is already cancelled");
            }
            if (order.getStatus() == OrderStatus.DELIVERED) {
                throw new IllegalStateException("Delivered orders cannot be cancelled");
            }
            // 3️⃣ Update status
            order.setStatus(OrderStatus.CANCELLED);
            // 4️⃣ (Optional) set cancellation date
            order.setCancelledAt(LocalDateTime.now());
            // 5️⃣ Save updated order
            return orderRepository.save(order);
        }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOrderItemsByOrderId(UUID orderId) {
        // ensure order exists
        if (!orderRepository.existsById(orderId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        // perform single-statement delete via repository
        orderItemRepository.deleteByOrderId(orderId);

        // optional: update order metadata (e.g., set a status or updatedAt)
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setUpdatedAt(OffsetDateTime.now());
        // do NOT change order row (do not set deleted flag unless you want to)
        orderRepository.save(order);
    }

}
