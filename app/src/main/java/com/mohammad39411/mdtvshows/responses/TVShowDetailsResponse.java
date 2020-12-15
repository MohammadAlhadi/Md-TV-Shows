package com.mohammad39411.mdtvshows.responses;

import com.google.gson.annotations.SerializedName;
import com.mohammad39411.mdtvshows.model.TVShow;
import com.mohammad39411.mdtvshows.model.TVShowsDetails;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    private TVShowsDetails tvShowsDetails;

    public TVShowsDetails getTvShowsDetails() {
        return tvShowsDetails;
    }
}
