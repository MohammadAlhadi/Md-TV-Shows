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
import io.reactivex.Flowable;

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

    public Flowable<TVShow> getTvShowFromWatchlist(String tvShowId){
        return tvShowsDatabase.tvShowDao().getTvShowFromWatchlist(tvShowId);
    }

    public Completable removeFromWatchList(TVShow tvShow){
        return tvShowsDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}
