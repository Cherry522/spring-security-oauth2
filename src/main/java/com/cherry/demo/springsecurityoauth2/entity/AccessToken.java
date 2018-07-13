package com.cherry.demo.springsecurityoauth2.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * AccessToken信息表:我们使用的是SpringSecurityOAuth2提供的Jdbc方式进行操作Token，所以需要根据标准创建对应的表结构
 * @author chenyan
 * @date 下午2:34
 */
@Entity
public class AccessToken {
    @Id
    @Column(updatable = false,nullable = false)
    @Size(max = 256)
    private String tokenId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="token",updatable = false, columnDefinition="blob")
    private byte[] token;

    @Column(updatable = false,nullable = false)
    @Size(max = 256)
    private String authenticationId;

    @Column(updatable = false,nullable = false)
    @Size(max = 256)
    private String userName;

    @Column(updatable = false,nullable = false)
    private String clientId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="authentication",updatable = false, columnDefinition="blob")
    private byte[] authentication;

    @Column(updatable = false,nullable = false)
    @Size(max = 256)
    private String refreshToken;

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

    public String getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
