package com.cherry.demo.springsecurityoauth2.repository;

import com.cherry.demo.springsecurityoauth2.entity.Authority;
import com.cherry.demo.springsecurityoauth2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * User JPA
 * @author chenyan
 * @date 下午3:50
 */
public interface AuthorityJPA extends JpaRepository<Authority,String> {

}
