package com.onlineshop.repository;

import com.onlineshop.model.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findOrderEntitiesByUserId(int userId, Pageable pageable);

    List<OrderEntity> findOrderEntitiesByOrderTimeBetween(Date startTime, Date endTime);

}
