package com.onlineshop.service;

import com.onlineshop.Util;
import com.onlineshop.model.SessionEntity;
import com.onlineshop.model.UserEntity;
import com.onlineshop.repository.SessionRepository;
import com.onlineshop.repository.UserRepository;
import org.apache.catalina.User;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.Properties;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    String salt = "";

    public UserService() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            salt = properties.getProperty("online_shop.password_salt");
        }
        catch (Throwable t) {
            System.err.println("Salt not setup:");
            t.printStackTrace(System.err);
        }
    }

    public String requestSession() {
        SessionEntity session = new SessionEntity();
        sessionRepository.save(session);
        return session.getCookiesSession();
    }

    public ResponseEntity<Object> getUserInfo(int userId) {
        UserEntity user = userRepository.findUserEntityById(userId);

        return ResponseEntity.ok(Map.of(
            "first_name", user.getFirstName(),
            "last_name", user.getLastName(),
            "email", user.getEmail()
        ));

    }

    public ResponseEntity<Object> register(String username, String email, String password, String firstName, String lastName) {
        UserEntity userEntity = new UserEntity(firstName, lastName, email, username, doSalt(password));

        try {
            userEntity = userRepository.save(userEntity);
        }
        catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "id", -1,
                    "message", e.toString()
            ));
        }

        return ResponseEntity.ok().body(Map.of(
                "id", userEntity.getId(),
                "message", ""
        ));
    }

    public ResponseEntity<Object> login(String username, String password, String session) {
        password = doSalt(password);
        UserEntity userEntity = userRepository.findByUsernameAndPassword(username, password);
        if(userEntity == null) {
            return ResponseEntity.badRequest().body(Util.buildGenericResponse(-1, "Login fail."));
        }

        var sessionEntity = sessionRepository.findByCookiesSession(session);
        sessionEntity.setUserId(userEntity.getId());
        sessionRepository.save(sessionEntity);

        return ResponseEntity.ok().body(Util.buildGenericResponse(0, "Success"));
    }

    public ResponseEntity<Object> logout(String session) {
        var sessionEntity = sessionRepository.findByCookiesSession(session);
        if(sessionEntity != null) {
            sessionEntity.setUserId(-1);
            sessionRepository.save(sessionEntity);
        }

        return ResponseEntity.ok().body(Util.buildGenericResponse(0, "Success"));

    }

    public UserEntity checkLogin(String username, String password) {
       return userRepository.findByUsernameAndPassword(username, password);
    }

    String doSalt(String password) {
        try {
            byte[] bytes = (password + salt).getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.update(bytes);

            return new BigInteger(1, messageDigest.digest()).toString(16);
        }
        catch (Throwable t) {
            System.err.println("Error when do salt:");
            t.printStackTrace(System.err);
        }

        return password;
    }

}
