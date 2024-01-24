package com.onlineshop.interceptor;

import com.onlineshop.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AnalyticsInterceptor implements HandlerInterceptor {

    @Autowired
    AnalyticsService analyticsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        analyticsService.logAccess(request.getServletPath());
        return true;
    }

}
