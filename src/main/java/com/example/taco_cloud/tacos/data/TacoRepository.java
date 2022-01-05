package com.example.taco_cloud.tacos.data;

import com.example.taco_cloud.tacos.Taco;
import org.springframework.data.repository.CrudRepository;

public interface TacoRepository extends CrudRepository<Taco, Long> {
}
