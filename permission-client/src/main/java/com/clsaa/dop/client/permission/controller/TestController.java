package com.clsaa.dop.client.permission.controller;

import com.clsaa.dop.client.permission.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@EnableAutoConfiguration
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/test")
    public void HelloWorld() {
        testService.TestService(22L);
    }
}