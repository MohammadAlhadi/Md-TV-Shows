package com.mohammad39411.mdtvshows.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohammad39411.mdtvshows.network.ApiClient;
import com.mohammad39411.mdtvshows.network.ApiService;
import com.mohammad39411.mdtvshows.responses.TVShowDetailsResponse;
import com.mohammad39411.mdtvshows.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTvShowRepository {

    private ApiService apiService;

    public SearchTvShowRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowsResponse> searchTvShow(String query , int page) {
       MutableLiveData<TVShowsResponse> data = new MutableLiveData<>();
       apiService.searchTvShow(query , page).enqueue(new Callback<TVShowsResponse>() {
           @Override
           public void onResponse(@NonNull Call<TVShowsResponse> call,@NonNull Response<TVShowsResponse> response) {
               data.setValue(response.body());
           }

           @Override
           public void onFailure(@NonNull Call<TVShowsResponse> call, @NonNull Throwable t) {
               data.setValue(null);

           }
       });

       return data;
    }
}
