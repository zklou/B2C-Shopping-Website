package com.onlineshop.interceptor;

import com.onlineshop.model.SessionEntity;
import com.onlineshop.repository.SessionRepository;
import com.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserIdentityInterceptor implements HandlerInterceptor {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UserRepository userRepository;

    String[] loginRequiredPaths = { "/probe_need_login", "/cart", "/check_out", "/orders" };

    String[] adminRequiredPaths = { "/admin" };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String session = (String)request.getAttribute("Session");

        SessionEntity sessionEntity = sessionRepository.findByCookiesSession(session);

        if(sessionEntity == null) {

            response.getWriter().write("Catastrophe-level accidents occur: " + "User passed a session ID but not in database.");

            response.setStatus(555);

            return false;
        }

        if(sessionEntity.getUserId() == -1 || sessionEntity.getUserId() == 0) {
            for (var path: loginRequiredPaths) {
                if(request.getServletPath().startsWith(path)) {
                    response.setStatus(401);
                    response.getWriter().write("{\"id\": -1, \"message\": \"Need login :(\"}");
                    return false;
                }
            }
        }
        else {
            for (var path: adminRequiredPaths) {
                if(request.getServletPath().startsWith(path)) {
                    if(sessionEntity.getUserId() >= -1) {
                        response.setStatus(401);
                        response.getWriter().write("{\"id\": -1, \"message\": \"Need login :(\"}");
                        return false;
                    }
                }
            }
        }

        request.setAttribute("UserID", sessionEntity.getUserId());

        return true;
    }

}
