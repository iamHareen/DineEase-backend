package com.hareendev.dineease.service;

import com.hareendev.dineease.dto.request.AddCartItemRequest;
import com.hareendev.dineease.model.Cart;
import com.hareendev.dineease.model.CartItem;
import com.hareendev.dineease.model.Food;
import com.hareendev.dineease.model.User;
import com.hareendev.dineease.repository.CartItemRepository;
import com.hareendev.dineease.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImp implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final UserService userService;

    private final FoodService foodService;

    @Autowired
    public CartServiceImp(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserService userService,
            FoodService foodService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.foodService = foodService;
    }


    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwtToken) throws Exception {
        User user = userService.findUserByJwtToken(jwtToken);
        Food food = foodService.findFoodById(req.getFoodId());
        Cart cart = cartRepository.findByCustomerId(user.getId());

        for(CartItem item : cart.getItems()) {
            if(item.getFood().getId().equals(food.getId())) {
                item.setQuantity(item.getQuantity() + req.getQuantity());
                return cartItemRepository.save(item);
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setFood(food);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setIngredients(req.getIngredients());
        newCartItem.setTotalPrice(req.getQuantity()*food.getPrice());

        // Save the new cart item to the repository
        CartItem savedCartItem = cartItemRepository.save(newCartItem);

        // Add the saved cart item to the cart's items list
        cart.getItems().add(savedCartItem);

        return savedCartItem;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if(cartItemOptional.isEmpty()){
            throw new Exception("Cart item not found");
        }
        CartItem item = cartItemOptional.get();
        item.setQuantity(quantity);

        // Update total price based on new quantity
        item.setTotalPrice(item.getFood().getPrice() * quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwtToken) throws Exception {
        User user = userService.findUserByJwtToken(jwtToken);
        Cart cart = cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if(cartItemOptional.isEmpty()){
            throw new Exception("Cart item not found");
        }

        CartItem item = cartItemOptional.get(); // Get the cart item to be removed

        cart.getItems().remove(item); // Remove the item from the cart's items list

        return cartRepository.save(cart); // Save and return the updated cart
    }

    @Override
    public Long calculateCartTotals(Cart cart) throws Exception {
        if(cart == null) {
            throw new Exception("Cart cannot be null");
        }
        long total = 0L;
        for(CartItem cartItem : cart.getItems()) {
            total += cartItem.getFood().getPrice()*cartItem.getQuantity(); // Sum up the total price
        }
        return total;
    }

    @Override
    public Cart findCartById(Long cartId) throws Exception {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if(optionalCart.isEmpty()) {
            throw new Exception("Cart not found");
        }
        return optionalCart.get();
    }

    @Override
    public Cart findCartByUserId(String jwtToken) throws Exception {
        if(jwtToken == null) {
            throw new Exception("JWT token cannot be null");
        }
        User user = userService.findUserByJwtToken(jwtToken); // Get user from JWT token
        return cartRepository.findByCustomerId(user.getId());
    }

    @Override
    public Cart clearCart(String jwtToken) throws Exception {
        if(jwtToken == null) {
            throw new Exception("JWT token cannot be null");
        }
        User user = userService.findUserByJwtToken(jwtToken); // Get user from JWT token

        Cart cart = cartRepository.findByCustomerId(user.getId()); // Find cart by user ID

        cart.getItems().clear(); // Clear all items from the cart
        return cartRepository.save(cart);
    }
}
