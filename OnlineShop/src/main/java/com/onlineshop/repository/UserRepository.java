package com.onlineshop.repository;

import com.onlineshop.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    public UserEntity findByUsernameAndPassword(String username, String password);
    public UserEntity findUserEntityById(int userId);

}
