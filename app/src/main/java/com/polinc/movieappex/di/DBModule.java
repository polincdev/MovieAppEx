package com.polinc.movieappex.di;

 

import android.content.Context;

import androidx.room.Room;

import com.polinc.movieappex.net.BuildConfig;
import com.polinc.movieappex.net.MovieAPI;
import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.net.RequestInterceptor;
import com.polinc.movieappex.room.AppDatabase;
import com.polinc.movieappex.room.MoviesRetrieveController;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class DBModule {


    @Singleton
    @Provides
    public AppDatabase provideRoomDB(@ApplicationContext Context context )  {
        return Room.databaseBuilder(context, AppDatabase.class, "movieappex").build();
    }

    @Singleton
    @Provides
    public MoviesRetrieveController provideMoviesRetrieveController(@ApplicationContext Context context,AppDatabase db )  {
        return  new MoviesRetrieveController(  db);
    }

}
