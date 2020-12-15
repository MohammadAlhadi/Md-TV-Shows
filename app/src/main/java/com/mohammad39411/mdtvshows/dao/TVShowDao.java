package com.mohammad39411.mdtvshows.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mohammad39411.mdtvshows.model.TVShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface TVShowDao {
    @Query("SELECT * FROM tvShows")
    Flowable<List<TVShow>> getWatchList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToWatchList(TVShow tvShow);

    @Delete
    Completable removeFromWatchList(TVShow tvShow);

    @Query("SELECT * FROM tvShows WHERE id = :tvShowId")
    Flowable<TVShow> getTvShowFromWatchlist(String tvShowId);
}
