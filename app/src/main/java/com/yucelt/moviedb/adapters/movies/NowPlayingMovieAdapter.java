package com.yucelt.moviedb.adapters.movies;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.models.movies.nowplaying.Result;
import com.yucelt.moviedb.network.ApiClient;
import com.yucelt.moviedb.utilities.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NowPlayingMovieAdapter extends RecyclerView.Adapter<NowPlayingMovieAdapter.ViewHolder> {
    private static final String TAG = "NowPlayingMovieAdapter";

    private List<Result> nowPlaying;

    private int lastPosition = -1;

    private OnItemClickListener listener;

    public NowPlayingMovieAdapter(List<Result> nowPlaying, OnItemClickListener listener) {
        this.nowPlaying = nowPlaying;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_now_playing_movies, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });

        return viewHolder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        String imgUrl = ApiClient.baseImgUrl + nowPlaying.get(position).getPosterPath();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(32));

        Glide.with(holder.imageViewNowPlayingMovies.getContext())
                .load(imgUrl)
                .apply(requestOptions)
                .into(holder.imageViewNowPlayingMovies);

        holder.textViewNowPlayingMovies.setText(nowPlaying.get(position).getTitle());

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return nowPlaying.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewNowPlayingMovies)
        ImageView imageViewNowPlayingMovies;

        @BindView(R.id.textViewNowPlayingMovies)
        TextView textViewNowPlayingMovies;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_animation_scale);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}