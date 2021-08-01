package com.polinc.movieappex.net;
import java.util.List;
import java.util.Map;

import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.models.Review;
import com.polinc.movieappex.models.ReviewsWrapper;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface  MovieAPI {
	 
	
	@GET("3/discover/movie?language=en&sort_by=popularity.desc")
    Observable<MoviesWraper> popularMovies(@Query("page") int page);

    @GET("3/movie/top_rated?language=en")
    Observable<MoviesWraper> topratedMovies(@Query("page") int page);

    @GET("3/movie/upcoming?language=en")
    Observable<MoviesWraper> upcomingMovies(@Query("page") int page);


    @GET("3/discover/movie/{movieId}?language=en")
    Observable<Movie> getMovie(@Path("movieId") String movieId);

    @GET("3/discover/movie?vote_count.gte=500&language=en&sort_by=vote_average.desc")
    Observable<MoviesWraper> highestRatedMovies(@Query("page") int page);
 
    @GET("3/discover/movie?language=en&sort_by=release_date.desc")
    Observable<MoviesWraper> newestMovies(@Query("release_date.lte") String maxReleaseDate, @Query("vote_count.gte") int minVoteCount);
 

    @GET("3/movie/{movieId}/reviews")
    Observable<ReviewsWrapper> reviews(@Path("movieId") String movieId);

    @GET("3/search/movie?language=en-US&page=1")
    Observable<MoviesWraper> searchMovies(@Query("query") String searchQuery);

}
