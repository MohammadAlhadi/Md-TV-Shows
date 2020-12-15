package com.mohammad39411.mdtvshows.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mohammad39411.mdtvshows.database.TVShowsDatabase;
import com.mohammad39411.mdtvshows.model.TVShow;

import java.util.List;

import io.reactivex.Flowable;

public class WaListVM extends AndroidViewModel {
    private TVShowsDatabase tvShowsDatabase;

    public WaListVM(@NonNull Application application){
        super(application);
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }
    public Flowable<List<TVShow>> loadWatchList(){
        return tvShowsDatabase.tvShowDao().getWatchList();
    }
}
