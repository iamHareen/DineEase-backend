package com.hareendev.dineease.service;

import com.hareendev.dineease.model.Category;
import com.hareendev.dineease.model.Restaurant;
import com.hareendev.dineease.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepository, RestaurantService restaurantService) {
        this.categoryRepository = categoryRepository;
        this.restaurantService = restaurantService;
    }

    @Override
    public Category createCategory(String name, Long userId) throws Exception {
        // Find the restaurant associated with the user
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);

        // Create and save the new category
        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurant);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long restaurantId) throws Exception {
        restaurantService.findRestaurantById(restaurantId); // Verify that the restaurant exists
        return categoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Category findCategoryById(Long id) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(id); // Verify that the category exists
        if(optionalCategory.isEmpty()) {
            throw new Exception("Category not found");
        }
        return optionalCategory.get();
    }

}
