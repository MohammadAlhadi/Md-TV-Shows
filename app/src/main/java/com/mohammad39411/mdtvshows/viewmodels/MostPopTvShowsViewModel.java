package com.mohammad39411.mdtvshows.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mohammad39411.mdtvshows.repositories.MostPopularTvShowsRepository;
import com.mohammad39411.mdtvshows.responses.TVShowsResponse;

public class MostPopTvShowsViewModel extends ViewModel {

    private MostPopularTvShowsRepository mostPopularTvShowsRepository;

    public MostPopTvShowsViewModel(){

        mostPopularTvShowsRepository = new MostPopularTvShowsRepository();

    }


    public LiveData<TVShowsResponse> getMostPopularTvShows(int page){
        return mostPopularTvShowsRepository.getMostPopularTvShows(page);
    }
}
