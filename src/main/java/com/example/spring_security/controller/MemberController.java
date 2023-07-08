package com.example.spring_security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {


    @Value("${spring.datasource.password}")
    private Long password;

    @GetMapping("/test")
    public ResponseEntity<Object> test(

    ) {
        return new ResponseEntity<>(password, HttpStatus.OK);
    }
}
