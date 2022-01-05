package com.example.taco_cloud.tacos.data;

import com.example.taco_cloud.tacos.Ingredient;

public interface IngredientRepository {
    Iterable<Ingredient> findAll();

    Ingredient findById(String id);

    Ingredient save(Ingredient ingredient);
}
