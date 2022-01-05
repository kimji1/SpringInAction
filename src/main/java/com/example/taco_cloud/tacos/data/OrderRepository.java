package com.example.taco_cloud.tacos.data;

import com.example.taco_cloud.tacos.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
