package com.onlineshop.repository;

import com.onlineshop.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    List<CartEntity> findCartEntitiesByUserIdOrderById(int userId);

}
