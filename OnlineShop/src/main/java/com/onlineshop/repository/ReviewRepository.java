package com.onlineshop.repository;

import com.onlineshop.model.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    List<ReviewEntity> findReviewEntitiesByProductId(int productId);

}
