package com.livecommerce.product.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private BigDecimal price;
  private String category;
  private boolean live;
  private Integer stock;
  private String imageUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}