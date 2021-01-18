package com.example.explorev3.pojo;

public class User {

    public String name, email, avatar;

    public User(){}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String avatar) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
    }


}
