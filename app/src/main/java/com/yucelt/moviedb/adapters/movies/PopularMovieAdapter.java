package com.yucelt.moviedb.adapters.movies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.models.movies.popular.Result;
import com.yucelt.moviedb.network.ApiClient;
import com.yucelt.moviedb.ui.MovieDetailFragment;
import com.yucelt.moviedb.utilities.Config;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopularMovieAdapter extends RecyclerView.Adapter<PopularMovieAdapter.ViewHolder> {
    private static final String TAG = "PopularMovieAdapter";

    private List<Result> popular;

    private int lastPosition = -1;

    public PopularMovieAdapter(List<Result> popular) {
        this.popular = popular;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_popular_movies, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String imgUrl = ApiClient.baseImgUrl + popular.get(position).getPosterPath();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(32));

        Glide.with(holder.imageViewPopularMovies.getContext())
                .load(imgUrl)
                .apply(requestOptions)
                .into(holder.imageViewPopularMovies);

        holder.textViewPopularMovies.setText(popular.get(position).getTitle());
        holder.textViewRateMovie.setText(String.valueOf(popular.get(position).getVoteAverage()));

        setAnimation(holder.itemView, position);

        holder.cardViewPopularMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("detail_id", String.valueOf(popular.get(position).getId()));
                Fragment fragment = new MovieDetailFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = Config.getContextMainActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack("MovieDetailFragment");
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return popular.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardViewPopularMovies)
        RelativeLayout cardViewPopularMovies;

        @BindView(R.id.imageViewPopularMovies)
        ImageView imageViewPopularMovies;

        @BindView(R.id.textViewPopularMovies)
        TextView textViewPopularMovies;

        @BindView(R.id.textViewRateMovie)
        TextView textViewRateMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_animation_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}