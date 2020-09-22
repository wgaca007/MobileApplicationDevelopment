package com.example.recipepuppy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetDataAsync extends AsyncTask<String, Void, ArrayList<Recipe>>{
    setDataInterface myInterface;
    Context context;


    public GetDataAsync(setDataInterface myInterface, Context context) {

        this.myInterface = myInterface;
        this.context = context;

    }

    @Override
    protected void onPreExecute() {

//        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> result) {
//        super.onPostExecute(tracks);
        if (result.size() > 0) {
            Log.d("result", result.toString());
            myInterface.setData(result);
        }
        else {
            Log.d("demo", "null result");
        }
    }

    @Override
    protected ArrayList<Recipe> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<Recipe> result = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                JSONObject root = new JSONObject(json);


                JSONArray rootJSONArray = root.getJSONArray("results");

                for (int i = 0; i < rootJSONArray.length(); i++) {

                    JSONObject recipeJSON = rootJSONArray.getJSONObject(i);
                    Recipe recipe = new Recipe();

                    recipe.myurl = recipeJSON.getString("href");
                    recipe.ingredients = recipeJSON.getString("ingredients");
                    recipe.image = recipeJSON.getString("thumbnail");
                    recipe.name = recipeJSON.getString("title");

                    result.add(recipe);
                }
            }
        } catch (Exception e) {
            //Handle Exceptions
        } finally {
            //Close the connections
        }
        return result;
    }

    public interface setDataInterface{
        public void setData(ArrayList<Recipe> myResult);
    }
}
