package com.yucelt.moviedb.adapters.tv;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.models.tv.toprated.TvTopRated;

public class RecyclerViewTopRatedTvAdapter extends RecyclerView.Adapter<RecyclerViewTopRatedTvAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewTopRatedTvAdapter";

    private TvTopRated topRated;

    private Context mContext;

    private int lastPosition = -1;

    public RecyclerViewTopRatedTvAdapter(Context context, TvTopRated topRated) {
        this.topRated = topRated;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_top_rated_tv, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String baseImgUrl = "https://image.tmdb.org/t/p/original/";
        String imgUrl = baseImgUrl + topRated.getResults().get(position).getPosterPath();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(32));

        Glide.with(mContext)
                .load(imgUrl)
                .apply(requestOptions)
                .into(holder.imageViewTopRatedTv);

        holder.textViewTopRatedTv.setText(topRated.getResults().get(position).getName());

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return topRated.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewTopRatedTv;
        TextView textViewTopRatedTv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewTopRatedTv = itemView.findViewById(R.id.imageViewTopRatedTv);
            textViewTopRatedTv = itemView.findViewById(R.id.textViewTopRatedTv);
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