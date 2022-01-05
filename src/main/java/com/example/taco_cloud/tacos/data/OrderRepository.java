package com.example.taco_cloud.tacos.data;

import com.example.taco_cloud.tacos.Order;

public interface OrderRepository {
    Order save(Order order);
}
