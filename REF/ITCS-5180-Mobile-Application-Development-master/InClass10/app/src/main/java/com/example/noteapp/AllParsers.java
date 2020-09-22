package com.example.noteapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllParsers {

    static public class Parser {
        static public class LoginSignupJsonParser {
            static LoginUtil parseLogin(String in) throws JSONException {
                JSONObject root = new JSONObject(in);
                LoginUtil logInDetails = new LoginUtil();
                if (root.has("auth"))
                    logInDetails.setAuth(root.getBoolean("auth"));
                if (root.has("token"))
                    logInDetails.setToken(root.getString("token"));
                return logInDetails;
            }
        }

        static public class MeParser {
            static MeUtil parseMe(String in) throws JSONException {
                JSONObject root = new JSONObject(in);
                MeUtil meDetails = new MeUtil();
                if (root.has("name"))
                    meDetails.setUser_name(root.getString("name"));
                return meDetails;
            }
        }

        static public class NoteParser {
            static ArrayList<NotesUtil> parseNotes(String in) throws JSONException {
                JSONObject root = new JSONObject(in);
                ArrayList<NotesUtil> notes = new ArrayList();

                JSONArray rootJsonArray = root.getJSONArray("notes");

                for (int idx = 0; idx < rootJsonArray.length(); idx++) {
                    JSONObject noteJSON = rootJsonArray.getJSONObject(idx);
                    NotesUtil note = new NotesUtil();
                    if (noteJSON.has("_id"))
                        note.setNote_id(noteJSON.getString("_id"));

                    if (noteJSON.has("userId"))
                        note.setUser_id(noteJSON.getString("userId"));

                    if (noteJSON.has("text"))
                        note.setNote_text(noteJSON.getString("text"));

                    notes.add(note);
                }

                return notes;
            }
        }

    }
}
