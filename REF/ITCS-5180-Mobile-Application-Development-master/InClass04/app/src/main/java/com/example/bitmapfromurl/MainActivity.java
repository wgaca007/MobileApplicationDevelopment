package com.example.bitmapfromurl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.button_AsyncTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView display_image = findViewById(R.id.imageView_DisplayImage);
                String URL = "https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg";

                display_image.setTag(URL);
                new BitMapFromURL1().execute(display_image);
            }
        });

        findViewById(R.id.button_Thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    handler = new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            Log.d("InHandler", "Activity Started");

                            ImageView my_view = findViewById(R.id.imageView_DisplayImage);
                            ProgressBar my_progress = findViewById(R.id.progressBar);


                            switch(msg.what){
                                case ThreadFromURL.STATUS_START:
                                    Log.d("InHandler", "Activity Started");
                                    my_view.setVisibility(View.INVISIBLE);
                                    my_progress.setVisibility(View.VISIBLE);
                                    break;

                                    case ThreadFromURL.STATUS_INPROGRESS:
                                        Log.d("InHandler", "Activity In Progress");
                                        my_view.setImageBitmap((Bitmap) msg.obj);
                                        break;

                                        case ThreadFromURL.STATUS_STOP:
                                            Log.d("InHandler", "Activity Stopped");
                                            my_view.setVisibility(View.VISIBLE);
                                            my_progress.setVisibility(View.INVISIBLE);
                                            break;
                            }
                            return false;
                        }
                    });
                    new Thread(new ThreadFromURL()).start();
            }
        });

    }

    public class BitMapFromURL1 extends AsyncTask<ImageView,Void,Bitmap>{

        ImageView imageView = null;
        ImageView mydisplay = findViewById(R.id.imageView_DisplayImage);
        ProgressBar my_bar = findViewById(R.id.progressBar);

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return getImageBitmap((String)imageView.getTag());
        }

        @Override
        protected void onPreExecute() {
            mydisplay.setVisibility(View.INVISIBLE);
            my_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            my_bar.setVisibility(View.INVISIBLE);
            mydisplay.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        private Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class ThreadFromURL implements Runnable{
        static final int STATUS_START = 0x00;
        static final int STATUS_INPROGRESS = 0x01;
        static final int STATUS_STOP = 0x02;

        @Override
        public void run() {
            Message start = new Message();
            start.what = STATUS_START;
            handler.sendMessage(start);
            String URL = "https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg ";

            Message progress = new Message();
            progress.what = STATUS_INPROGRESS;
            Bitmap my_image = getImageBitmap(URL);
            progress.obj = my_image;
            handler.sendMessage(progress);

            Message stop = new Message();
            stop.what = STATUS_STOP;
            handler.sendMessage(stop);
        }

        private Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
