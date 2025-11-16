package com.hareendev.dineease.service;

import com.hareendev.dineease.dto.RestaurantDTO;
import com.hareendev.dineease.model.Restaurant;
import com.hareendev.dineease.model.User;
import com.hareendev.dineease.dto.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {

    Restaurant createRestaurant(CreateRestaurantRequest req, User user);

    Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updateRestaurant) throws Exception;

    void deleteRestaurant(Long restaurantId) throws Exception;

    List<Restaurant> getAllRestaurant();

    List<Restaurant> searchRestaurant(String keyword);

    Restaurant findRestaurantById(Long id) throws Exception;

    Restaurant getRestaurantByUserId(Long userId) throws Exception;

    RestaurantDTO addToFavourites(Long restaurantId, User user) throws Exception;

    Restaurant updateRestaurantStatus(Long id) throws Exception;
}
