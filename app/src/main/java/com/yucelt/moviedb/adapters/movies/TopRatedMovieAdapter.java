package com.yucelt.moviedb.adapters.movies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.models.movies.toprated.Result;
import com.yucelt.moviedb.network.ApiClient;
import com.yucelt.moviedb.ui.MovieDetailFragment;
import com.yucelt.moviedb.utilities.Config;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopRatedMovieAdapter extends RecyclerView.Adapter<TopRatedMovieAdapter.ViewHolder> {
    private static final String TAG = "TopRatedMovieAdapter";

    private List<Result> topRated;

    private int lastPosition = -1;

    public TopRatedMovieAdapter(List<Result> topRated) {
        this.topRated = topRated;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_top_rated_movies, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String imgUrl = ApiClient.baseImgUrl + topRated.get(position).getBackdropPath();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(32));

        Glide.with(holder.imageViewTopRatedMovies.getContext())
                .load(imgUrl)
                .apply(requestOptions)
                .into(holder.imageViewTopRatedMovies);

        setAnimation(holder.itemView, position);

        holder.cardViewTopRatedMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("detail_id", String.valueOf(topRated.get(position).getId()));

                MovieDetailFragment fragment = new MovieDetailFragment();
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
        return topRated.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardViewTopRatedMovies)
        RelativeLayout cardViewTopRatedMovies;

        @BindView(R.id.imageViewTopRatedMovies)
        ImageView imageViewTopRatedMovies;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}