package com.cherry.demo.springsecurityoauth2.service;

import com.cherry.demo.springsecurityoauth2.SpringSecurityOauth2Application;
import com.cherry.demo.springsecurityoauth2.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author chenyan
 * @date 2:18 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Transactional
public class MyUserDetailsServiceTest {
    @Autowired
    MyUserDetailsService userDetailsService;

    @Test
    public void loadUserByUsername() {
    }

    //添加用户
    @Test
    public void add() {
        User user = new User();
        user.setEmail("test@123.com");
        user.setPassword("123456");
        user.setUsername("test");
        userDetailsService.add(user);
    }
}