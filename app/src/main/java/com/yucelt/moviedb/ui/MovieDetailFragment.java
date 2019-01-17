package com.yucelt.moviedb.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.yucelt.moviedb.models.movies.detail.MovieDetail;
import com.yucelt.moviedb.network.ApiClient;
import com.yucelt.moviedb.network.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment {
    private static final String TAG = "MovieDetailFragment";
    private static final String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;

    private ProgressDialog progressDialog;

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    private String stringId;

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

        initDialog();
        initPage();

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