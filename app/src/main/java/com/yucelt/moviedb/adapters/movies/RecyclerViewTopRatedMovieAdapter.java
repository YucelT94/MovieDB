package com.yucelt.moviedb.adapters.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.models.movies.toprated.MovieTopRated;

public class RecyclerViewTopRatedMovieAdapter extends RecyclerView.Adapter<RecyclerViewTopRatedMovieAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewTopRatedMovieAdapter";

    private MovieTopRated topRated;

    private Context mContext;

    private int lastPosition = -1;

    public RecyclerViewTopRatedMovieAdapter(Context context, MovieTopRated topRated) {
        this.topRated = topRated;
        this.mContext = context;
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

        String baseImgUrl = "https://image.tmdb.org/t/p/original/";
        String imgUrl = baseImgUrl + topRated.getResults().get(position).getBackdropPath();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(32));

        Glide.with(mContext)
                .load(imgUrl)
                .apply(requestOptions)
                .into(holder.imageViewTopRatedMovies);

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return topRated.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewTopRatedMovies;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewTopRatedMovies = itemView.findViewById(R.id.imageViewTopRatedMovies);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}