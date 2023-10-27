package com.bits.wilp.order.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.bits.wilp.order.model.Order;


@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    // @Query("{'name': ?0}")
    // Optional<Order> findByProduct(String productName);
    // // @Query("{'name': {$regex: ?0 }}")
    // @Query("{'$or':[ {'name': {$regex: ?0 }}, {'description': {$regex: ?0 }} ] }")
    // List<Order> findByCustomQuery(String name);
    
}