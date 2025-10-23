package com.livecommerce.order.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id")
  private List<OrderItem> items;
}
