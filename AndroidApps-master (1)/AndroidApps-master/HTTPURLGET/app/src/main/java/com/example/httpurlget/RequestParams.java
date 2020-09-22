package com.example.httpurlget;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestParams {

    private Map<String, String> params = new HashMap<String, String>();
    private StringBuilder sb = new StringBuilder();

    public RequestParams addParameter(String key, String value){
        try {
            params.put(key, URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return this;
    }

    public String getEncodedParameters() {

        for(String key : params.keySet()){
            if(sb.length() > 0){
                sb.append("&");
            }
            sb.append(key + "=" + params.get(key));
        }
        System.out.println("Encoded params = " + sb.toString());
        return sb.toString();
    }

    public String getEncodedURL(String url){
        String encodedurl = url + "?" + getEncodedParameters();
        System.out.println("URL = " + encodedurl);
        return encodedurl;
    }
}
