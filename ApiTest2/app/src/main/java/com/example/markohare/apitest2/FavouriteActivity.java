package com.example.markohare.apitest2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by aflynn on 17/04/2017.
 */


public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TvShowFavAdapter adapter;
    private List<TvShow> tvShowList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvShowList = new ArrayList<>();
        loadFavouritesFromDB();
        adapter = new TvShowFavAdapter(this, tvShowList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
     //   recyclerView.addItemDecoration(new MainActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Set the adapter to be a list array
        recyclerView.setAdapter(adapter);


    }

    private void loadFavouritesFromDB() {

        List<TvShowFavourites> TvShowFavouritesList;
        // These need to be read in from the SQLitedatabase
        SQLiteDatabaseHandler db2 = SQLiteDatabaseHandler.getInstance(this);
        TvShowFavouritesList = new ArrayList<TvShowFavourites>();
        TvShowFavouritesList=db2.getAllTvShowFavourites();
        // Close the database connection
        db2.close();
        // Copy the favourites into the current TvShowList
        copyFavouritesFromDB(TvShowFavouritesList);


    }



    private void copyFavouritesFromDB(List<TvShowFavourites> TvShowFavouritesList) {

//        TvShowList= new ArrayList<>();
        // Iterate through the favourites and copy them to the TvShowlist
        Iterator<TvShowFavourites> TvShowIterator = TvShowFavouritesList.iterator();
        while (TvShowIterator.hasNext()) {
            // tempTvShow will hold a temporary version of the current favourite before is copied to the list
            TvShow tempTvShow = new TvShow();
            // Use tempfav to store the current TvShow favourite that is being iterated through
            TvShowFavourites tempfav = TvShowIterator.next();
            tempTvShow.setId(tempfav.getID());
            tempTvShow.setName(tempfav.getTitle());
            tempTvShow.setPopularity(tempfav.getPopularity());
            tempTvShow.setOverview(tempfav.getOverview());
            tempTvShow.setPosterPath(tempfav.getPosterPath());
            Log.d( "ADAPT","copyFavouritesFromDB PosterPathxxx= " + tempTvShow.getPosterPath());
            tvShowList.add(tempTvShow);
        }


    }

}