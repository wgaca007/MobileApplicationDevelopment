package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.ClickListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private ImageButton addButton;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton=findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.recyclerView);
        addButton.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
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
    public void onPressListener() {

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(MainActivity.this,AddMeeting.class);
        startActivity(i);
    }
}

class GetNews extends AsyncTask<String, Void, ArrayList<String>> {

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        HttpURLConnection connection = null;

        URL url = null;
        try {
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                if(json.equals("")){
                    return null;
                }

                JSONObject root = new JSONObject(json);
                JSONArray articlesJSONArray = root.getJSONArray("articles");

                for(int i=0;i < articlesJSONArray.length(); i++){

                    JSONObject articleJSON = articlesJSONArray.getJSONObject(i);

                   // articles.add(new News(articleJSON.getString("author"), articleJSON.getString("title"), articleJSON.getString("url"), articleJSON.getString("urlToImage"), articleJSON.getString("publishedAt")));

                }

               // return articles;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<String> news) {

        //pb.setVisibility(View.INVISIBLE);

      /*NewsAdapter newsAdapter = new NewsAdapter(NewsActivity.this, R.layout.news_item, news);
        newslistview.setAdapter(newsAdapter);

        newslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News newsitem = (News) parent.getItemAtPosition(position);

            }
        });*/
    }
}
