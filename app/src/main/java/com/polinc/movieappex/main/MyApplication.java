package com.polinc.movieappex.main;
import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.polinc.movieappex.data.InitDataSource;
import com.polinc.movieappex.di.AppComponent;
import com.polinc.movieappex.di.AppModule;
import com.polinc.movieappex.di.DaggerAppComponent;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.net.NetworkModule;
import com.polinc.movieappex.room.MoviesRetrieveController;
import com.polinc.movieappex.util.FileClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;


 public class MyApplication extends Application  {


  public InitDataSource initDataSource=new InitDataSource();

  public AppComponent appComponent;
  @Inject
  public MoviesGetController moviesGetController ;
  @Inject
  public MoviesRetrieveController moviesRetrieveController ;

  @Override
  public void onCreate(){
   super.onCreate();
   appComponent = DaggerAppComponent.builder()
           .appModule(new AppModule(this))
           .networkModule(new NetworkModule())
           .build();

   appComponent.inject(this);


  }



 }

