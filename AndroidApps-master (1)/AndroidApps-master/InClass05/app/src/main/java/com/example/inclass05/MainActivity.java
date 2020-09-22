package com.example.inclass05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/*
Assignment : # InClass 05
Group: Groups1 41
NAME: AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button go;
    private ImageView currentimage ,prev, next;
    private TextView selectedkeyword;
    private ProgressBar pb;
    private Bitmap fetchedimage = null;
    private ArrayList<String>imagelist = new ArrayList<String>();
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentimage = findViewById(R.id.resultimage);
        selectedkeyword = findViewById(R.id.search);

        pb = findViewById(R.id.progressBar);

        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);

        prev.setAlpha(50);
        next.setAlpha(50);


        prev.setOnClickListener(this);
        next.setOnClickListener(this);

        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isConnected()) {
                    imagelist.clear();
                    new Getkeywords().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
                }
                else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        String imageurl = null;

        if(imagelist.size() == 0){
            return;
        }

        if(!isConnected()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if(v.getId() == R.id.prev){
            index = index - 1;
            if(index < 0){
                index = imagelist.size() - 1;
            }
        }
        else if(v.getId() == R.id.next){
            //index = (index + 1)%imagelist.size();
            index = index + 1;
            if(index >= imagelist.size()){
                index = 0;
            }
        }
        System.out.println("next image = " + index);
        imageurl = imagelist.get(index);
        new GetImage().execute(imageurl);
    }

    private class Getkeywords extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {

            //String[] listItems = {"one", "two", "three", "four", "five"};
            final String[] listItems = result.split(";");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose a Keyword");

            builder.setItems(listItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*Toast.makeText(MainActivity.this, "Position: " + which + " Value: " + listItems[which], Toast.LENGTH_LONG).show();*/
                    if(isConnected()) {
                        selectedkeyword.setText(listItems[which]);
                        new GetImageURLS(listItems[which]).execute("http://dev.theappsdr.com/apis/photos/index.php");
                    }
                    else {
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }


        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader bf = null;
            String result = null;

            try {
                url = new URL(strings[0]);
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
    }

    private class GetImageURLS extends AsyncTask<String, Void, String> {

        private String selecteditem;

        public GetImageURLS(String selecteditem) {
                this.selecteditem = selecteditem;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("")) {
                currentimage.setVisibility(View.INVISIBLE);
                prev.setAlpha(50);
                next.setAlpha(50);
                Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();
                return;
            }

            String list[] = s.split("\n");

            System.out.println("list is:");
            System.out.println(list);


            for(String k :  list) {
                imagelist.add(k);
            }
            new GetImage().execute(imagelist.get(0));
        }


        @Override
        protected String doInBackground(String... strings) {

            String result= null;
            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(strings[0] + "?keyword=" + selecteditem);
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

    private class GetImage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            currentimage.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb.setVisibility(View.INVISIBLE);

            currentimage.setVisibility(View.VISIBLE);
            currentimage.setImageBitmap(fetchedimage);

            prev.setAlpha(255);
            next.setAlpha(255);
        }

        @Override
        protected String doInBackground(String... strings) {

            String result= null;
            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(strings[0]);
                //System.out.println("calling getimage: " + strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    fetchedimage = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                    //result = IOUtils.toString(httpURLConnection.getInputStream(), "UTF8");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
            }
            return null;
        }
    }



}
