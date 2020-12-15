package com.mohammad39411.mdtvshows.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

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

                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWatchlist();
    }

    @Override
    public void onTvShowClicked(TVShow tvShow) {

    }

    @Override
    public void onRemoveShowFromList(TVShow tvShow, int position) {

    }
}
