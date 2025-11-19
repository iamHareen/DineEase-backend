package com.hareendev.dineease.controller;

import com.hareendev.dineease.dto.request.CreateFoodRequest;
import com.hareendev.dineease.dto.response.MessageResponse;
import com.hareendev.dineease.model.Food;
import com.hareendev.dineease.model.Restaurant;
import com.hareendev.dineease.model.User;
import com.hareendev.dineease.service.FoodService;
import com.hareendev.dineease.service.RestaurantService;
import com.hareendev.dineease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {
    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;

    @Autowired
    public AdminFoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req,
                                           @RequestHeader("Authorization") String jwtToken) throws Exception {
        User user = userService.findUserByJwtToken(jwtToken);
        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());

        // Additional check to ensure the user is an admin of the restaurant can be added here
        if (!restaurant.getOwner().getId().equals(user.getId())) {
            throw new Exception(" You do not have permission to add food to this restaurant.");
        }

        Food createdFood = foodService.createFood(req, req.getCategory(), restaurant);
        return new ResponseEntity<>(createdFood, HttpStatus.CREATED);
    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long foodId,
                                           @RequestHeader("Authorization") String jwtToken) throws Exception {
        // 1. Get the User
        User user = userService.findUserByJwtToken(jwtToken);

        // 2. Get the Food item so we can see which Restaurant it belongs to
        // (Assuming your FoodService has a findFoodById method)
        Food food = foodService.findFoodById(foodId);

        // 3. Check if the current user is the owner of that Restaurant
        User restaurantOwner = food.getRestaurant().getOwner();

        if (!user.getId().equals(restaurantOwner.getId())) {
            throw new Exception("You do not have permission to delete this food item.");
        }


        foodService.deleteFoodById(foodId);


        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Food item deleted successfully.");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping("/{foodId}")
    public ResponseEntity<Food> updateAvailabilityStatus(@PathVariable Long foodId,
                                                         @RequestHeader("Authorization") String jwtToken) throws Exception {

        // 1. Get the User
        User user = userService.findUserByJwtToken(jwtToken);

        // 2. Get the Food item first to check its owner
        Food food = foodService.findFoodById(foodId);

        // 3. Check if the current user owns the restaurant this food belongs to
        if (!food.getRestaurant().getOwner().getId().equals(user.getId())) {
            throw new Exception("You do not have permission to update this food item.");
        }

        // 4. If verified, proceed to update
        Food updatedFood = foodService.updateAvailabilityStatus(foodId);

        return new ResponseEntity<>(updatedFood, HttpStatus.OK);
    }

}
