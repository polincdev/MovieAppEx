package com.polinc.movieappex.ui.detail2;



import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.Review;
import com.polinc.movieappex.net.MoviesGetController;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


class MovieDetailsPresenterImpl implements MovieDetailsPresenter {
    private MovieDetailsView view;
    private MovieDetailsInteractor movieDetailsInteractor;

    private Disposable reviewSubscription;

    MovieDetailsPresenterImpl(MovieDetailsInteractor movieDetailsInteractor  ) {
        this.movieDetailsInteractor = movieDetailsInteractor;

    }

    @Override
    public void setView(MovieDetailsView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;

    }

    @Override
    public void showDetails(Movie movie) {
        if (isViewAttached()) {
            view.showDetails(movie);
        }
    }

    private boolean isViewAttached() {
        return view != null;
    }




    private void onGetTrailersFailure() {
        // Do nothing
    }

    @Override
    public void showReviews(Movie movie) {
        reviewSubscription = movieDetailsInteractor.getReviews(movie.getId())
                .subscribe(this::onGetReviewsSuccess, t -> onGetReviewsFailure());
    }

    private void onGetReviewsSuccess(List<Review> reviews) {
        if (isViewAttached()) {
            view.showReviews(reviews);
        }
    }

    private void onGetReviewsFailure() {
        // Do nothing
    }


}
