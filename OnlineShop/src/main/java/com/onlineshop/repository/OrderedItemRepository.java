package com.onlineshop.repository;

import com.onlineshop.model.OrderedItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedItemRepository extends JpaRepository<OrderedItemEntity,Integer> {

    List<OrderedItemEntity> findOrderedItemEntitiesByProductListId(int productListId);

}
