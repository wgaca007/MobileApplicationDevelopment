package com.example.inclass09;

public class UserInfo {
    String name, id;

    public UserInfo(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
