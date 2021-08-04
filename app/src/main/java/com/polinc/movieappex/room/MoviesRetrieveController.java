package com.polinc.movieappex.room;

import android.content.Context;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.models.ReviewsWrapper;
import com.polinc.movieappex.net.BuildConfig;
import com.polinc.movieappex.net.MovieAPI;
import com.polinc.movieappex.net.RequestInterceptor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class MoviesRetrieveController {

	AppDatabase db;
	@Inject
	 public MoviesRetrieveController(Context context ) {
		  db = Room.databaseBuilder(context,AppDatabase.class, "movieappex").build();

	 }



	public Single<List<Movie>> getMoviesAllByBatch(String batchId) {

		Single<List<Movie>> call = db.movieDao().loadAllByBatchId(batchId);

		call.subscribeOn(Schedulers.io())
		 .observeOn(AndroidSchedulers.mainThread());

		return call;

	}


	public Single<List<Movie>> saveMovies() {

		Single<List<Movie>> call = db.movieDao().loadAllByIds();

		call.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());

		return call;

	}

}
