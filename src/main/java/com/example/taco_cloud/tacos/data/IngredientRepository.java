package com.example.taco_cloud.tacos.data;

import com.example.taco_cloud.tacos.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
