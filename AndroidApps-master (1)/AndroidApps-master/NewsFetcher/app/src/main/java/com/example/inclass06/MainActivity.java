package com.example.inclass06;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/*
Assignment : # InClass 06
Group: Groups1 41
NAME: AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String[] listitems = {"Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"};
    private ImageView articleimage;
    private ArrayList<Source> articles = new ArrayList<>();
    private TextView title, publishedat, description, pagination, showselection;
    private ProgressBar pb;
    int index = 0;

    ImageView prev, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleimage = findViewById(R.id.articleimage);
        title = findViewById(R.id.title);
        publishedat = findViewById(R.id.published);
        description = findViewById(R.id.description);
        pagination= findViewById(R.id.pagination);
        pb = findViewById(R.id.progressBar);
        showselection = findViewById(R.id.showcategories);

        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);

        prev.setAlpha(50);
        next.setAlpha(50);

        pb.setVisibility(View.INVISIBLE);
        visibility(false);



        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose a Keyword");

                builder.setItems(listitems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!isConnected()) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        visibility(false);
                        index = 0;
                        articles.clear();
                        showselection.setText(listitems[which]);
                        new GetJSON().execute("https://newsapi.org/v2/top-headlines?category=" + listitems[which] + "&apiKey=a717dffdd6074b95be6a2c9e7fc2d841");
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

            }

        });

        prev.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(articles.size() == 1 || articles.size() == 0){
            return;
        }

        if(!isConnected()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if(v.getId() == R.id.prev){
            index = index - 1;
            if(index < 0){
                index = index + articles.size();
            }
        }
        else if(v.getId() == R.id.next){
            //index = (index + 1)%imagelist.size();
            index = index + 1;
            if(index == articles.size()){
                index = 0;
            }
        }

        Source article = articles.get(index);

        title.setText(article.title);
        description.setText(article.description == "null" ? "No Description Found" : article.description);
        publishedat.setText(article.publishedat);
        Picasso.get().load(article.imageurl).into(articleimage);
        pagination.setText("" + (index+1) + " out of " + articles.size());

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

    void visibility(Boolean b) {
        if(!b) {
            description.setVisibility(View.INVISIBLE);
            pagination.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            publishedat.setVisibility(View.INVISIBLE);
            articleimage.setVisibility(View.INVISIBLE);
        }
        else {
            description.setVisibility(View.VISIBLE);
            pagination.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            publishedat.setVisibility(View.VISIBLE);
            articleimage.setVisibility(View.VISIBLE);
        }
    }


    class GetJSON extends AsyncTask<String, Void, ArrayList<Source>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
            protected ArrayList<Source> doInBackground(String... params) {
                StringBuilder stringBuilder = new StringBuilder();
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                ArrayList<Source> result = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    /*reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();*/

                        String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                        if(json.equals("")){
                            return null;
                        }

                        JSONObject root = new JSONObject(json);
                        JSONArray articlesJSONArray = root.getJSONArray("articles");
                        for (int i = 0; i < 20; i++) {
                            JSONObject articleJson = articlesJSONArray.getJSONObject(i);

                            Source article = new Source();

                            article.title = articleJson.getString("title");
                            article.publishedat = articleJson.getString("publishedAt");
                            article.imageurl = articleJson.getString("urlToImage");
                            article.description = articleJson.getString("description");

                            articles.add(article);


                        /*Person person = new Person();
                        person.name = personJson.getString("name");
                        person.id = personJson.getLong("id");
                        person.age = personJson.getInt("age");

                        JSONObject addressJson = personJson.getJSONObject("address");

                        Address address = new Address();
                        address.line1 = addressJson.getString("line1");
                        address.city = addressJson.getString("city");
                        address.state = addressJson.getString("state");
                        address.zip = addressJson.getString("zip");

                        person.address = address;
                        result.add(person);*/
                        }

                        result = articles;

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<Source> resultobj) {

                if (resultobj != null) {
                    Log.d("demo", resultobj.toString());
                    title.setText(resultobj.get(0).title);
                    description.setText(resultobj.get(0).description == "null" ? "No Description Found" : resultobj.get(0).description);
                    publishedat.setText(resultobj.get(0).publishedat);
                    Picasso.get().load(resultobj.get(0).imageurl).into(articleimage);
                    pagination.setText("" + 1 + " out of " + resultobj.size());

                    visibility(true);

                    if(articles.size() == 1 || articles.size() == 0){
                        prev.setAlpha(50);
                        next.setAlpha(50);
                    }
                    else {
                        prev.setAlpha(255);
                        next.setAlpha(255);
                    }

                    pb.setVisibility(View.INVISIBLE);

                    //new GetImage().execute(result);
                } else {
                    Toast.makeText(MainActivity.this, "No articles found", Toast.LENGTH_SHORT).show();
                    Log.d("demo", "null result");
                }
            }

        }

    /*private class GetImage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... strings) {

            String result = null;
            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(strings[0]);
                //System.out.println("calling getimage: " + strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //fetchedimage = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                    //result = IOUtils.toString(httpURLConnection.getInputStream(), "UTF8");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return null;
        }
    }*/

}
