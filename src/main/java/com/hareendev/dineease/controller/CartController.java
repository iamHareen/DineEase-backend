package com.hareendev.dineease.controller;

import com.hareendev.dineease.dto.request.AddCartItemRequest;
import com.hareendev.dineease.dto.request.UpdateCartItemRequest;
import com.hareendev.dineease.model.Cart;
import com.hareendev.dineease.model.CartItem;
import com.hareendev.dineease.model.User;
import com.hareendev.dineease.service.CartService;
import com.hareendev.dineease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(
            @RequestBody AddCartItemRequest req,
            @RequestHeader("Authorization") String jwtToken) throws Exception {
        CartItem cartItem = cartService.addItemToCart(req, jwtToken);
        return new ResponseEntity<>(cartItem,HttpStatus.OK);
    }

    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @RequestBody UpdateCartItemRequest req,
            @RequestHeader("Authorization") String jwtToken) throws Exception {
        CartItem cartItem = cartService.updateCartItemQuantity(req.getCartItemId(), req.getQuantity());
        return new ResponseEntity<>(cartItem,HttpStatus.OK);
    }

    @DeleteMapping("/cart-item/{id}/update") // id --> cartItemID
    public ResponseEntity<Cart> removeCartItem(
            @PathVariable long id,
            @RequestHeader("Authorization") String jwtToken) throws Exception {
        Cart cart = cartService.removeItemFromCart(id, jwtToken);
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(
            @RequestHeader("Authorization") String jwtToken) throws Exception {
        User user = userService.findUserByJwtToken(jwtToken);
        Cart cart = cartService.clearCart(user.getId());
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> findUserCart(
            @RequestHeader("Authorization") String jwtToken) throws Exception {
        User user = userService.findUserByJwtToken(jwtToken);
        Cart cart = cartService.findCartByUserId(user.getId());
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

}
