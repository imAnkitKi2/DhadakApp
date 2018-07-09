package com.lexcorp.dhadakapp.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.lexcorp.dhadakapp.adapter.VideoAdapter;
import com.lexcorp.dhadakapp.model.VideoListModel;
import com.lexcorp.dhadakapp.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoListActivity extends BaseActivity {

    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.list_video)
    ListView listView;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;

    private FirebaseFirestore db;
    private CollectionReference movieCollectionRef;
    private ArrayList<VideoListModel> videoListModelArrayList;
    private VideoAdapter adapter;
    private boolean isTrailer;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);

        FirebaseApp app = getAppInstance(VideoListActivity.this);
        db = FirebaseFirestore.getInstance(app);

        if (getIntent() != null) {
            isTrailer = getIntent().getBooleanExtra(Constants.IsTrailer, false);
            movieId = getIntent().getStringExtra(Constants.MOVIE_ID);
        }

        if (TextUtils.isEmpty(movieId)) {
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (isTrailer)
                getSupportActionBar().setTitle("Trailers");
            else
                getSupportActionBar().setTitle("Videos");
        }

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        videoListModelArrayList = new ArrayList<>();
        if (isTrailer) {
            movieCollectionRef = db.collection("movies").document(movieId)
                    .collection("trailer");
        } else {
            movieCollectionRef = db.collection("movies").document(movieId)
                    .collection("video");
        }

        movieCollectionRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            videoListModelArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String code = document.getData().get("code").toString();
                                String title = document.getData().get("title").toString();
                                VideoListModel model = new VideoListModel(title, code);
                                videoListModelArrayList.add(model);
                            }
                            setAdapter();
                        } else {
                            Log.e("Movies", "Error getting documents.", task.getException());
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setAdapter() {
        if (videoListModelArrayList != null && videoListModelArrayList.size() > 0) {
            llNoData.setVisibility(View.GONE);
            //videoListModelArrayList.add(1, null);
            if (adapter == null) {
                adapter = new VideoAdapter(VideoListActivity.this, videoListModelArrayList);
            }

            adapter.doRefresh(videoListModelArrayList);

            if (listView.getAdapter() == null) {
                listView.setAdapter(adapter);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent i = new Intent(VideoListActivity.this, VideoActivity.class);
                    i.putExtra(Constants.YOUTUBE_CODE, videoListModelArrayList.get(position).youtubeCode);
                    startActivity(i);
                }
            });
        } else {
            llNoData.setVisibility(View.VISIBLE);
        }
    }
}
