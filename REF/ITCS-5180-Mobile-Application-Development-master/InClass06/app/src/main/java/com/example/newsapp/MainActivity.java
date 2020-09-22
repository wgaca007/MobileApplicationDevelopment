package com.example.newsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetDataAsync.setDataInterface {

    ProgressDialog progressDialog;

    ArrayList<Articles> articles = new ArrayList<>();
    public static int idx = 0;
    public String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton ibP = (ImageButton) findViewById(R.id.ibPrev);
        ImageButton ibN = (ImageButton) findViewById(R.id.ibNext);
        ibN.setVisibility(View.INVISIBLE);
        ibP.setVisibility(View.INVISIBLE);

        ImageView imageView = findViewById(R.id.imageView);

        TextView tvCategory = findViewById(R.id.tvShowCategories);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvCounter = findViewById(R.id.tvCounter);
        TextView tvpublishedAt = findViewById(R.id.tvPublishedAt);
        TextView tvTitle = findViewById(R.id.tvTitle);


        tvCounter.setText("");
        tvCategory.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        tvDescription.setVisibility(View.INVISIBLE);
        tvpublishedAt.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);


        final ArrayList<String> items = new ArrayList<String>();
        items.add("Business");
        items.add("Entertainment");
        items.add("General");
        items.add("Health");
        items.add("Science");
        items.add("Sports");
        items.add("Technology");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Category")
        .setItems(items.toArray(new CharSequence[items.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCategory = items.get(which);

                TextView tvCategory = findViewById(R.id.tvShowCategories);
                tvCategory.setText(selectedCategory);

                String my_url = "https://newsapi.org/v2/top-headlines?country=us&category="+selectedCategory+"&apiKey=59f2c02694a24896a0aea1440932c8b9";
                new GetDataAsync(MainActivity.this).execute(my_url);

                Log.d("Alert Dialog Selected", selectedCategory);
                Log.d("Alert Dialog Selected", String.valueOf(which));
            }
        });

        final AlertDialog alertDialog = builder.create();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    alertDialog.show();
                }
                else{
                    Toast.makeText( MainActivity.this, "Check Internet", Toast.LENGTH_SHORT ).show();
                }
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void startProcessing() {

        Log.d("start processing", "Before PD");

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();

        Log.d("start processing", "After PD");

        ImageView imageView = findViewById(R.id.imageView);

        TextView tvCategory = findViewById(R.id.tvShowCategories);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvCounter = findViewById(R.id.tvCounter);
        TextView tvpublishedAt = findViewById(R.id.tvPublishedAt);
        TextView tvTitle = findViewById(R.id.tvTitle);

        tvCategory.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        tvDescription.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);

        Log.d("start processing", "After Init");

    }

    @Override
    public void finishProcessing() {
        progressDialog.dismiss();
    }

    @Override
    public void setData(ArrayList<Articles> myResult) {

        ImageButton ibP = (ImageButton) findViewById(R.id.ibPrev);
        ImageButton ibN = (ImageButton) findViewById(R.id.ibNext);
        ibN.setVisibility(View.VISIBLE);
        ibP.setVisibility(View.VISIBLE);

        this.articles = myResult;

        final ImageView imageView = findViewById(R.id.imageView);
        final TextView tvCounter = findViewById(R.id.tvCounter);
        tvCounter.setText(idx+1+" out of "+articles.size());
        TextView tvCategory = findViewById(R.id.tvShowCategories);
        final TextView tvDescription = findViewById(R.id.tvDescription);
        final TextView tvpublishedAt = findViewById(R.id.tvPublishedAt);
        final TextView tvTitle = findViewById(R.id.tvTitle);

        tvCategory.setVisibility(View.VISIBLE);
        tvpublishedAt.setVisibility(View.VISIBLE);
        tvDescription.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvCounter.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);

        tvTitle.setText(articles.get(idx).title);
        tvDescription.setText(articles.get(idx).description);
        tvpublishedAt.setText(articles.get(idx).publishedAt);
        Picasso.get().load(articles.get(idx).urlToImage).into(imageView);

        ibP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idx == 0){
                    idx = articles.size() - 1;
                    tvTitle.setText(articles.get(idx).title);
                    tvDescription.setText(articles.get(idx).description);
                    tvpublishedAt.setText(articles.get(idx).publishedAt);
                    Picasso.get().load(articles.get(idx).urlToImage).into(imageView);
                    tvCounter.setText(idx+1+" out of "+articles.size());
                }

                else{
                    idx -= 1;
                    tvTitle.setText(articles.get(idx).title);
                    tvDescription.setText(articles.get(idx).description);
                    tvpublishedAt.setText(articles.get(idx).publishedAt);
                    Picasso.get().load(articles.get(idx).urlToImage).into(imageView);
                    tvCounter.setText(idx+1+" out of "+articles.size());
                }

            }
        });

        ibN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idx == articles.size() - 1){
                    idx =0;
                    tvTitle.setText(articles.get(idx).title);
                    tvDescription.setText(articles.get(idx).description);
                    tvpublishedAt.setText(articles.get(idx).publishedAt);
                    Picasso.get().load(articles.get(idx).urlToImage).into(imageView);
                    tvCounter.setText(idx+1+" out of "+articles.size());
                }
                else{
                    idx += 1;
                    tvTitle.setText(articles.get(idx).title);
                    tvDescription.setText(articles.get(idx).description);
                    tvpublishedAt.setText(articles.get(idx).publishedAt);
                    Picasso.get().load(articles.get(idx).urlToImage).into(imageView);
                    tvCounter.setText(idx+1+" out of "+articles.size());

                }
            }
        });
    }
}