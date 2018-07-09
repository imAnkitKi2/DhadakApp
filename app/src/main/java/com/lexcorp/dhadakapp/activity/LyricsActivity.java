package com.lexcorp.dhadakapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lexcorp.dhadakapp.R;
import com.lexcorp.dhadakapp.adapter.LyricsAdapter;
import com.lexcorp.dhadakapp.model.LyricsListModel;
import com.lexcorp.dhadakapp.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LyricsActivity extends BaseActivity {

    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.list_lyrics)
    ListView listView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;

    private FirebaseFirestore db;
    private CollectionReference movieCollectionRef;
    private ArrayList<LyricsListModel> lyricsListModelArrayList;
    private boolean isLyrics;
    private LyricsAdapter adapter;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        ButterKnife.bind(this);

        FirebaseApp app = getAppInstance(LyricsActivity.this);
        db = FirebaseFirestore.getInstance(app);

        if (getIntent() != null) {
            isLyrics = getIntent().getBooleanExtra(Constants.IsLyrics, false);
            movieId = getIntent().getStringExtra(Constants.MOVIE_ID);
        }

        if (TextUtils.isEmpty(movieId)) {
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (isLyrics)
                getSupportActionBar().setTitle("Lyrics");
            else
                getSupportActionBar().setTitle("Songs");
        }

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        swipeRefreshLayout.setRefreshing(true);
        getLyricsOrSong();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getLyricsOrSong();
            }
        });
    }

    public void getLyricsOrSong() {
        lyricsListModelArrayList = new ArrayList<>();
        if (isLyrics) {
            movieCollectionRef = db.collection("movies").document(movieId)
                    .collection("lyrics");
        } else {
            movieCollectionRef = db.collection("movies").document(movieId)
                    .collection("song");
        }

        movieCollectionRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            lyricsListModelArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name, link;
                                if (isLyrics) {
                                    name = document.getData().get("songName").toString();
                                    link = document.getData().get("lyrics").toString();
                                } else {
                                    name = document.getData().get("name").toString();
                                    link = document.getData().get("link").toString();
                                }
                                LyricsListModel model = new LyricsListModel(name, link);
                                lyricsListModelArrayList.add(model);
                            }
                            setAdapter();
                        } else {
                            Log.e("Movies", "Error getting documents.", task.getException());
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setAdapter() {
        if (lyricsListModelArrayList != null && lyricsListModelArrayList.size() > 0) {
            llNoData.setVisibility(View.GONE);
//            if (lyricsListModelArrayList.size() == 1) {
//                lyricsListModelArrayList.add(1, null);
//            } else if (lyricsListModelArrayList.size() == 2) {
//                lyricsListModelArrayList.add(2, null);
//            } else if (lyricsListModelArrayList.size() == 3) {
//                lyricsListModelArrayList.add(3, null);
//            } else if (lyricsListModelArrayList.size() == 4) {
//                lyricsListModelArrayList.add(4, null);
//            } else {
//                lyricsListModelArrayList.add(5, null);
//            }
            adapter = new LyricsAdapter(LyricsActivity.this, lyricsListModelArrayList, isLyrics);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if (isLyrics) {
                        Intent i = new Intent(LyricsActivity.this, LyricsDetailActivity.class);
                        i.putExtra(Constants.lyrics, lyricsListModelArrayList.get(position).lyrics);
                        i.putExtra(Constants.songName, lyricsListModelArrayList.get(position).songName);
                        i.putExtra("position", position);
                        i.putExtra("data", (Serializable) lyricsListModelArrayList);

                        startActivity(i);
                    } else {
                        Intent i = new Intent(LyricsActivity.this, SongPlayerActivity.class);
                        i.putExtra(Constants.AUDIO_URL, lyricsListModelArrayList.get(position).lyrics);
                        i.putExtra(Constants.AUDIO_NAME, lyricsListModelArrayList.get(position).songName);
                        i.putExtra(Constants.IMG_URL, "");
                        startActivity(i);
                    }
                }
            });
        } else {
            llNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
