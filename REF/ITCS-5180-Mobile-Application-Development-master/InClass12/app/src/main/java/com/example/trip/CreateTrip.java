package com.example.trip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateTrip extends AppCompatActivity implements GetCity.IData, GetPlaces.IData2 {

    private static final String LOG_TAG = "PlacesAPI";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBb1rZUUJdPFTH7cUyTbd_tn2eWh1d6hrk";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    static String MUSIC_LACK_KEY = "LACK";
    public String date;
    public String city = null;
    final ArrayList<Places> currentSelectedItems = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayAdapter<String> adapter;
    AutoCompleteTextView autoCompleteTextView;
    private static ArrayList<Places> allAvailablePlaces;
    private static ArrayList<Places> finalSelectedPlaces = new ArrayList<>();
    Trips newtrip = new Trips();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count % 3 == 1) {
                    adapter.clear();
                    new GetDataAsync().execute("true");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            public void afterTextChanged(Editable s) {
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().isEmpty()) {
                    city = parent.getItemAtPosition(position).toString();
                    new GetCity(CreateTrip.this).execute("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + parent.getItemAtPosition(position) +
                            "&inputtype=textquery&fields=name,geometry&key=AIzaSyBb1rZUUJdPFTH7cUyTbd_tn2eWh1d6hrk&type=city_hall");
                }

            }
        });

        final TextView textViewDate = findViewById(R.id.tvDate);
        Button pickDate = (Button) findViewById(R.id.buttonPickDate);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = month + "/" + dayOfMonth + "/" + year;
                        Log.d("demo", date);
                        textViewDate.setText(date);
                        newtrip.setDate(date);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void handleData(final City data) {
        if (data != null) {
            newtrip.setCity(data);
            final String[] types = new String[]{"airport", "resturant", "amusement_park", "aquarium", "art_gallery", "bar", "cafe", "casino", "church", "museum", "night_club", "park"};
            ArrayList<String> category = new ArrayList<>();

            for (String s : types) {
                category.add(s);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
            Spinner spinYear = (Spinner) findViewById(R.id.category_spin);
            spinYear.setAdapter(adapter);

            String temp = "airport";

            new GetPlaces(CreateTrip.this).execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + data.getLatitude() + ","
                    + data.getLongitude() + "&radius=15000&type=" + temp + "&key=AIzaSyBb1rZUUJdPFTH7cUyTbd_tn2eWh1d6hrk");

            spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("demo", "" + parent.getItemAtPosition(position));
                    String selectedCategory = parent.getItemAtPosition(position).toString();
                    new GetPlaces(CreateTrip.this).execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + data.getLatitude() + ","
                            + data.getLongitude() + "&radius=15000&type=" + selectedCategory + "&key=AIzaSyBb1rZUUJdPFTH7cUyTbd_tn2eWh1d6hrk");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

//            AlertDialog.Builder category = new AlertDialog.Builder(CreateTrip.this);
//            category.setTitle("Select the type of place").setItems(types, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    String selectedCategory = types[which].toLowerCase();
//                    new GetPlaces(CreateTrip.this).execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + data.getLatitude() + ","
//                            + data.getLongitude() + "&radius=15000&type=" + selectedCategory + "&key=AIzaSyBb1rZUUJdPFTH7cUyTbd_tn2eWh1d6hrk");
//                }
//            });
//            final AlertDialog alertDialog = category.create();
//            alertDialog.show();

        }
    }

    @Override
    public void handleData2(ArrayList<Places> data) {
        allAvailablePlaces = data;
        recyclerView = findViewById(R.id.recyclerView_Places);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(CreateTrip.this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ContactAdapter2(CreateTrip.allAvailablePlaces, new ContactAdapter2.OnItemCheckListener() {
            @Override
            public void onItemCheck(Places item) {
                if (currentSelectedItems.size() >= 15)
                    Toast.makeText(CreateTrip.this, "Cannot add more places", Toast.LENGTH_LONG).show();
                else
                    currentSelectedItems.add(item);
            }

            @Override
            public void onItemUncheck(Places item) {
                currentSelectedItems.remove(item);
            }
        });
        recyclerView.setAdapter(mAdapter);

        findViewById(R.id.button_AddToTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedItems.size() > 0) {
                    finalSelectedPlaces = currentSelectedItems;

                    EditText trip_name = findViewById(R.id.editText_TripName);
                    newtrip.setTripName(trip_name.getText().toString());
                    newtrip.setTripPlaces(finalSelectedPlaces);

                    DatabaseReference ref2 = mDatabase.child("trips/");
                    String key = ref2.push().getKey();
                    newtrip.setTripID(key);
                    ref2.child(key).setValue(newtrip);

                    Toast.makeText(CreateTrip.this, "Added to trip successfully", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.button_ViewTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newtrip != null) {
                    Intent intent = new Intent(getBaseContext(), TripMap.class);
                    intent.putExtra(CreateTrip.MUSIC_LACK_KEY, newtrip);
                    startActivity(intent);
                }
            }
        });


    }


    class GetDataAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                ArrayList resultList = null;

                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                    StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                    sb.append("?key=" + API_KEY);

                    sb.append("&input=" + URLEncoder.encode(autoCompleteTextView.getText().toString(), "utf8"));

                    URL url = new URL(sb.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsonResults.append(buff, 0, read);
                    }
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "Error processing API URL", e);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error connecting to Places API", e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                ;

                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");


                resultList = new ArrayList(predsJsonArray.length());
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                    System.out.println("============================================================");
                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                    adapter.add(predsJsonArray.getJSONObject(i).getString("description"));

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}
