package com.yucelt.moviedb.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yucelt.moviedb.BuildConfig;
import com.yucelt.moviedb.R;
import com.yucelt.moviedb.adapters.tv.PopularTvAdapter;
import com.yucelt.moviedb.adapters.tv.TopRatedTvAdapter;
import com.yucelt.moviedb.models.tv.popular.TvPopular;
import com.yucelt.moviedb.models.tv.toprated.TvTopRated;
import com.yucelt.moviedb.network.ApiClient;
import com.yucelt.moviedb.network.ApiInterface;
import com.yucelt.moviedb.utilities.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvFragment extends Fragment {
    private static final String TAG = "TvFragment";
    private static final String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;

    private ProgressDialog progressDialog;

    private TopRatedTvAdapter adapterTopRated;
    private PopularTvAdapter adapterPopular;
    private RecyclerView.LayoutManager linearLayoutManagerTopRated, linearLayoutManagerPopular;
    private TvTopRated topRated;
    private TvPopular popular;

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @BindView(R.id.textTopRatedTvTitle)
    TextView textTopRatedTvTitle;

    @BindView(R.id.recyclerViewTopRatedTv)
    RecyclerView recyclerViewTopRatedTv;

    @BindView(R.id.textPopularTitle)
    TextView textPopularTitle;

    @BindView(R.id.recyclerViewPopularTv)
    RecyclerView recyclerViewPopularTv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, container, false);
        ButterKnife.bind(this, view);

        initDialog();

        initRecyclerViewTopRated();
        initRecyclerViewPopular();

        return view;
    }

    private void initRecyclerViewTopRated() {
        showProgressDialog();

        linearLayoutManagerTopRated = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTopRatedTv.setLayoutManager(linearLayoutManagerTopRated);

        Log.d(TAG, "initRecyclerView: " + TMDB_API_KEY);

        Call<TvTopRated> call = apiService.getTopRatedTv(TMDB_API_KEY);
        call.enqueue(new Callback<TvTopRated>() {
            @Override
            public void onResponse(Call<TvTopRated> call, Response<TvTopRated> response) {
                topRated = response.body();

                adapterTopRated = new TopRatedTvAdapter(topRated.getResults(), new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("detail_id", String.valueOf(topRated.getResults().get(position).getId()));
                        bundle.putString("detail_name", String.valueOf(topRated.getResults().get(position).getOriginalName()));
                        TvDetailFragment fragment = new TvDetailFragment();
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack("TvDetailFragment");
                        ft.commit();
                    }
                });
                recyclerViewTopRatedTv.setAdapter(adapterTopRated);
                adapterTopRated.notifyDataSetChanged();

                textTopRatedTvTitle.setVisibility(View.VISIBLE);

                hideProgressDialog();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<TvTopRated> call, Throwable t) {
                Log.e(TAG + " FAILURE: : ", t.getLocalizedMessage());
                hideProgressDialog();
            }
        });
    }

    private void initRecyclerViewPopular() {
        showProgressDialog();

        linearLayoutManagerPopular = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPopularTv.setLayoutManager(linearLayoutManagerPopular);

        Log.d(TAG, "initRecyclerView: " + TMDB_API_KEY);

        Call<TvPopular> call = apiService.getPopularTv(TMDB_API_KEY);
        call.enqueue(new Callback<TvPopular>() {
            @Override
            public void onResponse(Call<TvPopular> call, Response<TvPopular> response) {
                popular = response.body();

                adapterPopular = new PopularTvAdapter(popular.getResults(), new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("detail_id", String.valueOf(popular.getResults().get(position).getId()));
                        bundle.putString("detail_name", String.valueOf(popular.getResults().get(position).getOriginalName()));
                        TvDetailFragment fragment = new TvDetailFragment();
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack("TvDetailFragment");
                        ft.commit();
                    }
                });
                recyclerViewPopularTv.setAdapter(adapterPopular);
                adapterPopular.notifyDataSetChanged();

                textPopularTitle.setVisibility(View.VISIBLE);

                hideProgressDialog();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<TvPopular> call, Throwable t) {
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