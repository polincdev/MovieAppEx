package com.polinc.movieappex.main;
import android.app.Application;

import com.polinc.movieappex.data.InitDataSource;
import com.polinc.movieappex.di.AppComponent;
import com.polinc.movieappex.di.AppModule;
import com.polinc.movieappex.di.DaggerAppComponent;
import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.net.NetworkModule;
import com.polinc.movieappex.room.MoviesRetrieveController;

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

