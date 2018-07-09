package com.lexcorp.dhadakapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.lexcorp.dhadakapp.R;
import com.lexcorp.dhadakapp.utils.Constants;

import java.io.IOException;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongPlayerActivity extends BaseActivity {

    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.main_audio_view)
    RelativeLayout mainAudioView;

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int[] colorList = new int[]{R.color.Tomato, R.color.LightCoral, R.color.peru, R.color.DarkCyan};
    private InterstitialAd mInterstitialAd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        final String audioFile = intent.getStringExtra(Constants.AUDIO_URL);
        final String coverImage = intent.getStringExtra(Constants.IMG_URL);
        final String songName = intent.getStringExtra(Constants.AUDIO_NAME);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        mainAudioView.setBackgroundColor(ContextCompat.getColor(this, getRandomColor()));
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(audioFile);
            mediaPlayer.prepareAsync();

            final ProgressDialog dialog = new ProgressDialog(SongPlayerActivity.this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.show();

            ((TextView) findViewById(R.id.now_playing_text)).setText(songName);
            ImageView mImageView = (ImageView) findViewById(R.id.coverImage);
            //Glide.with(getApplicationContext()).load(coverImage).into(mImageView);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(final MediaPlayer mp) {
                    mp.start();
                    play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_pause));
                    seekBar = (SeekBar) findViewById(R.id.seekBar);
                    mRunnable.run();
                    dialog.dismiss();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_play));
                }
            });
        } catch (IOException e) {
            Activity a = this;
            a.finish();
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        }
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return colorList[rnd.nextInt(colorList.length)];
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (mediaPlayer != null) {
                int mDuration = mediaPlayer.getDuration();
                seekBar.setMax(mDuration);
                TextView totalTime = (TextView) findViewById(R.id.totalTime);
                totalTime.setText(getTimeString(mDuration));

                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);

                TextView currentTime = (TextView) findViewById(R.id.currentTime);
                currentTime.setText(getTimeString(mCurrentPosition));

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (mediaPlayer != null && fromUser) {
                            mediaPlayer.seekTo(progress);
                        }
                    }
                });
            }
            mHandler.postDelayed(this, 10);
        }
    };

    @OnClick(R.id.play)
    public void play() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_play));
        } else {
            mediaPlayer.start();
            play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_pause));
        }
    }

    public void stop(View view) {
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
    }

    public void seekForward(View view) {
        int seekForwardTime = 5000;

        int currentPosition = mediaPlayer.getCurrentPosition();
        if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
            mediaPlayer.seekTo(currentPosition + seekForwardTime);
        } else {
            mediaPlayer.seekTo(mediaPlayer.getDuration());
        }
    }

    public void seekBackward(View view) {
        int seekBackwardTime = 5000;
        int currentPosition = mediaPlayer.getCurrentPosition();
        if (currentPosition - seekBackwardTime >= 0) {
            mediaPlayer.seekTo(currentPosition - seekBackwardTime);
        } else {
            mediaPlayer.seekTo(0);
        }
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    finish();
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        long hours = millis / (1000 * 60 * 60);
        long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

        buf.append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }
}
