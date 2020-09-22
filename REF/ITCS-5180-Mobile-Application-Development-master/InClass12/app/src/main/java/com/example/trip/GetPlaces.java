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

public class GetPlaces extends AsyncTask<String, Void, ArrayList<Places>> {
    IData2 iData2;

    public GetPlaces(IData2 iData) {
        this.iData2 = iData;
    }

    @Override
    protected void onPostExecute(ArrayList<Places> strings) {
        iData2.handleData2(strings);
    }

    @Override
    protected ArrayList<Places> doInBackground(String... strings) {
        HttpURLConnection connection = null;
        ArrayList<Places> result = new ArrayList<>();

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                JSONObject root = new JSONObject(json);

                JSONArray resultdetails = root.getJSONArray("results");
                for (int i =0; i < resultdetails.length(); i++) {
                    Places places = new Places();
                    JSONObject candidates = resultdetails.getJSONObject(i);

                    places.setPlaceName(candidates.getString("name"));

                    JSONObject geometry = candidates.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");

                    places.setLati(location.getString("lat"));
                    places.setLogi(location.getString("lng"));
                    places.setChecked(false);
                    result.add(places);
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

    public static interface IData2{
        public void handleData2(ArrayList<Places> data);
    }
}
