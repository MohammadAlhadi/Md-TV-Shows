package com.mohammad39411.mdtvshows.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mohammad39411.mdtvshows.repositories.SearchTvShowRepository;
import com.mohammad39411.mdtvshows.responses.TVShowsResponse;

public class SearchViewModel extends ViewModel {
    private SearchTvShowRepository searchTvShowRepository;

    public SearchViewModel(){
        searchTvShowRepository = new SearchTvShowRepository();
    }

    public LiveData<TVShowsResponse> searchTvShow(String query , int page){
        return searchTvShowRepository.searchTvShow(query , page);
    }
}
