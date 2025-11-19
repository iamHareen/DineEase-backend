package com.hareendev.dineease.service;

import com.hareendev.dineease.model.IngredientCategory;
import com.hareendev.dineease.model.IngredientsItem;
import com.hareendev.dineease.model.Restaurant;
import com.hareendev.dineease.repository.IngredientCategoryRepository;
import com.hareendev.dineease.repository.IngredientItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientsServiceImp implements IngredientsService {

    private final IngredientItemRepository ingredientItemRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public IngredientsServiceImp(IngredientItemRepository ingredientItemRepository,
                                 IngredientCategoryRepository ingredientCategoryRepository,
                                 RestaurantService restaurantService) {
        this.ingredientItemRepository = ingredientItemRepository;
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.restaurantService = restaurantService;
    }

    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        IngredientCategory category = new IngredientCategory();
        category.setRestaurant(restaurant);
        category.setName(name);
        return ingredientCategoryRepository.save(category);
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {
        Optional<IngredientCategory> opt = ingredientCategoryRepository.findById(id);

        if (opt.isEmpty()) {
            throw new Exception("Ingredient Category not found");
        }

        return opt.get();
    }

    @Override
    public List<IngredientCategory> findAllIngredientCategoriesByRestaurantId(Long restaurantId) throws Exception {
        restaurantService.findRestaurantById(restaurantId);
        return ingredientCategoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem createIngredientsItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category = findIngredientCategoryById(categoryId);

        IngredientsItem item = new IngredientsItem();
        item.setName(ingredientName);
        item.setRestaurant(restaurant);
        item.setCategory(category);

        IngredientsItem ingredient = ingredientItemRepository.save(item);
        category.getIngredients().add(ingredient);
        return ingredient;
    }

    @Override
    public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) {
        return ingredientItemRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem updatesStock(Long id) throws Exception {
        Optional<IngredientsItem> optionalIngredientsItem = ingredientItemRepository.findById(id);
        if(optionalIngredientsItem.isEmpty()) {
            throw new Exception("Ingredient Item not found");
        }
        IngredientsItem ingredientsItem = optionalIngredientsItem.get();
        ingredientsItem.setInStock(!ingredientsItem.isInStock());
        return ingredientItemRepository.save(ingredientsItem);
    }
}
