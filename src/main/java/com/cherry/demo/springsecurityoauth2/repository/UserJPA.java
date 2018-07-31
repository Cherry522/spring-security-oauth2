package com.cherry.demo.springsecurityoauth2.repository;

import com.cherry.demo.springsecurityoauth2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * User JPA
 * @author chenyan
 * @date 下午3:50
 */
public interface UserJPA extends JpaRepository<User,String> {

    /**
     * 根据用户名获取用户数据，用户名忽略大小写
     * @param username
     * @return
     */
    @Query("SELECT u FROM User u WHERE LOWER(username) = LOWER(:username)")
    User findByUsernameCaseInsensitive(@Param("username")String username);
}
