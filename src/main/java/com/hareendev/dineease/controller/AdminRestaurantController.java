package com.hareendev.dineease.controller;

import com.hareendev.dineease.dto.request.CreateRestaurantRequest;
import com.hareendev.dineease.dto.response.MessageResponse;
import com.hareendev.dineease.model.Restaurant;
import com.hareendev.dineease.model.User;
import com.hareendev.dineease.service.RestaurantService;
import com.hareendev.dineease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;

    @Autowired
    public AdminRestaurantController(RestaurantService restaurantService, UserService userService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(
            @RequestBody CreateRestaurantRequest req,
            @RequestHeader ("Authorization") String jwt
    ) throws Exception {
            User user = userService.findUserByJwtToken(jwt);
            Restaurant restaurant = restaurantService.createRestaurant(req, user);
//            return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
            return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @RequestBody CreateRestaurantRequest req,
            @RequestHeader ("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt); // Authenticate admin user

        // Proceed to update the restaurant
        Restaurant restaurant = restaurantService.updateRestaurant(id, req);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt); // Authenticate admin user

        restaurantService.deleteRestaurant(id); // Proceed to delete the restaurant
        MessageResponse res = new MessageResponse();
        res.setMessage("Restaurant deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt); // Authenticate admin user
        Restaurant restaurant = restaurantService.updateRestaurantStatus(id); // Proceed to update status
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Restaurant> getRestaurantByUserId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt); // Authenticate admin user

        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId()); // Proceed to get restaurant by user ID
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

}
