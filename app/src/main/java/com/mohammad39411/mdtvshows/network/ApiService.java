package com.mohammad39411.mdtvshows.network;

import com.mohammad39411.mdtvshows.responses.TVShowDetailsResponse;
import com.mohammad39411.mdtvshows.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTvShows(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailsResponse> getShowDetails(@Query("q") String tvShowId);

    @GET("search")
    Call<TVShowsResponse> searchTvShow(@Query("q") String query , @Query("page") int page);


}
