package com.polinc.movieappex.di;


import android.content.Context;

import androidx.room.Room;

import com.polinc.movieappex.room.AppDatabase;
import com.polinc.movieappex.room.MoviesRetrieveController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {



}
