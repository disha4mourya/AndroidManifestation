package com.example.androidmanifestation.server_calls;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import com.example.androidmanifestation.R;
import com.example.androidmanifestation.entity.SongsEntity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidmanifestation.Constants.SONGS_LIST_URL;

public class LoopjCallActivity extends AppCompatActivity {

    ProgressDialog progress;
    Context mContext;
    AsyncHttpClient client;

    private SongsAdapter songsAdapter;
    RecyclerView rvSongList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_library_call);
        mContext=this;


        rvSongList = findViewById(R.id.rvSongsList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvSongList.setLayoutManager(llm);

        songsAdapter = new SongsAdapter(this);
        rvSongList.setAdapter(songsAdapter);

        fetchList();
    }

    private void fetchList() {
        progress = new ProgressDialog(mContext);
        progress.setMessage(Html
                .fromHtml("<b>Fetching list " + "</b><br/>Please Wait..."));
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        String url = Uri.parse(SONGS_LIST_URL).buildUpon().build().toString();
        AsyncHttpResponseHandler httpResponseHandler = createHTTPResponseHandler();

        client = new AsyncHttpClient();
        client.get(url, httpResponseHandler);
    }

    public AsyncHttpResponseHandler createHTTPResponseHandler() {
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int arg0, org.apache.http.Header[] arg1,
                                  byte[] arg2, Throwable arg3) {
                progress.dismiss();
            }

            @Override
            public void onSuccess(int arg0, org.apache.http.Header[] arg1,
                                  byte[] arg2) {
                progress.dismiss();
                String result = new String(arg2);
                if (!result.equals("")) {
                    List<SongsEntity> songsEntityList = new ArrayList<>();

                    try {
                        JSONObject jsonObject = new JSONObject(result);
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
        };

        return handler;
    }
}
