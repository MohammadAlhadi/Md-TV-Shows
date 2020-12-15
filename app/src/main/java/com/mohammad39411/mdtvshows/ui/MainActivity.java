package com.mohammad39411.mdtvshows.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;



import com.mohammad39411.mdtvshows.R;
import com.mohammad39411.mdtvshows.adapters.TVShowsAdapter;
import com.mohammad39411.mdtvshows.databinding.ActivityMainBinding;
import com.mohammad39411.mdtvshows.listeners.TVShowsListener;
import com.mohammad39411.mdtvshows.model.TVShow;
import com.mohammad39411.mdtvshows.viewmodels.MostPopTvShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowsListener {

    private MostPopTvShowsViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();


    }
    private void doInitialization(){
        activityMainBinding.tvShowRecycler.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopTvShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows , this);
        activityMainBinding.tvShowRecycler.setAdapter(tvShowsAdapter);
        activityMainBinding.tvShowRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvShowRecycler.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePages){
                        currentPage += 1;
                        getMostPopularTVShows();
                    }

                }
            }
        });
        activityMainBinding.imgWatchList.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), WatchlistActivity.class)));

        getMostPopularTVShows();
    }

    private void getMostPopularTVShows() {
        toggleLoading();
        viewModel.getMostPopularTvShows(currentPage).observe(this, mostPopularTVShowsResponse ->
                {
               toggleLoading();
               if (mostPopularTVShowsResponse != null){
                   totalAvailablePages =  mostPopularTVShowsResponse.getTotalPages();
                   if (mostPopularTVShowsResponse.getTvShows() != null){
                       int oldCount = tvShows.size();
                       tvShows.addAll(mostPopularTVShowsResponse.getTvShows());
                       tvShowsAdapter.notifyItemRangeInserted(oldCount , tvShows.size());
                   }
               }
                });
    }
    private void toggleLoading(){
        if (currentPage == 1){
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);

            }else {
                activityMainBinding.setIsLoading(true);
            }


        }else {
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()){
                activityMainBinding.setIsLoadingMore(false);
            } else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext() , TVShowDetailsActivity.class);

        intent.putExtra("tvShow" , tvShow);

        startActivity(intent);
    }
}
