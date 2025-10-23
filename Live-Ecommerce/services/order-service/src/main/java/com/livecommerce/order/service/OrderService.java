package com.livecommerce.order.service;

import com.livecommerce.order.domain.Order;
import com.livecommerce.order.domain.OrderStatus;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order createOrder(Order order);
    List<Order> getOrdersByUser(String userId);
    Order getOrderById(UUID orderId);
    Order updateOrderStatus(UUID orderId, OrderStatus status);
}
