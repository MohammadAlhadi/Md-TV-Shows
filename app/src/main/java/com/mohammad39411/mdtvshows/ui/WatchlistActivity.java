package com.mohammad39411.mdtvshows.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.mohammad39411.mdtvshows.R;
import com.mohammad39411.mdtvshows.adapters.WatchlistAdapter;
import com.mohammad39411.mdtvshows.databinding.ActivityWatchlistBinding;
import com.mohammad39411.mdtvshows.listeners.WatchlistListener;
import com.mohammad39411.mdtvshows.model.TVShow;
import com.mohammad39411.mdtvshows.viewmodels.WaListVM;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchlistBinding binding;
    private WaListVM viewModel;
    private WatchlistAdapter watchlistAdapter;
    List<TVShow> watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_watchlist);
        doInitialization();
    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(WaListVM.class);
        binding.imgClose.setOnClickListener(view -> onBackPressed());
        watchlist = new ArrayList<>();
    }

    private void loadWatchlist() {
        binding.setIsLoading(true);
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(viewModel.loadWatchList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    binding.setIsLoading(false);
                    if (watchlist.size() > 0){
                        watchlist.clear();
                    }
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchlist , this);
                    binding.wListRecyclerView.setAdapter(watchlistAdapter);
                    binding.wListRecyclerView.setVisibility(View.VISIBLE);
                    disposable.dispose();
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWatchlist();
    }

    @Override
    public void onTvShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext() , TVShowDetailsActivity.class);
        intent.putExtra("tvShow" , tvShow);
        startActivity(intent);
    }

    @Override
    public void onRemoveShowFromList(TVShow tvShow, int position) {
        CompositeDisposable compositeDisposableDelete = new CompositeDisposable();
        compositeDisposableDelete.add(viewModel.removeTvShowFromWatchlist(tvShow)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(()->{
            watchlist.remove(position);
            watchlistAdapter.notifyItemRemoved(position);
            watchlistAdapter.notifyItemRangeChanged(position , watchlistAdapter.getItemCount());
            compositeDisposableDelete.dispose();
        }));
    }
}
