package com.yucelt.moviedb.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yucelt.moviedb.BuildConfig;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.adapters.movies.MovieCastAdapter;
import com.yucelt.moviedb.adapters.tv.TvCastAdapter;
import com.yucelt.moviedb.models.movies.cast.Cast;
import com.yucelt.moviedb.models.movies.cast.MovieDetailCast;
import com.yucelt.moviedb.models.movies.detail.MovieDetail;
import com.yucelt.moviedb.models.tv.cast.TvCast;
import com.yucelt.moviedb.network.ApiClient;
import com.yucelt.moviedb.network.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment {
    private static final String TAG = "MovieDetailFragment";
    private static final String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;

    private ProgressDialog progressDialog;

    private MovieCastAdapter adapterMovieCast;
    private RecyclerView.LayoutManager linearLayoutManagerCast;
    private List<Cast> casts;

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    private String stringId, stringMovieName;

    @BindView(R.id.imageCover)
    ImageView imageCover;

    @BindView(R.id.imagePoster)
    ImageView imagePoster;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.imageShare)
    ImageView imageShare;

    @BindView(R.id.textTitle)
    TextView textTitle;

    @BindView(R.id.textGenre)
    TextView textGenre;

    @BindView(R.id.textRate)
    TextView textRate;

    @BindView(R.id.textOverview)
    TextView textOverview;

    @BindView(R.id.textFullCast)
    TextView textFullCast;

    @BindView(R.id.recyclerViewDetailCast)
    RecyclerView recyclerViewDetailCast;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);

        assert getArguments() != null;
        stringId = getArguments().getString("detail_id");
        stringMovieName = getArguments().getString("detail_name");

        initDialog();
        initPage();
        initRecyclerViewCast();

        imageCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "I like this movie: " + stringMovieName;
                String shareSub = "Movie Suggestion";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        return view;
    }

    private void initPage() {
        showProgressDialog();

        Call<MovieDetail> call = apiService.getMovieDetail(stringId, TMDB_API_KEY);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                MovieDetail movieDetail = response.body();

                Double star = movieDetail.getVoteAverage() / 2;

                Glide.with(getActivity())
                        .load(ApiClient.baseImgUrl + movieDetail.getBackdropPath())
                        .into(imageCover);

                Glide.with(getActivity())
                        .load(ApiClient.baseImgUrl + movieDetail.getPosterPath())
                        .into(imagePoster);

                textTitle.setText(movieDetail.getTitle());

                String genres = "";
                for (int i = 0; i < movieDetail.getGenres().size(); i++) {
                    if (i != movieDetail.getGenres().size() - 1) {
                        genres = genres + movieDetail.getGenres().get(i).getName() + ", ";
                    } else {
                        genres = genres + movieDetail.getGenres().get(i).getName();
                    }
                }
                textGenre.setText(genres);
                textRate.setText(String.valueOf(movieDetail.getVoteAverage()));
                textOverview.setText(movieDetail.getOverview());

                ratingBar.setRating(star.floatValue());

                hideProgressDialog();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e(TAG + " FAILURE: : ", t.getLocalizedMessage());
                hideProgressDialog();
            }
        });
    }

    private void initRecyclerViewCast() {
        showProgressDialog();

        linearLayoutManagerCast = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDetailCast.setLayoutManager(linearLayoutManagerCast);

        Call<MovieDetailCast> call = apiService.getMovieCastCrew(stringId, TMDB_API_KEY);
        call.enqueue(new Callback<MovieDetailCast>() {
            @Override
            public void onResponse(Call<MovieDetailCast> call, Response<MovieDetailCast> response) {
                MovieDetailCast movieDetailCast = response.body();

                adapterMovieCast = new MovieCastAdapter(movieDetailCast.getCast());
                recyclerViewDetailCast.setAdapter(adapterMovieCast);
                adapterMovieCast.notifyDataSetChanged();

                hideProgressDialog();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<MovieDetailCast> call, Throwable t) {
                Log.e(TAG + " FAILURE: : ", t.getLocalizedMessage());
                hideProgressDialog();
            }
        });
    }

    protected void initDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.string_progress));
        progressDialog.setCancelable(false);
    }

    protected void showProgressDialog() {
        if (!progressDialog.isShowing()) progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }
}