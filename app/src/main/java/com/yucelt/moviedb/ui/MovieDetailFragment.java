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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yucelt.moviedb.BuildConfig;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.models.movies.detail.MovieDetail;
import com.yucelt.moviedb.services.ApiClient;
import com.yucelt.moviedb.services.ApiInterface;

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

    @BindView(R.id.imageRate1)
    ImageView imageRate1;

    @BindView(R.id.imageRate2)
    ImageView imageRate2;

    @BindView(R.id.imageRate3)
    ImageView imageRate3;

    @BindView(R.id.imageRate4)
    ImageView imageRate4;

    @BindView(R.id.imageRate5)
    ImageView imageRate5;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initPage() {
        showProgressDialog();

        Call<MovieDetail> call = apiService.getMovieDetail(stringId, TMDB_API_KEY);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                MovieDetail movieDetail = response.body();

                String baseImgUrl = "https://image.tmdb.org/t/p/original/";
                Double star = movieDetail.getVoteAverage() / 2;

                Glide.with(getActivity())
                        .load(baseImgUrl + movieDetail.getBackdropPath())
                        .into(imageCover);

                Glide.with(getActivity())
                        .load(baseImgUrl + movieDetail.getPosterPath())
                        .into(imagePoster);

                assert movieDetail != null;
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

                if (star < 1) {
                    imageRate1.setImageResource(R.drawable.ic_star);
                    imageRate2.setImageResource(R.drawable.ic_star);
                    imageRate3.setImageResource(R.drawable.ic_star);
                    imageRate4.setImageResource(R.drawable.ic_star);
                    imageRate5.setImageResource(R.drawable.ic_star);
                } else if (star < 2) {
                    imageRate1.setImageResource(R.drawable.ic_star_selected);
                    imageRate2.setImageResource(R.drawable.ic_star);
                    imageRate3.setImageResource(R.drawable.ic_star);
                    imageRate4.setImageResource(R.drawable.ic_star);
                    imageRate5.setImageResource(R.drawable.ic_star);
                } else if (star < 3) {
                    imageRate1.setImageResource(R.drawable.ic_star_selected);
                    imageRate2.setImageResource(R.drawable.ic_star_selected);
                    imageRate3.setImageResource(R.drawable.ic_star);
                    imageRate4.setImageResource(R.drawable.ic_star);
                    imageRate5.setImageResource(R.drawable.ic_star);
                } else if (star < 4) {
                    imageRate1.setImageResource(R.drawable.ic_star_selected);
                    imageRate2.setImageResource(R.drawable.ic_star_selected);
                    imageRate3.setImageResource(R.drawable.ic_star_selected);
                    imageRate4.setImageResource(R.drawable.ic_star);
                    imageRate5.setImageResource(R.drawable.ic_star);
                } else if (star < 5) {
                    imageRate1.setImageResource(R.drawable.ic_star_selected);
                    imageRate2.setImageResource(R.drawable.ic_star_selected);
                    imageRate3.setImageResource(R.drawable.ic_star_selected);
                    imageRate4.setImageResource(R.drawable.ic_star_selected);
                    imageRate5.setImageResource(R.drawable.ic_star);
                } else if (star == 5) {
                    imageRate1.setImageResource(R.drawable.ic_star_selected);
                    imageRate2.setImageResource(R.drawable.ic_star_selected);
                    imageRate3.setImageResource(R.drawable.ic_star_selected);
                    imageRate4.setImageResource(R.drawable.ic_star_selected);
                    imageRate5.setImageResource(R.drawable.ic_star_selected);
                }

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