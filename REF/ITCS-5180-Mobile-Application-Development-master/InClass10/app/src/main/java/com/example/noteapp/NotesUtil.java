package com.example.noteapp;

import java.io.Serializable;

public class NotesUtil implements Serializable {

    String note_id, user_id, note_text;

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNote_text() {
        return note_text;
    }

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    @Override
    public String toString() {
        return "NotesUtil{" +
                "note_id='" + note_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", note_text='" + note_text + '\'' +
                '}';
    }
}
