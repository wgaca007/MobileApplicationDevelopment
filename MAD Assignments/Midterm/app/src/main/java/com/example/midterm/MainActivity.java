package com.example.midterm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<City> cityArrayList = new ArrayList<>();
   public static String selectedcity = "selectedcity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listView);
        String cities = loadJSONFromAsset(MainActivity.this);
        JSONObject root = null;
        try {
            root = new JSONObject(cities);
            JSONArray articlesJSONArray = root.getJSONArray("data");
            for(int i=0;i < articlesJSONArray.length(); i++){

                JSONObject articleJSON = articlesJSONArray.getJSONObject(i);

                cityArrayList.add(new City(articleJSON.getString("city"), articleJSON.getString("country")));
                Log.d("demo",""+articleJSON.getString("city")+"");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

       CityAdapter cityadapter = new CityAdapter(MainActivity.this,R.layout.cities, android.R.id.text1, cityArrayList);
        listView.setAdapter(cityadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isConnected()){
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(MainActivity.this, WeatherActivity.class);
                    City city = (City) parent.getItemAtPosition(position);
                    i.putExtra(selectedcity, (Parcelable) city);
                    startActivity(i);
                }
            }
        });

    }
    private static class CityAdapter extends ArrayAdapter {

        public CityAdapter(@NonNull Context context, int cities, int resource, ArrayList<City> cityArrayList) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            City city = (City)getItem(position);
            ViewHolder viewHolder;

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.cities, parent, false);
                viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.cardView).findViewById(R.id.cityView).findViewById(R.id.city),(TextView) convertView.findViewById(R.id.cardView).findViewById(R.id.cityView).findViewById(R.id.country));
                convertView.setTag(viewHolder);

            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.city.setText(city.city == "null" ? "No city Found" : city.city);
            Log.d("demo",""+city.city);
            viewHolder.country.setText(city.country == "null" ? "No country Found" : city.country);
            return convertView;
        }

        private class ViewHolder{
            TextView city,country;

            public ViewHolder(TextView city, TextView country) {
                this.city = city;
                this.country = country;

            }
        }
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.cities);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

  /*  if(!isConnected()){
        pb.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }
        else {
        pb.setVisibility(View.VISIBLE);
        new GetSources().execute("https://newsapi.org/v2/sources?apiKey=a717dffdd6074b95be6a2c9e7fc2d841");
    }
}*/

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        if(network != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }
        return false;

    }

 /*   private class GetSources extends AsyncTask<String, Void, ArrayList<Source>>{

        @Override
        protected ArrayList<Source> doInBackground(String... strings) {
            HttpURLConnection connection = null;

            URL url = null;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if(json.equals("")){
                        Toast.makeText(MainActivity.this, "No Sources Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    JSONArray sourcesJSONArray = root.getJSONArray("sources");

                    for(int i=0;i < sourcesJSONArray.length(); i++){

                        JSONObject sourceJSON = sourcesJSONArray.getJSONObject(i);

                        sourceslist.add(new Source(sourceJSON.getString("id"), sourceJSON.getString("name")));

                    }

                    return sourceslist;

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
        protected void onPostExecute(ArrayList<Source> sources) {
            super.onPostExecute(sources);
            pb.setVisibility(View.INVISIBLE);
            ArrayAdapter<Source> sourcesadapter = new ArrayAdapter<Source>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, sourceslist);
            sourceslistview.setAdapter(sourcesadapter);

            sourceslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!isConnected()){
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent i = new Intent(MainActivity.this, NewsActivity.class);
                        Source source = (Source) parent.getItemAtPosition(position);
                        i.putExtra(selectedsource, source);
                        startActivity(i);
                    }
                }
            });


        }
    }
*/
}
