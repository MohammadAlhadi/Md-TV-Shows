package com.mohammad39411.mdtvshows.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.mohammad39411.mdtvshows.database.TVShowsDatabase;
import com.mohammad39411.mdtvshows.model.TVShow;
import com.mohammad39411.mdtvshows.repositories.TVShowDetailsRepository;
import com.mohammad39411.mdtvshows.responses.TVShowDetailsResponse;

import io.reactivex.Completable;

public class TVShowDetailsViewModel extends AndroidViewModel {

    private TVShowsDatabase tvShowsDatabase;
    private TVShowDetailsRepository tvShowDetailsRepository;

    public TVShowDetailsViewModel(@NonNull Application application){
        super(application);

        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        return tvShowDetailsRepository.getTvShowDetails(tvShowId);

    }

    public Completable addToWatchList(TVShow tvShow){
        return tvShowsDatabase.tvShowDao().addToWatchList(tvShow);

    }
}
