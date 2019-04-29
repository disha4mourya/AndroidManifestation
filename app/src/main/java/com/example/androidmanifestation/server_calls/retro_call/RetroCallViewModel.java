package com.example.androidmanifestation.server_calls.retro_call;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidmanifestation.entity.SongsEntity;
import com.example.androidmanifestation.entity.SongsListResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetroCallViewModel extends ViewModel {

    private MutableLiveData<List<SongsEntity>> songsEntity;

    public LiveData<List<SongsEntity>> getSongs() {
        if (songsEntity == null) {
            songsEntity = new MutableLiveData<List<SongsEntity>>();
            loadSongs();
        }
        return songsEntity;
    }


    private void loadSongs() {

        RetrofitServices retrofitServices = RetrofitInstance.getRetrofitInstance().create(RetrofitServices.class);
        Call<SongsListResult> call = retrofitServices.getAllSongs();

        call.enqueue(new Callback<SongsListResult>() {
            @Override
            public void onResponse(Call<SongsListResult> call, Response<SongsListResult> response) {
                SongsListResult songsListResult = response.body();
                songsEntity.setValue(songsListResult.getResults());
            }

            @Override
            public void onFailure(Call<SongsListResult> call, Throwable t) {

            }


        });
    }
}
