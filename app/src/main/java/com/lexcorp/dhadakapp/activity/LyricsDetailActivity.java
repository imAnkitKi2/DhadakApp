package com.lexcorp.dhadakapp.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.lexcorp.dhadakapp.R;
import com.lexcorp.dhadakapp.utils.Constants;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LyricsDetailActivity extends BaseActivity {

    @BindView(R.id.tv_lyrics)
    TextView tvLyrics;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.adView)
    AdView adView;

    private String songName, lyrics;
    private InterstitialAd mInterstitialAd;
    private int[] colorList = new int[]{R.color.Tomato, R.color.LightCoral, R.color.peru, R.color.DarkCyan};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_detail);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            songName = getIntent().getStringExtra(Constants.songName);
            lyrics = getIntent().getStringExtra(Constants.lyrics);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(songName);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(request);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvLyrics.setText(Html.fromHtml(lyrics, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvLyrics.setText(Html.fromHtml(lyrics));
        }

        cardView.setCardBackgroundColor(ContextCompat.getColor(this, getRandomColor()));
    }

    public void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return colorList[rnd.nextInt(colorList.length)];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, tvLyrics.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.menu_copy:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", tvLyrics.getText().toString());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                }

                Toast.makeText(this, "Lyrics Copied !!!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
