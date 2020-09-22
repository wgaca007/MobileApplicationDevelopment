package com.example.photogalleryapplication;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements GetKeywordAsyncTask.IData, GetImagesLinkAsyncTask.IData2, GetImageAsync.IData3 {

    LinkedList<String> data;
    LinkedList<String> links;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Photo Gallery");

        new GetKeywordAsyncTask(MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/keywords.php");

    }

    public void handleData(LinkedList<String> data) {
        this.data = data;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Keywords")
                .setItems(data.toArray(new CharSequence[data.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void handleListData(final LinkedList<String> data) {
            this.data = data;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Keywords")
                .setItems(data.toArray(new CharSequence[data.size()]), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String checkitem = data.get(which);
                        TextView selectedKeyword = findViewById(R.id.textView);
                        selectedKeyword.setText(checkitem);
                        new GetImagesLinkAsyncTask(MainActivity.this).execute(checkitem);
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void handleListData3(int i) {
        Log.d("Interesting", "Ye kuch khass hai:" + links.get(1));
        ImageButton next = findViewById(R.id.button_Next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Next Button","In next button" + index);
                index++;
                if(index > 3)
                    index = 2;
                if (index <= 2 && index >= 0) {
                    new GetImageAsync((ImageView) findViewById(R.id.imageView), MainActivity.this, (ProgressBar) findViewById(R.id.progressBar)).execute(links.get(index));
                }
            }
        });

        ImageButton previous = findViewById(R.id.button_Previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Previous Button", "In previous button" + index);
                index--;
                if(index < 0)
                    index = 0;
                if(index >= 0 && index <= 2){
                    new GetImageAsync((ImageView) findViewById(R.id.imageView), MainActivity.this, (ProgressBar) findViewById(R.id.progressBar)).execute(links.get(index));
                }
            }
        });
    }

    @Override
    public void updateProgress(int progress) {

    }

    @Override
    public void handleListData2(LinkedList<String> data) {
        this.links = data;
        Log.d("Mharo photo", "Link of maharo photo: " + data.get(0));

        new GetImageAsync((ImageView) findViewById(R.id.imageView), MainActivity.this, (ProgressBar) findViewById(R.id.progressBar)).execute(data.get(0));
    }

}
