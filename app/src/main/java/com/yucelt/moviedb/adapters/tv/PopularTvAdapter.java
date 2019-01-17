package com.yucelt.moviedb.adapters.tv;

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
import com.yucelt.moviedb.models.tv.popular.Result;
import com.yucelt.moviedb.network.ApiClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopularTvAdapter extends RecyclerView.Adapter<PopularTvAdapter.ViewHolder> {
    private static final String TAG = "PopularTvAdapter";

    private List<Result> popular;

    private int lastPosition = -1;

    public PopularTvAdapter(List<Result> popular) {
        this.popular = popular;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_popular_tv, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String imgUrl = ApiClient.baseImgUrl + popular.get(position).getBackdropPath();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(32));

        Glide.with(holder.imageViewPopularTv.getContext())
                .load(imgUrl)
                .apply(requestOptions)
                .into(holder.imageViewPopularTv);

        holder.textViewPopularTvTitle.setText(popular.get(position).getName());
        holder.textViewRateTv.setText(String.valueOf(popular.get(position).getVoteAverage()));

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return popular.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewPopularTv)
        ImageView imageViewPopularTv;

        @BindView(R.id.textViewPopularTvTitle)
        TextView textViewPopularTvTitle;

        @BindView(R.id.textViewRateTv)
        TextView textViewRateTv;

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