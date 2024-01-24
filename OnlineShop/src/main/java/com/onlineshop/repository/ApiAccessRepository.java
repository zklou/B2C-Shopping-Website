package com.onlineshop.repository;

import com.onlineshop.model.ApiAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ApiAccessRepository extends JpaRepository<ApiAccessEntity, Integer> {

    List<ApiAccessEntity> findApiAccessEntitiesByTimeBetween(Date timeStart, Date timeEnd);

}
