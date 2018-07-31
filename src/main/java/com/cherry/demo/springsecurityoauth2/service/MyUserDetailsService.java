package com.cherry.demo.springsecurityoauth2.service;

import com.cherry.demo.springsecurityoauth2.entity.Authority;
import com.cherry.demo.springsecurityoauth2.entity.User;
import com.cherry.demo.springsecurityoauth2.repository.UserJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 配置 Spring Security 来声明系统中的合法用户及其对应的权限
 * 用户相关的信息是通过org.springframework.security.core.userdetails.UserDetailsService 接口来加载的。
 * 该接口的唯一方法是loadUserByUsername(String username)，用来根据用户名加载相关的信息。
 * @author chenyan
 * @date 上午11:09
 */
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserJPA userJPA;

    /**
     * 这个方法的返回值是org.springframework.security.core.userdetails.UserDetails 接口，
     * 其中包含了用户的信息，包括用户名、密码、权限、是否启用、是否被锁定、是否过期等。
     * 其中最重要的是用户权限，由 org.springframework.security.core.GrantedAuthority 接口来表示。
     * 虽然 Spring Security 内部的设计和实现比较复杂，但是一般情况下，开发人员只需要使用它默认提供的实现就可以满足绝大多数情况下的需求，而且只需要简单的配置声明即可。
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库读取用户
        String lowercaseUsername = username.toLowerCase();
        User user = userJPA.findByUsernameCaseInsensitive(lowercaseUsername);
        if(null == user){
            throw new UsernameNotFoundException("User " + lowercaseUsername + " was not found in the database");
        }

        //设置对应的角色后返回SpringSecurity内置的User对象实例
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : user.getAuthorities()){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            grantedAuthorities.add(grantedAuthority);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities);

//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                user.getAuthorities());
    }
}
