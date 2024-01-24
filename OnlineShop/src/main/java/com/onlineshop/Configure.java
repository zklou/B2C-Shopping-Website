package com.onlineshop;

import com.onlineshop.interceptor.*;
import com.onlineshop.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Configure implements WebMvcConfigurer {

    @Autowired
    SessionInterceptor sessionInterceptor;

    @Autowired
    UserIdentityInterceptor userIdentityInterceptor;

    @Autowired
    CORSInterceptor corsInterceptor;

    @Autowired
    AnalyticsInterceptor analyticsInterceptor;

    @Autowired
    LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(corsInterceptor).addPathPatterns("/**");
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
        registry.addInterceptor(userIdentityInterceptor).addPathPatterns("/**");
        registry.addInterceptor(analyticsInterceptor).addPathPatterns("/**");
        // registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    }

}
