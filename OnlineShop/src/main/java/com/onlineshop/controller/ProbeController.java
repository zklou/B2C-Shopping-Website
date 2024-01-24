package com.onlineshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProbeController {

    @GetMapping("/")
    ResponseEntity<String> root(HttpServletRequest request) {
        return ResponseEntity.ok("It works!");
    }

    @GetMapping("/probe")
    ResponseEntity<String> probe(HttpServletRequest request) {

        return ResponseEntity.ok(String.format("Session: %s", request.getAttribute("Session")));
    }

    @GetMapping("/probe_need_login")
    ResponseEntity<String> probe_need_login(HttpServletRequest request) {

        return ResponseEntity.ok(String.format("Session: %s", request.getAttribute("Session")));
    }
}
