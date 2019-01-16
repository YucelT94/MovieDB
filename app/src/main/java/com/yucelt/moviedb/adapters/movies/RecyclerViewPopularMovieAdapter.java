package com.yucelt.moviedb.adapters.movies;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.models.movies.nowplaying.MovieNowPlaying;
import com.yucelt.moviedb.models.movies.popular.MoviePopular;
import com.yucelt.moviedb.ui.MovieDetailFragment;
import com.yucelt.moviedb.utilities.Config;

public class RecyclerViewPopularMovieAdapter extends RecyclerView.Adapter<RecyclerViewPopularMovieAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewPopularMovieAdapter";

    private MoviePopular popular;

    private Context mContext;

    private int lastPosition = -1;

    public RecyclerViewPopularMovieAdapter(Context context, MoviePopular popular) {
        this.popular = popular;
        this.mContext = context;
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

        String baseImgUrl = "https://image.tmdb.org/t/p/original/";
        String imgUrl = baseImgUrl + popular.getResults().get(position).getPosterPath();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(32));

        Glide.with(mContext)
                .load(imgUrl)
                .apply(requestOptions)
                .into(holder.imageViewPopularMovies);

        holder.textViewPopularMovies.setText(popular.getResults().get(position).getTitle());
        holder.textViewRateMovie.setText(String.valueOf(popular.getResults().get(position).getVoteAverage()));

        setAnimation(holder.itemView, position);

        holder.cardViewPopularMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("detail_id", String.valueOf(popular.getResults().get(position).getId()));

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
        return popular.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cardViewPopularMovies;
        ImageView imageViewPopularMovies;
        TextView textViewPopularMovies;
        TextView textViewRateMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            cardViewPopularMovies = itemView.findViewById(R.id.cardViewPopularMovies);
            imageViewPopularMovies = itemView.findViewById(R.id.imageViewPopularMovies);
            textViewPopularMovies = itemView.findViewById(R.id.textViewPopularMovies);
            textViewRateMovie = itemView.findViewById(R.id.textViewRateMovie);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}