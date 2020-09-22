package com.example.homework07;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class Chat {

    String userid,sender, message, chatid, textimageurl;
    Date timestamp;

    public Chat(String userid, String sender, String message, Date timestamp, String textimageurl) {
        this.userid = userid;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
        this.textimageurl = textimageurl;
    }

    public String getTextimageurl() {
        return textimageurl;
    }

    public void setTextimageurl(String textimageurl) {
        this.textimageurl = textimageurl;
    }

    public String getUserid() {
        return userid;
    }

    @Exclude
    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Chat(){

    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
