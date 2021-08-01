package com.polinc.movieappex.ui.movies;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.Snackbar;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.net.MoviesGetController;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;

public class TopratedViewModel extends ViewModel {

    private SingleLiveEvent<ArrayList<Movie>> moviesData;
    private SingleLiveEvent<Integer> resultStatus;
    MoviesGetController moviesGetController ;
   // ArrayList<Movie> movies;
    public int currentPage=0;
    Activity activity;

    public TopratedViewModel(Activity activity )
    {
        this.activity=activity;
        //
        moviesGetController =  ((MyApplication) getActivity().getApplication()).moviesGetController;
        moviesData = new SingleLiveEvent<>();
        resultStatus = new SingleLiveEvent<>();
        System.out.println("TopratedViewModel=");
    }

    public LiveData<ArrayList<Movie>> getMoviesData() {
        System.out.println("getMoviesData="+currentPage);
        return moviesData;
    }
    public LiveData<Integer> getResultStatus() {
        return resultStatus;
    }


    Activity getActivity(){return activity;}


    public void fetchMovies()
    {

        ((MoviesActivity)getActivity()).loadingProgressBar.setVisibility(View.VISIBLE);

        currentPage++;
        if (currentPage > 100)
            currentPage = 100;
        System.out.println("fetchMovies="+currentPage);

        Observable<MoviesWraper> moviesCall = moviesGetController.getMoviesToprated(currentPage);
        moviesCall.subscribe( this::onMovieFetchAddSuccess,  this::onMovieFetchFailed);
    }

    void onMovieFetchAddSuccess(MoviesWraper moviesWraper)
    {
        ArrayList<Movie> newItems = new ArrayList<Movie>();
        System.out.println("WYNIK="+moviesWraper.getMovieList().size());
        moviesWraper.getMovieList().forEach(movie ->  newItems.add(movie));
       //post instea of set to publish on mainThread
        moviesData.postValue(newItems);

    }

    private void onMovieFetchFailed(Throwable e) {

        e.printStackTrace();
        resultStatus.setValue(-1);
    }


}
