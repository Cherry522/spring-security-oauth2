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
 * @author chenyan
 * @date 上午11:09
 */
@Service("userDetailsService")
public class HengYuUserDetailsService implements UserDetailsService {

    @Autowired
    UserJPA userJPA;

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
    }
}
