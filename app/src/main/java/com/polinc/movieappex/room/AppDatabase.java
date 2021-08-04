package com.polinc.movieappex.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.polinc.movieappex.models.Movie;

@Database(entities = {Movie.class}, version = 1,  exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}