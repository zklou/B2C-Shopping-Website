package com.onlineshop.repository;

import com.onlineshop.model.SessionEntity;
import com.onlineshop.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Integer> {

    SessionEntity findByCookiesSession(String cookies_session);

}
