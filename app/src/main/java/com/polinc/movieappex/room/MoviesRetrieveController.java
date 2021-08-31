package com.polinc.movieappex.room;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.google.common.util.concurrent.ListenableFuture;
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
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MoviesRetrieveController {
	@Inject
	AppDatabase db;

	 public MoviesRetrieveController(Context context ) {

	 }



	public Single<List<Movie>> getMoviesAllByBatch(String batchId) {

		Single<List<Movie>> call = db.movieDao().loadAllByBatchId(batchId);

		call.subscribeOn(Schedulers.io())
		 .observeOn(AndroidSchedulers.mainThread());

		return call;

	}

	public ListenableFuture<List<Movie>> getMoviesAll( ) {

		ListenableFuture<List<Movie>> call = db.movieDao().loadAll();

		return call;

	}

	public void saveMovies(List<Movie> movies ) {

	 /*	//
		Completable deleteAllCompletable = db.movieDao().deleteAll()
				.subscribeOn(Schedulers.newThread()) //Not io. Causes cannot-access-database-on-the-main-thread
				.observeOn(AndroidSchedulers.mainThread());

		Completable insertAllCompletable = db.movieDao().insertAll(movies);
       //delete al before
	 	deleteAllCompletable.andThen(insertAllCompletable);
		return deleteAllCompletable;*/
          db.movieDao().insertAll(movies);

	};

	public void cleanDB( ) {
           db.movieDao().deleteAll( );

	};


}
