package com.onlineshop.repository;

import com.onlineshop.model.BrandEntity;
import com.onlineshop.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
        List<ProductEntity> findProductEntitiesByBrandId(int brandId, Pageable pageable);
        List<ProductEntity> findProductEntitiesByTypeId(int typeId, Pageable pageable);

        Page<ProductEntity> findAll(Pageable pageable);

        ProductEntity getById(int productId);
}
