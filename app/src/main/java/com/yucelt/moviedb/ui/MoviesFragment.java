package com.yucelt.moviedb.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yucelt.moviedb.BuildConfig;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.adapters.movies.RecyclerViewNowPlayingMovieAdapter;
import com.yucelt.moviedb.adapters.movies.RecyclerViewPopularMovieAdapter;
import com.yucelt.moviedb.adapters.movies.RecyclerViewTopRatedMovieAdapter;
import com.yucelt.moviedb.models.movies.nowplaying.MovieNowPlaying;
import com.yucelt.moviedb.models.movies.popular.MoviePopular;
import com.yucelt.moviedb.models.movies.toprated.MovieTopRated;
import com.yucelt.moviedb.services.ApiClient;
import com.yucelt.moviedb.services.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {
    private static final String TAG = "MoviesFragment";
    private static final String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;

    private ProgressDialog progressDialog;

    private RecyclerViewTopRatedMovieAdapter adapterTopRated;
    private RecyclerViewNowPlayingMovieAdapter adapterNowPlaying;
    private RecyclerViewPopularMovieAdapter adapterPopular;
    private RecyclerView.LayoutManager linearLayoutManagerTopRated, linearLayoutManagerNowPlaying, linearLayoutManagerPopular;
    private MovieTopRated topRated;
    private MovieNowPlaying nowPlaying;
    private MoviePopular popular;

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @BindView(R.id.textTopRatedMoviesTitle)
    TextView textTopRatedMoviesTitle;

    @BindView(R.id.recyclerViewTopRatedMovies)
    RecyclerView recyclerViewTopRatedMovies;

    @BindView(R.id.textNowPlayingTitle)
    TextView textNowPlayingTitle;

    @BindView(R.id.recyclerViewNowPlayingMovies)
    RecyclerView recyclerViewNowPlayingMovies;

    @BindView(R.id.textPopularTitle)
    TextView textPopularTitle;

    @BindView(R.id.recyclerViewPopularMovies)
    RecyclerView recyclerViewPopularMovies;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);

        initDialog();

        initRecyclerViewTopRated();
        initRecyclerViewNowPlaying();
        initRecyclerViewPopular();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initRecyclerViewTopRated() {
        showProgressDialog();

        linearLayoutManagerTopRated = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTopRatedMovies.setLayoutManager(linearLayoutManagerTopRated);

        Call<MovieTopRated> call = apiService.getTopRatedMovies(TMDB_API_KEY);
        call.enqueue(new Callback<MovieTopRated>() {
            @Override
            public void onResponse(Call<MovieTopRated> call, Response<MovieTopRated> response) {
                topRated = response.body();

                adapterTopRated = new RecyclerViewTopRatedMovieAdapter(getActivity(), topRated);
                recyclerViewTopRatedMovies.setAdapter(adapterTopRated);
                adapterTopRated.notifyDataSetChanged();

                textTopRatedMoviesTitle.setVisibility(View.VISIBLE);

                hideProgressDialog();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<MovieTopRated> call, Throwable t) {
                Log.e(TAG + " FAILURE: : ", t.getLocalizedMessage());
                hideProgressDialog();
            }
        });
    }

    private void initRecyclerViewNowPlaying() {
        showProgressDialog();

        linearLayoutManagerNowPlaying = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNowPlayingMovies.setLayoutManager(linearLayoutManagerNowPlaying);

        Call<MovieNowPlaying> call = apiService.getNowPlayingMovies(TMDB_API_KEY);
        call.enqueue(new Callback<MovieNowPlaying>() {
            @Override
            public void onResponse(Call<MovieNowPlaying> call, Response<MovieNowPlaying> response) {
                nowPlaying = response.body();

                adapterNowPlaying = new RecyclerViewNowPlayingMovieAdapter(getActivity(), nowPlaying);
                recyclerViewNowPlayingMovies.setAdapter(adapterNowPlaying);
                adapterNowPlaying.notifyDataSetChanged();

                textNowPlayingTitle.setVisibility(View.VISIBLE);

                hideProgressDialog();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<MovieNowPlaying> call, Throwable t) {
                Log.e(TAG + " FAILURE: : ", t.getLocalizedMessage());
                hideProgressDialog();
            }
        });
    }

    private void initRecyclerViewPopular() {
        showProgressDialog();

        linearLayoutManagerPopular = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularMovies.setLayoutManager(linearLayoutManagerPopular);

        Call<MoviePopular> call = apiService.getPopularMovies(TMDB_API_KEY);
        call.enqueue(new Callback<MoviePopular>() {
            @Override
            public void onResponse(Call<MoviePopular> call, Response<MoviePopular> response) {
                popular = response.body();

                adapterPopular = new RecyclerViewPopularMovieAdapter(getActivity(), popular);
                recyclerViewPopularMovies.setAdapter(adapterPopular);
                adapterPopular.notifyDataSetChanged();

                textPopularTitle.setVisibility(View.VISIBLE);

                hideProgressDialog();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<MoviePopular> call, Throwable t) {
                Log.e(TAG + " FAILURE: : ", t.getLocalizedMessage());
                hideProgressDialog();
            }
        });
    }

    protected void initDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("İşleminiz gerçekleştiriliyor...");
        progressDialog.setCancelable(false);
    }

    protected void showProgressDialog() {
        if (!progressDialog.isShowing()) progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }
}