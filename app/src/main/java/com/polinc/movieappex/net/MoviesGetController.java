package com.polinc.movieappex.net;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.models.Review;
import com.polinc.movieappex.models.ReviewsWrapper;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response; 
import retrofit2.Retrofit;
 
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

@Singleton
public class MoviesGetController   {


	 Retrofit retrofit;
	MovieAPI movieAPI;

	@Inject
	 public MoviesGetController()
	 {
		  Gson gson = new GsonBuilder()
	                .setLenient()
	                .create();

		      OkHttpClient okHttpClient = new OkHttpClient.Builder()
				  .addInterceptor(new RequestInterceptor())
				   .build();
		           
		            retrofit = new Retrofit.Builder()
	                .baseUrl(BuildConfig.TMDB_BASE_URL)
	                .client(okHttpClient)
	                .addConverterFactory(GsonConverterFactory.create(gson))
	                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
	                .build();

		   movieAPI = retrofit.create(MovieAPI.class);

	 }


	 public Observable<MoviesWraper> getMoviesPopular(int page ) {

		    Observable<MoviesWraper> call = movieAPI.popularMovies(page);

		     call .subscribeOn(Schedulers.io())
			 .observeOn(AndroidSchedulers.mainThread());

		     return call;

	         }

	public Observable<MoviesWraper> getMoviesToprated(int page ) {

		Observable<MoviesWraper> call = movieAPI.topratedMovies(page);
 		call .subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread());

		return call;
     }

	public Observable<MoviesWraper> getMoviesUpcoming(int page ) {

		Observable<MoviesWraper> call = movieAPI.upcomingMovies(page);
		call .subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		return call;
	}

	public Observable<Movie> getMovie(String movieId ) {

	    Observable<Movie> call = movieAPI.getMovie(movieId);

		call
		  .subscribeOn(Schedulers.io())
		 .observeOn(AndroidSchedulers.mainThread());

		return call;

	}

	public Observable<ReviewsWrapper> getReviews(String movieId ) {

		Observable<ReviewsWrapper> call = movieAPI.reviews(movieId);

		call
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		return call;

	}



}
