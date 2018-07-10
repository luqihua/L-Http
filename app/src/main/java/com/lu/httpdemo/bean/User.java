package com.lu.httpdemo.bean;

/**
 * Author: luqihua
 * Time: 2017/6/19
 * Description: User
 */

public class User {

    /**
     * name : luqihua
     * password : helloworld
     * age : 20
     */

    private String username;
    private String password;
    private int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
