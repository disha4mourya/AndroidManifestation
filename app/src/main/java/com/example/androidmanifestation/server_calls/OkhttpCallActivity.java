package com.example.androidmanifestation.server_calls;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.example.androidmanifestation.AppExecutors;
import com.example.androidmanifestation.R;
import com.example.androidmanifestation.entity.SongsEntity;
import com.example.androidmanifestation.entity.SongsListResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

