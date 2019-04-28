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

                            SongsListResult songsListResult=response.body();
                            Toast.makeText(mContext, "dataGot" + response.body().toString(), Toast.LENGTH_SHORT).show();

                            if (!response.equals("")) {
                                List<SongsEntity> songsEntityList = new ArrayList<>();

                                try {
                                    JSONObject jsonObject = new JSONObject(response.toString());
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
                    });
                }
            }
        });
    }


    private class FetchClinics extends AsyncTask<Void, Integer, String> {
        private OkHttpClient client = new OkHttpClient();
        String serresponse = "";
        ProgressDialog progress;

        protected void onPreExecute() {
            progress = new ProgressDialog(mContext);
            progress.setCancelable(false);
            progress.setMessage(Html
                    .fromHtml("<b>Fetching songs list</b><br/>Please Wait..."));
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        protected String doInBackground(Void... arg0) {

            Request request = new Request.Builder()
                    .url(SONGS_LIST_URL)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    serresponse = response.body().string();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return serresponse;
        }


        protected void onPostExecute(String result) {
            progress.dismiss();
            if (!serresponse.equals("")) { //Response is not empty
                // Toast.makeText(context, serresponse, Toast.LENGTH_SHORT).show();


                    String separate[] = serresponse.split("XDX");
                    String clinicArray = separate[1];

                    JsonReader reader = new JsonReader(new StringReader(clinicArray));
                    reader.setLenient(true);

            }
        }

    }
}

