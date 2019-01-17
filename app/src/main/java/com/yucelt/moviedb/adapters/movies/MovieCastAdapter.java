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
import com.yucelt.moviedb.models.movies.cast.Cast;
import com.yucelt.moviedb.network.ApiClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.ViewHolder> {
    private static final String TAG = "MovieCastAdapter";

    private List<Cast> casts;

    private int lastPosition = -1;

    public MovieCastAdapter(List<Cast> casts) {
        this.casts = casts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cast, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String imgUrl = ApiClient.baseImgUrl + casts.get(position).getProfilePath();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(32));

        Glide.with(holder.imageCast.getContext())
                .load(imgUrl)
                .apply(requestOptions)
                .into(holder.imageCast);

        holder.textCastName.setText(casts.get(position).getName());
        holder.textCastCharacter.setText(String.valueOf(casts.get(position).getCharacter()));

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageCast)
        ImageView imageCast;

        @BindView(R.id.textCastName)
        TextView textCastName;

        @BindView(R.id.textCastCharacter)
        TextView textCastCharacter;

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