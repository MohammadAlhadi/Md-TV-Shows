package com.mohammad39411.mdtvshows.listeners;

import com.mohammad39411.mdtvshows.model.TVShow;

public interface WatchlistListener {

    void onTvShowClicked(TVShow tvShow);


    void onRemoveShowFromList(TVShow tvShow , int position);
}
