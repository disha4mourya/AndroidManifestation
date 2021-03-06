package com.example.androidmanifestation.server_calls;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidmanifestation.R;
import com.example.androidmanifestation.entity.SongsEntity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidmanifestation.Constants.SONGS_LIST_URL;

public class VolleyServerCall extends AppCompatActivity {

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

    private void loadSongList() {

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SONGS_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("")) {
                            List<SongsEntity> songsEntityList = new ArrayList<>();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SongsEntity songsEntity = new SongsEntity();
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    if (jsonObject1.has("trackName")) {
                                        songsEntity.setTrackName(jsonObject1.getString("trackName"));
                                    }
                                    if (jsonObject1.has("collectionName")) {
                                        songsEntity.setCollectionName(jsonObject1.getString("collectionName"));
                                    }

                                    if (jsonObject1.has("artworkUrl100")) {
                                        songsEntity.setArtworkUrl100(jsonObject1.getString("artworkUrl100"));
                                    }

                                    if (jsonObject1.has("trackTimeMillis")) {
                                        songsEntity.setTrackTimeMillis(jsonObject1.getString("trackTimeMillis"));
                                    }

                                    if (jsonObject1.has("artistName")) {
                                        songsEntity.setArtistName(jsonObject1.getString("artistName"));
                                    }
                                    if (jsonObject1.has("collectionPrice")) {
                                        songsEntity.setCollectionPrice(jsonObject1.getString("collectionPrice"));
                                    }
                                    if (jsonObject1.has("trackPrice")) {
                                        songsEntity.setTrackPrice(jsonObject1.getString("trackPrice"));
                                    }
                                    if (jsonObject1.has("releaseDate")) {
                                        songsEntity.setReleaseDate(jsonObject1.getString("releaseDate"));
                                    }
                                    if (jsonObject1.has("trackCensoredName")) {
                                        songsEntity.setTrackCensoredName(jsonObject1.getString("trackCensoredName"));
                                    }
                                    if (jsonObject1.has("collectionViewUrl")) {
                                        songsEntity.setCollectionViewUrl(jsonObject1.getString("collectionViewUrl"));
                                    }
                                    if (jsonObject1.has("artistViewUrl")) {
                                        songsEntity.setArtistViewUrl(jsonObject1.getString("artistViewUrl"));
                                    }
                                    songsEntityList.add(songsEntity);
                                }


                                songsAdapter.setSongs(songsEntityList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}
