package com.lexcorp.dhadakapp.adapter;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lexcorp.dhadakapp.R;
import com.lexcorp.dhadakapp.model.LyricsListModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Boss on 7/3/2017.
 */

public class LyricsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LyricsListModel> data;
    private boolean isLyrics;
    private int[] colorList = new int[]{R.color.Tomato, R.color.LightCoral, R.color.peru, R.color.DarkCyan};
    private int count = 0;

    public LyricsAdapter(Context context, ArrayList<LyricsListModel> list, boolean isLyrics) {
        this.context = context;
        this.data = list;
        this.isLyrics = isLyrics;
    }

    public interface OnDownloadClickListener {
        public void OnAudioDownload();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public LyricsListModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View v;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.lyrics_item_list, null);
        } else {
            v = (View) convertView;
        }
        TextView txt_title = (TextView) v.findViewById(R.id.tv_name);
        final AdView adView = (AdView) v.findViewById(R.id.adView);
        final CardView cardView = (CardView) v.findViewById(R.id.card_view);

        if (data.get(position) != null) {
            adView.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(data.get(position).songName);

            if (count == colorList.length) {
                count = 0;
            }

            cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorList[count]));
            count++;
        } else {
            cardView.setVisibility(View.GONE);
            txt_title.setVisibility(View.GONE);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    cardView.setVisibility(View.VISIBLE);
                    adView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.e("Ad Failed", errorCode + " Failed");
                    cardView.setVisibility(View.GONE);
                    adView.setVisibility(View.GONE);
                }
            });
        }
        return v;
    }
}
