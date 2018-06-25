package com.lu.aptdemo.bean;

import java.io.Serializable;

/**
 * @Author: luqihua
 * @Time: 2018/6/6
 * @Description: UserInfo
 */

public class UserInfo implements Serializable{
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
