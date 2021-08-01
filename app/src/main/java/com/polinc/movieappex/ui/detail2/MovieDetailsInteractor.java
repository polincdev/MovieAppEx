package com.polinc.movieappex.ui.detail2;



import com.polinc.movieappex.models.Review;
import com.polinc.movieappex.models.Video;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;


public interface MovieDetailsInteractor {


    Observable<List<Review>> getReviews(String id);
}
