package com.clsaa.dop.server.pipeline.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @GetMapping(value = "/test")
    public String test(){
        return "111";
    }
}
