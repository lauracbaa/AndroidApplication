package com.example.markohare.apitest2;

import java.util.List;

/**
 * Created by Mark O'Hare on 25/03/2017.
 */

public class TvShow {


    final private String IMG_BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w300";
    final private String IMG_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";

    private int id;
    private String backdropPath;
    private int[] epRuntime;
    private String[] genres;
    private String homepage;
    private String lastAirDate;
    private String name;
    private String[] networks;
    private int noOfEpisodes;
    private int noOfSeasons;
    private String overview;
    private double popularity;
    private String posterPath;
    private List<Season> seasons;

    public String getIMG_POSTER_BASE_URL() {
        return IMG_POSTER_BASE_URL;
    }

    public static class Season {
        private String airDate;
        private int episodeCount;
        private int id;
        private String posterPath;
        private int number;



        public String getAirDate() {
            return airDate;
        }

        public void setAirDate(String airDate) {
            this.airDate = airDate;
        }

        public int getEpisodeCount() {
            return episodeCount;
        }

        public void setEpisodeCount(int episodeCount) {
            this.episodeCount = episodeCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = IMG_BACKDROP_BASE_URL + backdropPath;
    }

    public int[] getEpRuntime() {
        return epRuntime;
    }

    public void setEpRuntime(int[] epRuntime) {
        this.epRuntime = epRuntime;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getNetworks() {
        return networks;
    }

    public void setNetworks(String[] networks) {
        this.networks = networks;
    }

    public int getNoOfEpisodes() {
        return noOfEpisodes;
    }

    public void setNoOfEpisodes(int noOfEpisodes) {
        this.noOfEpisodes = noOfEpisodes;
    }

    public int getNoOfSeasons() {
        return noOfSeasons;
    }

    public void setNoOfSeasons(int noOfSeasons) {
        this.noOfSeasons = noOfSeasons;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }


}
