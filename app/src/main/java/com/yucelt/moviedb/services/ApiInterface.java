package com.yucelt.moviedb.services;

import com.yucelt.moviedb.models.movies.cast.MovieDetailCast;
import com.yucelt.moviedb.models.movies.detail.MovieDetail;
import com.yucelt.moviedb.models.movies.nowplaying.MovieNowPlaying;
import com.yucelt.moviedb.models.movies.popular.MoviePopular;
import com.yucelt.moviedb.models.movies.toprated.MovieTopRated;
import com.yucelt.moviedb.models.tv.cast.TvCast;
import com.yucelt.moviedb.models.tv.detail.TvDetail;
import com.yucelt.moviedb.models.tv.popular.TvPopular;
import com.yucelt.moviedb.models.tv.toprated.TvTopRated;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //Movies
    @GET("movie/top_rated")
    Call<MovieTopRated> getTopRatedMovies(@Query("api_key") String api_key);

    @GET("movie/now_playing")
    Call<MovieNowPlaying> getNowPlayingMovies(@Query("api_key") String api_key);

    @GET("movie/popular")
    Call<MoviePopular> getPopularMovies(@Query("api_key") String api_key);

    //Movie Details
    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(@Path("movie_id") String movie_id,
                                           @Query("api_key") String api_key);

    @GET("movie/{movie_id}/credits")
    Call<MovieDetailCast> getMovieCastCrew(@Path("movie_id") String movie_id,
                                                 @Query("api_key") String api_key);

    //Tv
    @GET("tv/top_rated")
    Call<TvTopRated> getTopRatedTv(@Query("api_key") String api_key);

    @GET("tv/popular")
    Call<TvPopular> getPopularTv(@Query("api_key") String api_key);

    //Tv Details
    @GET("tv/{tv_id}")
    Call<TvDetail> getTvDetail(@Path("tv_id") String tv_id,
                                     @Query("api_key") String api_key);

    @GET("tv/{tv_id}/credits")
    Call<TvCast> getTvCastCrew(@Path("tv_id") String tv_id,
                                     @Query("api_key") String api_key);
}