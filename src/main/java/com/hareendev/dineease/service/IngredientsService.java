package com.hareendev.dineease.service;

import com.hareendev.dineease.model.IngredientCategory;
import com.hareendev.dineease.model.IngredientsItem;

import java.util.List;

// Service Interface for managing ingredientCategories and ingredientsItem
public interface IngredientsService {

    IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception;

    IngredientCategory findIngredientCategoryById(Long id) throws Exception;

    List<IngredientCategory> findAllIngredientCategoriesByRestaurantId(Long restaurantId) throws Exception;

    IngredientsItem createIngredientsItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception;

    List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) throws Exception;

    IngredientsItem updatesStock(Long id) throws Exception;

}
