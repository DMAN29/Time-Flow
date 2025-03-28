package com.app.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.model.Order;

@Repository
public interface OrderRepo extends MongoRepository<Order, String>{

	Optional<Order> findByStyleNo(String styleNo);

	Optional<Order> findByItemNo(String itemNo);


}
