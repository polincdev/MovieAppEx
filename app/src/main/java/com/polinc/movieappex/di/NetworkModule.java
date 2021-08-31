package com.polinc.movieappex.di;

 

import com.polinc.movieappex.net.BuildConfig;
import com.polinc.movieappex.net.MovieAPI;
import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.net.RequestInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    public static final int CONNECT_TIMEOUT_IN_MS = 30000;

    @Provides
    @Singleton
    MoviesGetController provideMoviesGetController( ) {
        return new MoviesGetController();
    }

    @Provides
    @Singleton
    Interceptor requestInterceptor(RequestInterceptor interceptor) {
        return interceptor;
    }


    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(RequestInterceptor requestInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                .addInterceptor(requestInterceptor);


		/*
		 * if (BuildConfig.DEBUG) { HttpLoggingInterceptor loggingInterceptor = new
		 * HttpLoggingInterceptor();
		 * loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		 * builder.addInterceptor(loggingInterceptor); }
		 */

        return builder.build();
    }

    @Singleton
    @Provides
    Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit
                .Builder()
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                 .client(okHttpClient)
                .build();




    }

    @Singleton
    @Provides
    MovieAPI provideMovieAPI(Retrofit retrofit) {
        return retrofit.create(MovieAPI.class);
    }

}
