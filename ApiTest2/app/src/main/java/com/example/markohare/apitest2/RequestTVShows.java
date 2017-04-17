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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark O'Hare on 06/04/2017.
 *
 * JSON asyncronous task that finds a show by its ID
 * Returns more info related to show, such as season info etc
 *
 */

public class RequestTVShows extends AsyncTask<String, String, TvShow>{

    //--------Params to build URL request for online DB-----------//
    private String requestURL;
    private final String API_KEY = "?api_key=872b836efb69a145906d151fc7e865d1";
    private final String requestSuffix = "&language=en-UK";
    private final String requestPrefix = "https://api.themoviedb.org/3/tv/";

    //-------Interface to handle the result of the request-------//
    public interface AsyncResponse{
        void processFinish(TvShow result);
    }

    //Interface variable for constructor
    public AsyncResponse delegate = null;

    //When instantiating object, AsyncResponse should be passed in as 'new AsyncResponse()'
    //This will automatically generate the 'processFinish' method
    public RequestTVShows(AsyncResponse delegate){
        this.delegate = delegate;
    }


    //Standard AsyncTask methods that build your TvShow object based on the URL request
    @Override
    protected TvShow doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        TvShow tvShow = new TvShow();
        requestURL = requestPrefix+params[0]+API_KEY+requestSuffix;

        try{

            URL gettvShowsUrl = new URL(requestURL);
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
            JSONArray seasons = parentObj.getJSONArray("seasons");
            JSONArray JSONnetworks = parentObj.getJSONArray("networks");
            JSONArray JSONepRuntime = parentObj.getJSONArray("episode_run_time");
            JSONArray JSONgenres = parentObj.getJSONArray("genres");

            int[] epRuntime = new int[(JSONepRuntime.length())];
            String[] networks = new String[(JSONnetworks.length())];
            String[] genres = new String[(JSONgenres.length())];

            for(int i=0;i<networks.length;i++){
                networks[i] = JSONnetworks.optString(i);
            }

            for(int i = 0;i<genres.length;i++){
                genres[i] = JSONgenres.optString(i);
            }

            for(int i = 0;i<epRuntime.length;i++){
                epRuntime[i]= JSONepRuntime.optInt(i);
            }

            tvShow.setName(parentObj.getString("name"));
            tvShow.setId(parentObj.getInt("id"));
            tvShow.setBackdropPath(parentObj.getString("backdrop_path"));
            tvShow.setEpRuntime(epRuntime);
            tvShow.setGenres(genres);
            tvShow.setNetworks(networks);
            tvShow.setHomepage(parentObj.getString("homepage"));
            tvShow.setLastAirDate(parentObj.getString("last_air_date"));
            tvShow.setNoOfEpisodes(parentObj.getInt("number_of_episodes"));
            tvShow.setNoOfSeasons(parentObj.getInt("number_of_seasons"));
            tvShow.setOverview(parentObj.getString("overview"));
            tvShow.setPopularity(parentObj.getDouble("popularity"));
            tvShow.setPosterPath(parentObj.getString("poster_path"));

            List<TvShow.Season> seasonList = new ArrayList<>();

            for(int i=0; i< seasons.length();i++){
                JSONObject seasonObj = seasons.getJSONObject(i);
                TvShow.Season season = new TvShow.Season();
                season.setAirDate(seasonObj.getString("air_date"));
                season.setEpisodeCount(seasonObj.getInt("episode_count"));
                season.setId(seasonObj.getInt("id"));
                season.setPosterPath(seasonObj.getString("poster_path"));
                season.setNumber(seasonObj.getInt("season_number"));
                seasonList.add(season);
            }
            tvShow.setSeasons(seasonList);

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
        Log.d("POST_EXECUTE", result.getName());
        delegate.processFinish(result);
    }



}

