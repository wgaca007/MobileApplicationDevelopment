package com.example.newsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class GetDataAsync extends AsyncTask<String, Void, ArrayList<Articles>> {

    setDataInterface myInterface;

    public GetDataAsync(setDataInterface myInterface) {
        this.myInterface = myInterface;
    }

    @Override
    protected ArrayList<Articles> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<Articles> result = new ArrayList<Articles>();
        try {
            URL url = new URL(params[0]);
            Log.d("URL", String.valueOf(url));

            connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
// if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
            JSONObject root = new JSONObject(json);
            JSONArray articles =  root.getJSONArray("articles");


            for(int i = 0; i < articles.length(); i++){
                JSONObject articlesJson = articles.getJSONObject(i);
                Articles article = new Articles();
                article.title = articlesJson.getString("title");
                article.description = articlesJson.getString("description");
                article.urlToImage = articlesJson.getString("urlToImage");
                article.publishedAt = articlesJson.getString("publishedAt");
//                Log.d("articles", String.valueOf(article));
                result.add(article);
                Log.d("articles", String.valueOf(article));
                }

//            }
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


    @Override
    protected void onPreExecute() {
//        super.onPreExecute();
        myInterface.startProcessing();

    }

    @Override
    protected void onPostExecute(ArrayList<Articles> result) {
        if (result.size() > 0) {
            Log.d("result", String.valueOf(result));
            myInterface.finishProcessing();
            myInterface.setData(result);


        } else {
            Log.d("demo", "null result");

        }
    }

    public interface setDataInterface{
        public void startProcessing();
        public void finishProcessing();
        public void setData(ArrayList<Articles> myResult);
    }
}
