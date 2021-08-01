package com.polinc.movieappex.ui.detail2;



import com.polinc.movieappex.models.Review;
import com.polinc.movieappex.models.ReviewsWrapper;
import com.polinc.movieappex.models.Video;
import com.polinc.movieappex.models.VideoWrapper;
import com.polinc.movieappex.net.MoviesGetController;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;


class MovieDetailsInteractorImpl implements MovieDetailsInteractor {
    MoviesGetController moviesGetController;
    MovieDetailsInteractorImpl(  MoviesGetController moviesGetController ) {
      this.moviesGetController=moviesGetController;
    }



    @Override
    public Observable<List<Review>> getReviews(final String id) {
        return moviesGetController.getReviews(id).map(ReviewsWrapper::getReviews);
    }

}
