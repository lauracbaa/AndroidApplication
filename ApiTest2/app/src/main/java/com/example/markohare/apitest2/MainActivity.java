package com.example.markohare.apitest2;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.markohare.apitest2.Models.PopularShows;

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
    private TextView tv;

    final String API_KEY = "api_key=872b836efb69a145906d151fc7e865d1";

    //----IMPORTANT!! This is using my api key and specific tv show id to find Game of Thrones!!
    //----BE SURE TO CHECK THE DOCUMENTATION OF HOW TO USE THE API ON thetvShowDB website!!
    final String REQUEST_TEST = "https://api.themoviedb.org/3/tv/1399?api_key=872b836efb69a145906d151fc7e865d1&language=en-UK";
    final String REQUEST_POPULAR="https://api.themoviedb.org/3/discover/tv?api_key=872b836efb69a145906d151fc7e865d1&language=en-UK&sort_by=popularity.desc&page=1&timezone=UK/London&include_null_first_air_dates=false";

    //----TV SHOW NEEDS TO BE APPENDED TO END OF REQUEST WITH EACH SPACE REPRESENTED AS A '%20'!!!
    final String REQUEST_KEYWORD_SEARCH = "https://api.themoviedb.org/3/search/tv?api_key=872b836efb69a145906d151fc7e865d1&language=en-US&query=";


    private RecyclerView recyclerView;
    private TvShowAdapter adapter;
    private List<TvShow> tvShowList;
    SQLiteDatabaseHandler db;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvShowList = new ArrayList<>();
        // Set up adapter to the RecycleView
        adapter = new TvShowAdapter(this, tvShowList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


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
        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config); // Do it on Application start
        try {
            // Draw the image at the top of the screen first
            Glide.with(this).load(R.drawable.tvcover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


        new PopularShows(new PopularShows.AsyncResponse() {
            @Override
            public void processFinish(JSONArray result) {
                // This functionality is only run when the call to the external film service has been
                // completed and the film data has been stored in the tvShow List array
                Log.d("TVSHOW_RESULT", result.toString());

                ImageLoader.getInstance().init(config); // Do it on Application start

                // Loop through the JSON array and add entries to the TvList
                for (int i = 0; i < result.length(); i++) {
                    try {
                        makeView(result, i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Inform adapter that data has changed in the cardview
                // This will drive the drawing of the dynamic data
                adapter.notifyDataSetChanged();
            }
        }).execute(REQUEST_POPULAR);


    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //               collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void makeView(JSONArray popShows, int index) throws JSONException {
        TvShow tvShow = new TvShow();
        JSONObject JSONpopShow = popShows.getJSONObject(index);

        Log.d("JSON_SHOW", JSONpopShow.toString());

        tvShow.setName(JSONpopShow.getString("name"));
        tvShow.setId(JSONpopShow.getInt("id"));
        tvShow.setBackdropPath(JSONpopShow.getString("backdrop_path"));
        tvShow.setOverview(JSONpopShow.getString("overview"));
        tvShow.setPopularity(JSONpopShow.getDouble("popularity"));
        tvShow.setPosterPath(tvShow.getIMG_POSTER_BASE_URL()+ JSONpopShow.getString("poster_path"));
        Log.d( "MAIN","makeView: PosterPath= " + tvShow.getPosterPath());

        tvShowList.add(tvShow);

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
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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


