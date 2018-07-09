package com.lexcorp.dhadakapp.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lexcorp.dhadakapp.R;
import com.lexcorp.dhadakapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.adView)
    AdView mAdView;

    private String movieId = "Dhadak";
    String shareBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (TextUtils.isEmpty(movieId)) {
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(movieId);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        shareBody = movieId + " movie Songs, Video, Trailers & Lyrics available now on : https://play.google.com/store/apps/details?id="
                + getPackageName();

        AdRequest request = new AdRequest.Builder().build();
        mAdView.loadAd(request);
    }

    @OnClick({R.id.card_view1, R.id.card_view2, R.id.card_view3, R.id.card_view4, R.id.card_share, R.id.card_rateus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_view1:
                Intent trailerIntent = new Intent(MainActivity.this, VideoListActivity.class);
                trailerIntent.putExtra(Constants.IsTrailer, true);
                trailerIntent.putExtra(Constants.MOVIE_ID, movieId);
                startActivity(trailerIntent);
                break;
            case R.id.card_view2:
                Intent songIntent = new Intent(MainActivity.this, LyricsActivity.class);
                songIntent.putExtra(Constants.IsLyrics, false);
                songIntent.putExtra(Constants.MOVIE_ID, movieId);
                startActivity(songIntent);
                break;
            case R.id.card_view3:
                Intent videoIntent = new Intent(MainActivity.this, VideoListActivity.class);
                videoIntent.putExtra(Constants.IsTrailer, false);
                videoIntent.putExtra(Constants.MOVIE_ID, movieId);
                startActivity(videoIntent);
                break;
            case R.id.card_view4:
                Intent lyricsIntent = new Intent(MainActivity.this, LyricsActivity.class);
                lyricsIntent.putExtra(Constants.IsLyrics, true);
                lyricsIntent.putExtra(Constants.MOVIE_ID, movieId);
                startActivity(lyricsIntent);
                break;
            case R.id.card_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.card_rateus:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rate_us:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    return true;
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    return true;
                }
            case R.id.action_more_app:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=CrickBetting")));
                return true;
            case R.id.action_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via..."));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
