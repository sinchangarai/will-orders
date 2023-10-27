package com.bits.wilp.order.model;

import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private Integer userId;
    private List<ProductQuantity> productQuantityList;
    private Double amount;
    private String paymentType;
    private String orderStatus;
    private String deliveryAddress;
    private Date placedAt;
    private Date updatedAt;
}
