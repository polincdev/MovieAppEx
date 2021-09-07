package com.polinc.movieappex.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.polinc.movieappex.models.Movie;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie")
    Single<List<Movie>> getAll();

    @Query("SELECT * FROM Movie")
    ListenableFuture<List<Movie>> loadAll( );
    @Query("SELECT * FROM Movie")
    List<Movie> loadAllMovies( );

    @Query("SELECT * FROM Movie WHERE batch = :id")
    Single<List<Movie>> loadAllByBatchId(String id );

    @Query("SELECT * FROM Movie WHERE title LIKE :title LIMIT 1")
    Single<Movie> findByTitle(String title);

    @Query("SELECT * FROM Movie WHERE title LIKE :id LIMIT 1")
    Single<Movie> findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Movie> movies);

    @Delete
    Completable  delete(Movie movie);

    @Query("DELETE FROM Movie")
    void deleteAll();
}