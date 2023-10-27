package com.bits.wilp.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantity {
    
    private String productId;
    private Integer quantity;
}
