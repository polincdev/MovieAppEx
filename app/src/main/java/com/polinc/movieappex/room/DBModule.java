package com.polinc.movieappex.room;

 

import android.content.Context;

import com.polinc.movieappex.net.BuildConfig;
import com.polinc.movieappex.net.MovieAPI;
import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.net.RequestInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class DBModule {

    @Provides
    @Singleton
    MoviesRetrieveController provideMoviesRetrieveControllerr(Context context) {
        return new MoviesRetrieveController(context);
    }



 

}
