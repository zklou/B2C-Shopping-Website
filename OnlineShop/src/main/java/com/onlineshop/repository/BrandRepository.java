package com.onlineshop.repository;

import com.onlineshop.model.BrandEntity;
import com.onlineshop.model.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandEntity, Integer> { }
