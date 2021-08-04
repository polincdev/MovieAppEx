package com.polinc.movieappex.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.polinc.movieappex.models.Movie;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie")
    Single<List<Movie>> getAll();

    @Query("SELECT * FROM Movie")
    Single<List<Movie>> loadAllByIds( );

    @Query("SELECT * FROM Movie WHERE batch = :id")
    Single<List<Movie>> loadAllByBatchId(String id );

    @Query("SELECT * FROM Movie WHERE title LIKE :title LIMIT 1")
    Maybe<Movie> findByTitle(String title);

    @Query("SELECT * FROM Movie WHERE title LIKE :id LIMIT 1")
    Single<Movie> findById(String id);

    @Insert
    void insertAll(List<Movie> movies);

    @Delete
    void delete(Movie movie);
}