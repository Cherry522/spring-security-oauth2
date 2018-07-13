package com.cherry.demo.springsecurityoauth2.entity;

import org.apache.catalina.Engine;
import org.hibernate.dialect.InnoDBStorageEngine;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.File;

/**
 * RefreshToken信息表:刷新Token时需要用到refresh_token信息
 * @author chenyan
 * @date 下午2:34
 */
@Entity
public class RefreshToken {
    @Id
    @Column(updatable = false,nullable = false)
    @Size(max = 256)
    private String tokenId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="token",updatable = false, columnDefinition="blob")
    private byte[] token;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="authentication",updatable = false, columnDefinition="blob")
    private byte[] authentication;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    public byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }
}
