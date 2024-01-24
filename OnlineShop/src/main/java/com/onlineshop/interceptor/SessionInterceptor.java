package com.onlineshop.interceptor;

import com.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var cookies = request.getCookies();
        String session;

        if(cookies == null) {
            session = userService.requestSession();
            response.addCookie(new Cookie("Session", session));
        }
        else {
            Optional<Cookie> optionalSessionCookie = Arrays.stream(cookies).filter(c -> "Session".equals(c.getName())).findFirst();

            if(!optionalSessionCookie.isPresent()) {
                session = userService.requestSession();
                response.addCookie(new Cookie("Session", session));
            }
            else{
                session = optionalSessionCookie.get().getValue();
            }
        }



        request.setAttribute("Session", session);

        return true;
    }

}
