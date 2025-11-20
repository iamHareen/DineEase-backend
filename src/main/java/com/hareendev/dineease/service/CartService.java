package com.hareendev.dineease.service;

import com.hareendev.dineease.dto.request.AddCartItemRequest;
import com.hareendev.dineease.model.Cart;
import com.hareendev.dineease.model.CartItem;

public interface CartService {

    CartItem addItemToCart(AddCartItemRequest addCartItemRequest, String jwtToken) throws Exception;

    CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;

    Cart removeItemFromCart(Long cartItemId, String jwtToken) throws Exception;

    Long calculateCartTotals(Cart cart) throws Exception;

    Cart findCartById(Long cartId) throws Exception;

    Cart findCartByUserId(String jwtToken) throws Exception;

    Cart clearCart(String jwtToken) throws Exception;

}
