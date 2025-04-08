package com.hareendev.dineease.repository;

import com.hareendev.dineease.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
