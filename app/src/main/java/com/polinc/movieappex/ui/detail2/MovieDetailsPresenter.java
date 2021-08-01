package com.polinc.movieappex.ui.detail2;


import com.polinc.movieappex.models.Movie;

public interface MovieDetailsPresenter {
    void showDetails(Movie movie);



    void showReviews(Movie movie);



    void setView(MovieDetailsView view);

    void destroy();
}
