package com.example.markohare.apitest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TvShowFavouritesManager";

    // TvShowFavouritess table name
    private static final String TABLE_FAVOURITES = "TvShowFavourites";

    // TvShowFavouritess Table Columns names
    private static final String KEY_ID = "ID";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_POPULARITY = "Popularity";
    private static final String KEY_OVERVIEW  = "Overview";
    private static final String KEY_POSTERPATH = "PosterPath";

    private static SQLiteDatabaseHandler mInstance = null;

    public static SQLiteDatabaseHandler getInstance(Context activityContext) {

        // Get the application context from the activityContext to prevent leak
        if (mInstance == null) {
            mInstance = new SQLiteDatabaseHandler (activityContext.getApplicationContext());
        }
        return mInstance;
    }


    /**
     * Private, use the static method "SQLiteDatabaseHandler.getInstance(Context)" instead.
     */
    private SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAVOURITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_POPULARITY + " REAL,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_POSTERPATH + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */



    // Adding new tvShowFavourites
    void addTvShowFavourites(TvShowFavourites tvShowFavourites) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, tvShowFavourites.getID()); // TvShowFavourites ID
        values.put(KEY_TITLE, tvShowFavourites.getTitle()); // TvShowFavourites TITLE
        values.put(KEY_POPULARITY, tvShowFavourites.getPopularity()); // TvShowFavourites POPULARITY
        values.put(KEY_OVERVIEW, tvShowFavourites.getOverview()); // TvShowFavourites OVERVIEW
        values.put(KEY_POSTERPATH, tvShowFavourites.getPosterPath()); // TvShowFavourites POSTERPATH



        // Inserting Row
        db.insert(TABLE_FAVOURITES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single tvShowFavourites
    TvShowFavourites getTvShowFavourites(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVOURITES, new String[] { KEY_ID,
                        KEY_TITLE, KEY_POPULARITY,KEY_OVERVIEW, KEY_POSTERPATH }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TvShowFavourites tvShowFavourites = new TvShowFavourites(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Double.parseDouble(cursor.getString(2)),cursor.getString(3),cursor.getString(4));
        // return tvShowFavourites
        return tvShowFavourites;
    }

    // Getting All TvShowFavouritess
    public List<TvShowFavourites> getAllTvShowFavourites() {
        List<TvShowFavourites> tvShowFavouritesList = new ArrayList<TvShowFavourites>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TvShowFavourites tvShowFavourites = new TvShowFavourites();
                tvShowFavourites.setID(Integer.parseInt(cursor.getString(0)));
                tvShowFavourites.setTitle(cursor.getString(1));
                Log.d( "DEBUG","getAllTvShowFavourites: Title= " + tvShowFavourites.getTitle());
                tvShowFavourites.setPopularity (Double.parseDouble(cursor.getString(2)));
                Log.d( "DEBUG","getAllTvShowFavourites: Popularity=" + tvShowFavourites.getPopularity());
                tvShowFavourites.setOverview (cursor.getString(3));
                Log.d( "DEBUG","getAllTvShowFavourites: Overview=" + tvShowFavourites.getOverview());
                tvShowFavourites.setPosterPath(cursor.getString(4));
                Log.d( "DEBUG","getAllTvShowFavourites: PosterPath= " + tvShowFavourites.getPosterPath());
                // Adding tvShowFavourites to list
                tvShowFavouritesList.add(tvShowFavourites);
            } while (cursor.moveToNext());
        }

        // return tvShowFavourites list
        return tvShowFavouritesList;
    }

    // Updating single tvShowFavourites
    public int updateTvShowFavourites(TvShowFavourites tvShowFavourites) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, tvShowFavourites.getTitle()); // TvShowFavourites TITLE
        values.put(KEY_POPULARITY, tvShowFavourites.getPopularity()); // TvShowFavourites POPULARITY
        values.put(KEY_OVERVIEW, tvShowFavourites.getOverview()); // TvShowFavourites OVERVIEW
        values.put(KEY_POSTERPATH, tvShowFavourites.getPosterPath()); // TvShowFavourites POSTERPATH

        // updating row
        return db.update(TABLE_FAVOURITES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(tvShowFavourites.getID()) });
    }

    // Deleting single tvShowFavourites
    public void deleteTvShowFavourites(TvShowFavourites tvShowFavourites) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITES, KEY_ID + " = ?",
                new String[] { String.valueOf(tvShowFavourites.getID()) });
        db.close();
    }


    // Getting tvShowFavouritess Count
    public int getTvShowFavouritessCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FAVOURITES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }    

}
