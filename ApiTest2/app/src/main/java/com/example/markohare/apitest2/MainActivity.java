package com.example.markohare.apitest2;

import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.app.SearchManager;
import android.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.markohare.apitest2.Models.TvShow;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final String REQUEST_TEST = "https://api.themoviedb.org/3/tv/2343?api_key=872b836efb69a145906d151fc7e865d1&language=en-US";


    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
                .cacheOnDisk(true)
        .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .build();

        ImageLoader.getInstance().init(config); // Do it on Application start


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public class JSONTask extends AsyncTask<String, String, TvShow>{

        @Override
        protected TvShow doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            TvShow tvShow = new TvShow();

            try{

                URL getMoviesUrl = new URL(params[0]);
                connection = (HttpURLConnection) getMoviesUrl.openConnection();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";

                while((line = reader.readLine())!=null){
                    buffer.append(line);
                }

                String JSONTvShow = buffer.toString();

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

            TextView name = (TextView)findViewById(R.id.movieName);
            TextView noOfSeasons = (TextView)findViewById(R.id.movieNoSeasons);
            TextView popularity = (TextView)findViewById(R.id.moviePopularity);
            TextView overview = (TextView)findViewById(R.id.movieOverview);
            ImageView image = (ImageView)findViewById(R.id.imageTest);

            name.setText("Name: "+result.getName());
            noOfSeasons.setText("No. of Seasons: "+String.valueOf(result.getNoOfSeasons()));
            popularity.setText("Popularity rating: "+String.valueOf(result.getPopularity()));
            overview.setText("Overview: "+result.getOverview());


            //show information about TV show

            name.setVisibility(View.VISIBLE);
            noOfSeasons.setVisibility(View.VISIBLE);
            popularity.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            overview.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(result.getPosterPath(), image);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        
        searchView = (SearchView) searchItem.getActionView();searchView.setQueryHint("Search TV show...");

        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {


                new JSONTask().execute(REQUEST_TEST);

                return false;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if(id == R.id.action_refresh){
            new JSONTask().execute(REQUEST_TEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


