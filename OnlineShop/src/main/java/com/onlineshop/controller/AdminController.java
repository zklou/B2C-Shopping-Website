package com.onlineshop.controller;

import com.onlineshop.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    AnalyticsService analyticsService;

    @GetMapping("/admin/sales_report/{year}/{month}")
    ResponseEntity<Object> getSalesReport(@PathVariable(name = "year") int year, @PathVariable(name = "month") int month) {

        return analyticsService.getSalesLog(year, month);
    }

    @GetMapping("/admin/access_report/{year}/{month}")
    ResponseEntity<Object> getAccessReport(@PathVariable(name = "year") int year, @PathVariable(name = "month") int month) {

        return analyticsService.getAccessLog(year, month);
    }

}
