package com.polinc.movieappex.ui.detail2;



import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.Review;
import com.polinc.movieappex.models.Video;

import java.util.List;


interface MovieDetailsView {
    void showDetails(Movie movie);



    void showReviews(List<Review> reviews);


}
