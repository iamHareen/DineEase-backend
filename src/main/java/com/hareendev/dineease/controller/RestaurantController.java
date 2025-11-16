package com.hareendev.dineease.controller;

import com.hareendev.dineease.dto.RestaurantDTO;
import com.hareendev.dineease.model.Restaurant;
import com.hareendev.dineease.model.User;
import com.hareendev.dineease.service.RestaurantService;
import com.hareendev.dineease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, UserService userService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam String keyword
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwtToken); // Authenticate user

        List<Restaurant> restaurants = restaurantService.searchRestaurant(keyword);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Restaurant>> getAllRestaurants(
            @RequestHeader("Authorization") String jwtToken
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwtToken); // Authenticate user

        List<Restaurant> restaurants = restaurantService.getAllRestaurant();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> findRestaurantById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable Long restaurantId
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwtToken); // Authenticate user

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }


    @PutMapping("/{restaurantId}/add-favorites")
    public ResponseEntity<RestaurantDTO> addToFavorites(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable Long restaurantId
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwtToken); // Authenticate user

        RestaurantDTO restaurant = restaurantService.addToFavourites(restaurantId, user);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}
