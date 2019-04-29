package com.example.androidmanifestation.server_calls.retro_call;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmanifestation.R;
import com.example.androidmanifestation.entity.SongsEntity;
import com.example.androidmanifestation.server_calls.SongsAdapter;

import java.util.List;

public class RetroViewModelThroughCall extends AppCompatActivity {

    Context mContext;
    private SongsAdapter songsAdapter;
    RecyclerView rvSongList;
    private String TAG=RetroViewModelThroughCall.class.getSimpleName();

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

        setUpViewModel();
    }

    private void setUpViewModel() {

        RetroCallViewModel retroCallViewModel = ViewModelProviders.of(this).get(RetroCallViewModel.class);
        retroCallViewModel.getSongs().observe(this, new Observer<List<SongsEntity>>() {
            @Override
            public void onChanged(List<SongsEntity> songsEntities) {
                songsAdapter.setSongs(songsEntities);
            }
        });
    }
}