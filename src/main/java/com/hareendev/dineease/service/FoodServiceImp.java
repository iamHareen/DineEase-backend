package com.hareendev.dineease.service;

import com.hareendev.dineease.dto.request.CreateFoodRequest;
import com.hareendev.dineease.model.Category;
import com.hareendev.dineease.model.Food;
import com.hareendev.dineease.model.Restaurant;
import com.hareendev.dineease.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImp implements FoodService{

    private final FoodRepository foodRepository;

    @Autowired
    public FoodServiceImp(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) {
        Food food = new Food();
        food.setCategory(category);
        food.setRestaurant(restaurant);
        food.setDescription(req.getDescription());
        food.setImages(req.getImages());
        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setIngredients(req.getIngredients());
        food.setSeasonal(req.isSeasonal());
        food.setVegetarian(req.isVegetarian());

        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    @Override
    public void deleteFoodById(Long foodId) throws Exception {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new Exception("Food not found"));
        food.setRestaurant(null);
        foodRepository.save(food);
    }

    @Override
    public List<Food> getRestaurantsFood(
            Long restaurantId,
            boolean isVegetarian,
            boolean isNonVegetarian,
            boolean isSeasonal,
            String foodCategory) {
        List<Food> restaurantsFoods = foodRepository.findByRestaurantId(restaurantId);

        if (isVegetarian) {
            restaurantsFoods = filterByVegetarian(restaurantsFoods);
        }
        if (isNonVegetarian) {
            restaurantsFoods = filterByNonVeg(restaurantsFoods);
        }
        if (isSeasonal) {
            restaurantsFoods = filterBySeasonal(restaurantsFoods);
        }
        if (foodCategory !=null && !foodCategory.isEmpty()) {
            restaurantsFoods = filterByCategory(restaurantsFoods, foodCategory);
        }
        return restaurantsFoods;
    }

    private List<Food> filterByCategory(List<Food> foods, String foodCategory ) {
        return foods.stream().filter(food -> {
            if(food.getCategory() != null) {
                return food.getCategory().getName().equals(foodCategory);
            }
            return false;
        }).collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foods ) {
        return foods.stream().filter(Food::isVegetarian).collect(Collectors.toList());
    }

    private List<Food> filterByNonVeg(List<Food> foods ) {
        return foods.stream().filter(food -> !food.isVegetarian()).collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods ) {
        return foods.stream().filter(Food::isSeasonal).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (optionalFood.isEmpty()) {
            throw new Exception("Food is not exist ...");
        }
        return optionalFood.get();
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
