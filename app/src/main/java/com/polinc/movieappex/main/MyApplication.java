package com.polinc.movieappex.main;
import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import com.polinc.movieappex.R;
import com.polinc.movieappex.data.InitDataSource;

import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.room.MoviesRetrieveController;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
 public class MyApplication extends Application  {


  public InitDataSource initDataSource=new InitDataSource();

  @Inject
  public MoviesGetController moviesGetController ;
  @Inject
  public MoviesRetrieveController moviesRetrieveController ;

 public FirebaseAuth mAuth;
 public FirebaseRemoteConfig mFirebaseRemoteConfig;

  @Override
  public void onCreate(){
   super.onCreate();
   // Initialize Firebase Auth
   mAuth = FirebaseAuth.getInstance();

   //Init remote config
   mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
   FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
           .setMinimumFetchIntervalInSeconds(3600)
           .build();
   mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
   //Set default values
   mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);



  }



 }

