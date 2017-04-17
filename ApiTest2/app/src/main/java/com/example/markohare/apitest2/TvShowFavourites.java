package com.example.markohare.apitest2;

/**
 * Created by aflynn on 16/04/2017.
 */

public class TvShowFavourites {

    private int ID;
    private String Title;
    private double Popularity;
    private String Overview;
    private String PosterPath;


    public TvShowFavourites() {

    }

    public TvShowFavourites(int ID, String Title, double Popularity, String Overview, String PosterPath  ) {
        this.ID=ID;
        this.Title=Title;
        this.Popularity=Popularity;
        this.Overview=Overview;
        this.PosterPath=PosterPath;

    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public double getPopularity() {
        return Popularity;
    }

    public void setPopularity(double popularity) {
        Popularity = popularity;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }
}
