package com.example.inclass09;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupLoginUtil {
public static class JsonParser{

    public static LoginResponse parseInfo(String in) throws JSONException {
        JSONObject root =new JSONObject();
    LoginResponse loginResponse = new LoginResponse();
if(root.has("status")){
    loginResponse.setStatus(root.getString("status"));
}
        if(root.has("message")){
            loginResponse.setMessage(root.getString("message"));
        }
        if(root.has("token")){
            loginResponse.setToken(root.getString("token"));
        }
        if(root.has("user_id")){
            loginResponse.setUser_id(root.getString("user_id"));
        }
        if(root.has("user_email")){
            loginResponse.setUser_email(root.getString("user_email"));
        }
        if(root.has("user_fname")){
            loginResponse.setUser_fname(root.getString("user_fname"));
        }
        if(root.has("user_lname")){
            loginResponse.setUser_lname(root.getString("user_lname"));
        }
        if(root.has("user_role")){
            loginResponse.setUser_role(root.getString("user_role"));
        }
        return loginResponse;
    }
}

}
