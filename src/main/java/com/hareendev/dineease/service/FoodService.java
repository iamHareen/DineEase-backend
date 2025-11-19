package com.hareendev.dineease.service;

import com.hareendev.dineease.dto.request.CreateFoodRequest;
import com.hareendev.dineease.model.Category;
import com.hareendev.dineease.model.Food;
import com.hareendev.dineease.model.Restaurant;

import java.util.List;

public interface FoodService {

    Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);

    void deleteFoodById(Long foodId) throws Exception;

    List<Food> getRestaurantsFood(Long restaurantId,
                                  boolean isVegetarian,
                                  boolean isNonVegetarian,
                                  boolean isSeasonal,
                                  String foodCategory
                                  );

    List<Food> searchFood(String keyword);

    Food findFoodById(Long foodId) throws Exception;

    Food updateAvailabilityStatus(Long foodId) throws Exception;


}
