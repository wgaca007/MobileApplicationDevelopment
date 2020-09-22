package com.example.httpurlget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button getnoparams, getwithparams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getnoparams = findViewById(R.id.getnoparams);
        getwithparams = findViewById(R.id.getwithparams);

        getnoparams.setOnClickListener(this);
        getwithparams.setOnClickListener(this);

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        if(network != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }

        return false;

    }

    @Override
    public void onClick(View v) {
        if(isConnected()){
            Toast.makeText(MainActivity.this, "Conneccted", Toast.LENGTH_SHORT).show();
            if(v.getId() == R.id.getnoparams) {
                new GetDataAsync().execute("https://api.theappsdr.com/simple.php");
            }
            else if(v.getId() == R.id.getwithparams){
                RequestParams params = new RequestParams();
                params.addParameter("name", "AkhilChudarathil").addParameter("age", "21").addParameter("class", "Android");
                new GetDataParamsAsync(params).execute("https://api.theappsdr.com");
            }
        } else {
            Toast.makeText(MainActivity.this, "Not Conneccted", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetDataAsync extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader bf = null;
            String result = null;
            try {
                url = new URL(params[0]);
                connection =  (HttpURLConnection)url.openConnection();
                connection.connect();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
                }
               /* bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line="";

                while((line = bf.readLine()) != null) {
                    stringBuilder.append(line);
                }

                return stringBuilder.toString();*/



            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }

                if(bf != null){
                    try {
                        bf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetDataParamsAsync extends AsyncTask<String, Void, String> {
        private RequestParams mparams;

        public GetDataParamsAsync(RequestParams params) {
            mparams = params;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result= null;
            HttpURLConnection httpURLConnection = null;
            //Bitmap image = null;
            try {
                URL url = new URL(mparams.getEncodedURL(params[0]));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    //BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                    result = IOUtils.toString(httpURLConnection.getInputStream(), "UTF8");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
            }
            return result;

        }
    }
}
