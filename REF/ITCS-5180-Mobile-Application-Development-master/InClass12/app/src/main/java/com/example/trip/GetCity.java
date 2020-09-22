package com.example.trip;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

public class GetCity extends AsyncTask<String, Void, City> {
    IData iData;

    public GetCity(IData iData) {
        this.iData = iData;
    }

    @Override
    protected void onPostExecute(City strings) {
        iData.handleData(strings);
    }

    @Override
    protected City doInBackground(String... strings) {
        HttpURLConnection connection = null;
        City result = new City();
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                JSONObject root = new JSONObject(json);

                JSONArray candidatesdetails = root.getJSONArray("candidates");
                for (int i =0; i < candidatesdetails.length(); i++) {
                    JSONObject candidates = candidatesdetails.getJSONObject(i);

                    result.setCityName(candidates.getString("name"));

                    JSONObject geometry = candidates.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");

                    result.setLatitude(location.getString("lat"));
                    result.setLongitude(location.getString("lng"));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    public static interface IData{
        public void handleData(City data);
    }
}
