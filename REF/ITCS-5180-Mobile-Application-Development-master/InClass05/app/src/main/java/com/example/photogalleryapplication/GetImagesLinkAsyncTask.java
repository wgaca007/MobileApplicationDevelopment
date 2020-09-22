package com.example.photogalleryapplication;

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

public class GetImagesLinkAsyncTask extends AsyncTask<String, Void, LinkedList<String>> {
    IData2 iData;

    public GetImagesLinkAsyncTask(GetImagesLinkAsyncTask.IData2 iData) {
        this.iData = iData;
    }

    @Override
    protected void onPostExecute(LinkedList<String> strings) {
        iData.handleListData2(strings);
    }

    @Override
    protected LinkedList<String> doInBackground(String... strings) {
        LinkedList<String> keywords = new LinkedList<>();
        HttpURLConnection connection = null;
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            URL url = new URL("http://dev.theappsdr.com/apis/photos/index.php?keyword=" + strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(";");
            }
            String str = stringBuilder.toString();
            Log.d("String builder", "This: " + stringBuilder.toString());
            String[] arrOfStr = str.split(";");

            for (String a : arrOfStr) {
                Log.d("Added link:", a);
                keywords.add(a);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return keywords;
    }

    public static interface IData2 {
        public void handleListData2(LinkedList<String> data);

        public void updateProgress(int progress);
    }
}
