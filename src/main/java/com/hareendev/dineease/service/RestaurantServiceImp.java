package com.hareendev.dineease.service;

import com.hareendev.dineease.dto.RestaurantDTO;
import com.hareendev.dineease.model.Address;
import com.hareendev.dineease.model.Restaurant;
import com.hareendev.dineease.model.User;
import com.hareendev.dineease.repository.AddressRepository;
import com.hareendev.dineease.repository.RestaurantRepository;
import com.hareendev.dineease.dto.request.CreateRestaurantRequest;
import com.hareendev.dineease.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImp implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    // Use constructor injection
    @Autowired
    public RestaurantServiceImp(
            RestaurantRepository restaurantRepository,
            AddressRepository addressRepository,
            UserRepository userRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {

        // Save Address first
        Address address = addressRepository.save(req.getAddress());

        // Create Restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setDescription(req.getDescription());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updateRestaurant) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new Exception("Restaurant not found"));

        if (restaurant.getCuisineType() != null) {
            restaurant.setCuisineType(updateRestaurant.getCuisineType());
        }

        if (restaurant.getDescription() != null) {
            restaurant.setDescription(updateRestaurant.getDescription());
        }

        if (restaurant.getName() != null) {
            restaurant.setName(updateRestaurant.getName());
        }
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new Exception("Restaurant not found"));
        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isEmpty()) {
            throw new Exception("Restaurant not found");
        }
        return restaurant.get();
    }

    @Override
    public Restaurant getRestaurantByUserId(Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if (restaurant == null) {
            throw new Exception("Restaurant not found for the given user ID"+ userId);
        }
        return restaurant;
    }

    @Override
    public RestaurantDTO addToFavourites(Long restaurantId, User user) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId);
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setDescription(restaurant.getDescription());
        restaurantDTO.setImages(restaurant.getImages());
        restaurantDTO.setTitle(restaurant.getName());
        restaurantDTO.setId(restaurantId);

        boolean isFavorite = false;
        List<Restaurant> favorites = user.getFavourites();
        for(Restaurant favorite : favorites) {
            if(favorite.getId().equals(restaurantId)) {
                isFavorite = true;
                break;
            }
        }

        if(isFavorite) {
            favorites.removeIf(favorite -> favorite.getId().equals(restaurantId));
        } else {
            favorites.add(restaurant);
        }

        userRepository.save(user);
        return restaurantDTO;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {
        Restaurant restaurant = findRestaurantById(id);
        if (restaurant == null) {
            throw new Exception("Restaurant not found");
        }
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);

    }
}
