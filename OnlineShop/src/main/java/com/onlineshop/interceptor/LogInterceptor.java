package com.onlineshop.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        var body =  request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        System.err.print(
                String.format("%s %s\n%s\n\n%s\n\n", request.getMethod(), request.getServletPath(), getHeaders(request), body)
        );

        return true;
    }

    String getHeaders(HttpServletRequest request) {
        String result = "";

        var headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            var headerName = headerNames.nextElement();
            result += headerName;
            result += ": ";
            result += request.getHeader(headerName);
            result += "\n";

        }

        return result;
    }


}
