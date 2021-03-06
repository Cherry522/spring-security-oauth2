package com.cherry.demo.springsecurityoauth2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenyan
 * @date 下午4:01
 */
@RestController
@RequestMapping("/hello")
public class HelloWorldController {

    @RequestMapping(method = RequestMethod.GET)
    public String sayHello(){
        return "Hello User!";
    }
}
