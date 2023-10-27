package com.bits.wilp.order.model;

import java.util.Date;
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
public class Product {
    private String id;
    private String name;
    private String description;
    private String productImageLink;
    private Double price;
    private Integer availableQuantity;
    private Date createdAt;
    private Date updatedAt;
}