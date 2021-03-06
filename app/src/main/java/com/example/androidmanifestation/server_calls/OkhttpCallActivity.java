package com.example.androidmanifestation.server_calls;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmanifestation.R;
import com.example.androidmanifestation.entity.SongsEntity;
import com.example.androidmanifestation.entity.SongsListResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.androidmanifestation.Constants.SONGS_LIST_URL;

public class OkhttpCallActivity extends AppCompatActivity {

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
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvSongList.setLayoutManager(llm);

        songsAdapter = new SongsAdapter(this);
        rvSongList.setAdapter(songsAdapter);

        loadSongList();
    }

    void loadSongList() {

        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(SONGS_LIST_URL)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            SongsListResult songsListResult = new Gson().fromJson(response.body().charStream(), SongsListResult.class);
                            List<SongsEntity> songsEntityList = songsListResult.getResults();

                            // SongsListResult songsListResult = response.body();
                            Toast.makeText(mContext, "dataGot" + response.body().toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(mContext, "SongListSize" + songsEntityList.size(), Toast.LENGTH_SHORT).show();
                            songsAdapter.setSongs(songsEntityList);

                        }
                    });
                }
            }
        });
    }

}

