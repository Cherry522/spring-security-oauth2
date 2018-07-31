package com.cherry.demo.springsecurityoauth2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 角色
 * @author chenyan
 * @date 下午6:28
 */
@Entity
public class Authority {
    @Id
    @Column(updatable = false,nullable = false)
    @NotNull
    @Size(max = 50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
