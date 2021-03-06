package com.example.androidmanifestation.server_calls.retro_call;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmanifestation.R;
import com.example.androidmanifestation.entity.SongsListResult;
import com.example.androidmanifestation.server_calls.SongsAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetroCallActivity extends AppCompatActivity {

    Context mContext;
    private SongsAdapter songsAdapter;
    RecyclerView rvSongList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_library_call);
        mContext = this;


        rvSongList = findViewById(R.id.rvSongsList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        rvSongList.setLayoutManager(llm);

        songsAdapter = new SongsAdapter(this);
        rvSongList.setAdapter(songsAdapter);

        loadSongList();
    }

    private void loadSongList() {

        RetrofitServices retrofitServices = RetrofitInstance.getRetrofitInstance().create(RetrofitServices.class);
        Call<SongsListResult> call = retrofitServices.getAllSongs();
        call.enqueue(new Callback<SongsListResult>() {
            @Override
            public void onResponse(Call<SongsListResult> call, Response<SongsListResult> response) {
                songsAdapter.setSongs(response.body().getResults());
            }

            @Override
            public void onFailure(Call<SongsListResult> call, Throwable t) {
                Log.d("TAG", "is" + t.getMessage());
            }
        });
    }
}