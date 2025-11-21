package com.hareendev.dineease.service;

import com.hareendev.dineease.dto.request.OrderRequest;
import com.hareendev.dineease.model.Order;
import com.hareendev.dineease.model.User;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {

    Order createOrder(OrderRequest order, User user) throws Exception;
    Order updateOrder(Long orderId, String orderStatus) throws Exception;

    void cancelOrder(Long orderId) throws Exception;

    List<Order> getUserOrder(Long userId) throws Exception;

    List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception;

    Order findOrderById(Long orderId) throws Exception;
}
