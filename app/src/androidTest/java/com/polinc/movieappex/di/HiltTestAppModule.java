package com.polinc.movieappex.di;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.polinc.movieappex.room.AppDatabase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class HiltTestAppModule {

    @Provides
    @Named("testDatabase")
    public AppDatabase injectInMemoryRoom(){
        return Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase.class ).allowMainThreadQueries().build();

    }
}
