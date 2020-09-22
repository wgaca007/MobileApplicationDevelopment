package com.example.photogalleryapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class GetKeywordAsyncTask extends AsyncTask<String, Void, LinkedList<String>> {
    IData iData;

    public GetKeywordAsyncTask(IData iData) {
        this.iData = iData;
    }

    @Override
    protected void onPostExecute(LinkedList<String> strings) {
        iData.handleListData(strings);
    }

    @Override
    protected LinkedList<String> doInBackground(String... strings) {
        LinkedList<String> keywords = new LinkedList<>();
        HttpURLConnection connection = null;
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                Log.d("Connection MyApp", "Connection URL: " + strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                Log.d("Connection MyApp", "Connection Established");
                inputStream = connection.getInputStream();
                Log.d("Connection MyApp", "Read");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String str = stringBuilder.toString();
            String[] arrOfStr = str.split(";");

            for (String a : arrOfStr)
                keywords.add(a);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return keywords;
    }

    public static interface IData {
        public void handleListData(LinkedList<String> data);

        public void updateProgress(int progress);
    }
}