package com.livecommerce.order.repository;

import com.livecommerce.order.domain.Order;
import com.livecommerce.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(String userId);
    List<Order> findByStatus(OrderStatus status);
//    List<Order> findByDeletedTrue(); // ✅ Added
}
