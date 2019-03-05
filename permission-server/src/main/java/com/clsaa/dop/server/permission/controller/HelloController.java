package com.clsaa.dop.server.permission.controller;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 测试用首页
 *
 * @author lzy
 *
 *since :2019.3.1
 */
@RestController
@EnableAutoConfiguration
public class HelloController {

    @GetMapping(value = "/hello")
     String printhello() {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
               return "Hello ";
    }


}