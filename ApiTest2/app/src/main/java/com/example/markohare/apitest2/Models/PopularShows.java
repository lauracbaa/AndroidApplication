package com.example.markohare.apitest2.Models;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.markohare.apitest2.Models.TvShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mark O'Hare on 06/04/2017.
 *
 * Returns current popular shows
 * Works similarly to the search JSONTask method, except it iterates over a selection of objects
 * from a JSONArray
 */

public class PopularShows extends AsyncTask<String, String, JSONArray>{

    private final String REQUEST_POPULAR="https://api.themoviedb.org/3/discover/tv?api_key=872b836efb69a145906d151fc7e865d1&language=en-UK&sort_by=popularity.desc&page=1&timezone=UK/London&include_null_first_air_dates=false";

    //-------Interface to handle the result of the request-------//
    public interface AsyncResponse{
        void processFinish(JSONArray result);
    }

    //Interface variable for constructor
    public AsyncResponse delegate = null;

    //When instantiating object, AsyncResponse should be passed in as 'new AsyncResponse()'
    //This will automatically generate the 'processFinish' method
    public PopularShows(AsyncResponse delegate){
        this.delegate = delegate;
    }

    //Standard AsyncTask methods that build your TvShow object based on the URL request
    @Override
    protected JSONArray doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try{

            URL gettvShowsUrl = new URL(params[0]);
            connection = (HttpURLConnection) gettvShowsUrl.openConnection();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line = "";

            while((line = reader.readLine())!=null){
                buffer.append(line);
            }

            String JSONTvShow = buffer.toString();

            JSONObject parentObj = new JSONObject(JSONTvShow);
            JSONArray popShows = parentObj.getJSONArray("results");

            return popShows;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{

            if(connection!=null)
            {
                connection.disconnect();
            }
            try {
                if(reader !=null)
                {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray result){
        super.onPostExecute(result);
        Log.d("JSON_STRING", result.toString());
        delegate.processFinish(result);
    }


}

