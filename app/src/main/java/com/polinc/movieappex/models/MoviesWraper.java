package com.polinc.movieappex.models;

 
import com.google.gson.annotations.SerializedName;
 

import java.util.List;

 

public class MoviesWraper {

    @SerializedName(  "results")
    private List<Movie> movies;

    public List<Movie> getMovieList() {
        return movies;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movies = movieList;
    }
}
