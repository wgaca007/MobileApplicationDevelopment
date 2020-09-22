package com.example.musicsearchapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class GetSongs extends AsyncTask<String, Void, ArrayList<TrackDetails>> {

    IData iData;

    public GetSongs(IData iData) {
        this.iData = iData;
    }

    @Override
    protected ArrayList<TrackDetails> doInBackground(String... strings) {
        HttpURLConnection connection = null;
        ArrayList<TrackDetails> result = new ArrayList<>();
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                LinkedList<TrackDetails> my_linked = new LinkedList<>();
                JSONObject root = new JSONObject(json);

                JSONArray trackDetails = root.getJSONArray("results");

                for(int i = 0; i < trackDetails.length(); i++)
                {
                    JSONObject article = trackDetails.getJSONObject(i);
                    TrackDetails track = new TrackDetails();

                    track.setTrackName(article.getString("trackName"));
                    track.setGenre(article.getString("primaryGenreName"));
                    track.setArtist(article.getString("artistName"));
                    track.setAlbum(article.getString("collectionName"));
                    track.setTrack_Price(article.getString("trackPrice"));
                    track.setAlbum_Price(article.getString("collectionPrice"));
                    track.setTrackViewUrl(article.getString("artworkUrl100"));

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String dateInString = article.getString("releaseDate");
                    Date date = formatter.parse(dateInString);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    String calendarMonth;
                    if((cal.get(Calendar.MONTH) + 1) < 10)
                        calendarMonth = "0" + (cal.get(Calendar.MONTH) + 1);
                    else
                        calendarMonth = "" + (cal.get(Calendar.MONTH) + 1);
                    String formatedDate = calendarMonth + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.YEAR);
                    track.setReleaseDate(formatedDate);
                    //Log.d("demo", "" + formatedDate);

                    result.add(track);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Log.d("demo", ""+result.get(0).toString());
        return result;

    }

    @Override
    protected void onPostExecute(ArrayList<TrackDetails> strings) {
        iData.handleData(strings);
    }

    public static interface IData{
        public void handleData(ArrayList<TrackDetails> data);
    }
}
