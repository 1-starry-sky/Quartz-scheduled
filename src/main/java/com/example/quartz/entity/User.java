package com.example.quartz.entity;

import java.io.Serializable;

/**
 * 用户
 */
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String userName;// 用户名

    private String password;// 密码

    private String address;// 地址

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
