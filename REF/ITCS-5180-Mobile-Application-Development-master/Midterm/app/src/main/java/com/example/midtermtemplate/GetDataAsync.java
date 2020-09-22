package com.example.midtermtemplate;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetDataAsync extends AsyncTask<String, Void, ArrayList<Track>> {
    setDataInterface myInterface;

    public GetDataAsync(setDataInterface myInterface) {
        this.myInterface = myInterface;
    }

    @Override
    protected void onPreExecute() {

        myInterface.startProcessing();
//        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Track> result) {
//        super.onPostExecute(tracks);
        if (result.size() > 0) {
            Log.d("result", String.valueOf(result));
            myInterface.finishProcessing();
            myInterface.setData(result);


        }
        else {
            Log.d("demo", "null result");
        }
    }

    @Override
    protected ArrayList<Track> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<Track> result = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                JSONObject root = new JSONObject(json);
                JSONObject message = (JSONObject) root.get("message");
                JSONObject body = (JSONObject) message.get("body");

                JSONArray rootJSONArray = body.getJSONArray("track_list");

                for (int i = 0; i < rootJSONArray.length(); i++) {

                    JSONObject tj = rootJSONArray.getJSONObject(i);

                    JSONObject trackJson = tj.getJSONObject("track");

                    Track track = new Track();

                    track.songName = trackJson.getString("track_name");
                    track.albumName = trackJson.getString("album_name");
                    track.artistName = trackJson.getString("artist_name");


                    //2013-05-08T16:51:12Z
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss'Z'");
                    DateFormat outputFormat = new SimpleDateFormat("mm-dd-yyyy");

                    String inputDateStr=trackJson.getString("updated_time");;
                    Date my_date = inputFormat.parse(inputDateStr);
                    track.date = outputFormat.format(my_date);


                    //-------------------//

                    track.trackUrl = trackJson.getString("track_share_url");

                    //-------------------//

                    result.add(track);
                }
            }
        } catch (Exception e) {
            //Handle Exceptions
        } finally {
            //Close the connections
        }
        return result;
    }

    public interface setDataInterface{
        public void startProcessing();
        public void finishProcessing();
        public void setData(ArrayList<Track> myResult);
    }
}
