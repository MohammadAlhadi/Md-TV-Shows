package com.mohammad39411.mdtvshows.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;


import com.mohammad39411.mdtvshows.R;
import com.mohammad39411.mdtvshows.adapters.TVShowsAdapter;
import com.mohammad39411.mdtvshows.databinding.ActivitySearchBinding;
import com.mohammad39411.mdtvshows.listeners.TVShowsListener;
import com.mohammad39411.mdtvshows.model.TVShow;
import com.mohammad39411.mdtvshows.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowsListener {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter adapter;
    private int currentPage = 1;
    private int totalPages = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_search);

        doInitialization();

    }

    private void doInitialization() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
        binding.tvShowRecycler.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        adapter = new TVShowsAdapter(tvShows , this);
        binding.tvShowRecycler.setAdapter(adapter);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null){
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()){
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                totalPages = 1;
                                currentPage = 1;
                                searchTvShow(editable.toString());
                            });
                        }
                    } , 800);
                }else {
                    tvShows.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        binding.tvShowRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (binding.tvShowRecycler.canScrollVertically(1)){
                    if (!binding.etSearch.getText().toString().isEmpty()){
                        if (currentPage > totalPages) {
                            currentPage += 1;
                            searchTvShow(binding.etSearch.getText().toString());
                        }
                    }
                }
            }
        });

        binding.etSearch.requestFocus();
    }

    private void searchTvShow(String query){
        toggleLoading();
        viewModel.searchTvShow(query , currentPage).observe(this , tvShowsResponse -> {
            toggleLoading();
            if (tvShowsResponse != null){
                totalPages = tvShowsResponse.getTotalPages();
                if (tvShowsResponse.getTvShows() != null){
                    int oldCount = tvShows.size();
                    tvShows.addAll(tvShowsResponse.getTvShows());
                    adapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });
    }

    private void toggleLoading(){
        if (currentPage == 1){
            if (binding.getIsLoading() != null && binding.getIsLoading()){
                binding.setIsLoading(false);

            }else {
                binding.setIsLoading(true);
            }


        }else {
            if (binding.getIsLoadingMore() != null && binding.getIsLoadingMore()){
                binding.setIsLoadingMore(false);
            } else {
                binding.setIsLoadingMore(true);
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
