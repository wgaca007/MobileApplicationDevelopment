package com.example.photogalleryapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class GetImageAsync extends AsyncTask<String, Void, Void> {
    ImageView imageView;
    ProgressBar progressBar;
    Bitmap bitmap = null;
    IData3 id;

    public GetImageAsync(ImageView iv, GetImageAsync.IData3 id, ProgressBar pro) {
        imageView = iv;
        this.id = id;
        this.progressBar = pro;
    }

    @Override
    protected void onPreExecute() {
        imageView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection = null;
        bitmap = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressBar.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        if (bitmap != null && imageView != null) {
            imageView.setImageBitmap(bitmap);
            Log.d("Location", "I'm in class");
            id.handleListData3(0);
        }
    }

    public static interface IData3 {
        public void handleListData3(int i);
        public void updateProgress(int progress);
    }
}
