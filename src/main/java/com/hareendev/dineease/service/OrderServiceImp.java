package com.hareendev.dineease.service;

import com.hareendev.dineease.dto.request.OrderRequest;
import com.hareendev.dineease.model.*;
import com.hareendev.dineease.repository.AddressRepository;
import com.hareendev.dineease.repository.OrderItemRepository;
import com.hareendev.dineease.repository.OrderRepository;
import com.hareendev.dineease.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public OrderServiceImp(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            AddressRepository addressRepository,
            CartService cartService,
            UserRepository userRepository, RestaurantService restaurantService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.addressRepository = addressRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.restaurantService = restaurantService;
    }

    @Override
    public Order createOrder(OrderRequest order, User user) throws Exception {
        Address shippingAddress = order.getDeliveryAddress();
        Address savedAddress = addressRepository.save(shippingAddress);

        if (!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(savedAddress);
            userRepository.save(user);
        }

        // Fetch restaurant
        Restaurant restaurant = restaurantService.findRestaurantById(order.getRequestId());

        // Create Order
        Order createdOrder = new Order();
        createdOrder.setCustomer(user);
        createdOrder.setCreatedAt(new Date());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.setDeliverAddress(savedAddress);
        createdOrder.setRestaurant(restaurant);


        Cart cart = cartService.findCartByUserId(restaurant.getId()); // Fetch cart by restaurant id

        List<OrderItem> orderItems = new ArrayList<>(); // Initialize the list
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(); // Create a new OrderItem instance
            orderItem.setFood(cartItem.getFood());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem); // Add the saved OrderItem to the list
        }

        Long totalPrice = cartService.calculateCartTotals(cart);

        createdOrder.setItems(orderItems);
        createdOrder.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(createdOrder);
        restaurant.getOrders().add(savedOrder);

        return createdOrder;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        if(orderStatus.equals("OUT_FOR_DELIVERY")
                || orderStatus.equals("DELIVERED")
                || orderStatus.equals("COMPLETED")
                || orderStatus.equals("PENDING")
        ){
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }
        throw new Exception("Please Select a valid Order Status");
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
//        Order order = findOrderById(orderId);
        if (orderId == null) {
            throw new Exception("Order ID cannot be null");
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> getUserOrder(Long userId) throws Exception {
        if (userId == null) {
            throw new Exception("User ID cannot be null");
        }
        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception {

        // Validate restaurantId
        if (restaurantId == null) {
            throw new Exception("Restaurant ID cannot be null");
        }

        List<Order> orders = orderRepository.findByRestaurantId(restaurantId); // Fetch orders by restaurant ID
        if (orderStatus != null) {
            // Filter orders by order status if provided
            orders = orders.stream().filter(order ->
                    order.getOrderStatus().equals(orderStatus)).toList();
        }
        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found"));
    }
}
