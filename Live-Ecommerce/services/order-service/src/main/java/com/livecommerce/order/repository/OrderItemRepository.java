//package com.livecommerce.order.repository;
//
//import com.livecommerce.order.domain.OrderItem;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import java.util.UUID;
//
//@Repository
//public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
//
//    @Modifying
//    @Query("DELETE FROM OrderItem oi WHERE oi.order.id = :orderId")
//    void deleteByOrderId(@Param("orderId") UUID orderId);
//
//}
