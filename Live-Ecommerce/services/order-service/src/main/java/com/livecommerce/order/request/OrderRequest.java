package com.livecommerce.order.request;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private String userId;
    private Long productId;
    private Integer quantity;
}
