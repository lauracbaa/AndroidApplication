package com.example.markohare.apitest2;


import android.os.AsyncTask;
import android.util.Log;

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
 * Searches for a TV Show via a key word
 * Returns minimal info but can be used to retrieve the ID which can then be used to get more
 * info
 *
 */

public class SearchTVShow extends AsyncTask<String, String, TvShow> {

    private final String REQUEST_KEYWORD_SEARCH = "https://api.themoviedb.org/3/search/tv?api_key=872b836efb69a145906d151fc7e865d1&language=en-UK&query=";

    //-------Interface to handle the result of the request-------//
    public interface AsyncResponse{
        void processFinish(TvShow result);
    }

    //Interface variable for constructor
    public AsyncResponse delegate = null;

    //When instantiating object, AsyncResponse should be passed in as 'new AsyncResponse()'
    //This will automatically generate the 'processFinish' method
    public SearchTVShow(AsyncResponse delegate){
        this.delegate = delegate;
    }

    //Standard AsyncTask methods that build your TvShow object based on the URL request
    @Override
    protected TvShow doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        TvShow tvShow = new TvShow();
        String keywordSearch = REQUEST_KEYWORD_SEARCH + params[0].replaceAll(" ","%20");

        try{

            URL gettvShowsUrl = new URL(keywordSearch);
            connection = (HttpURLConnection) gettvShowsUrl.openConnection();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line = "";

            while((line = reader.readLine())!=null){
                buffer.append(line);
            }

            String JSONTvShow = buffer.toString();

            Log.d("JSON_CONTENTS", JSONTvShow);

            JSONObject parentObj = new JSONObject(JSONTvShow);
            JSONArray searchResultsArr = parentObj.getJSONArray("results");
            JSONObject searchResultsJSON = searchResultsArr.getJSONObject(0);

            tvShow.setName(searchResultsJSON.getString("name"));
            tvShow.setId(searchResultsJSON.getInt("id"));
            tvShow.setBackdropPath(searchResultsJSON.getString("backdrop_path"));
            tvShow.setOverview(searchResultsJSON.getString("overview"));
            tvShow.setPopularity(searchResultsJSON.getDouble("popularity"));
            tvShow.setPosterPath(searchResultsJSON.getString("poster_path"));


            return tvShow;

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
    protected void onPostExecute(TvShow result){
        super.onPostExecute(result);

        delegate.processFinish(result);
    }


}


