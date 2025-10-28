package com.livecommerce.order.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE orders SET deleted = true, deleted_at = now() WHERE id = ?")
@Where(clause = "deleted = false")
public class Order {

  @Id
  @GeneratedValue
  private UUID id;

  private String userId;

  private BigDecimal totalAmount;

  @Enumerated(EnumType.STRING)
  private OrderStatus status; // CREATED, PAID, SHIPPED, DELIVERED

  private OffsetDateTime createdAt;

  private OffsetDateTime updatedAt;

  private LocalDateTime cancelledAt;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id")
  private List<OrderItem> items;
}
