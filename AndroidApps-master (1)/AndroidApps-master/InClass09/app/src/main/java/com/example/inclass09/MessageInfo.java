package com.example.inclass09;

import java.io.Serializable;

public class MessageInfo implements Serializable {

    String id, name, subject, message, createdat;

    public MessageInfo(String id, String name, String subject, String message, String createdat) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.message = message;
        this.createdat = createdat;
    }
}
