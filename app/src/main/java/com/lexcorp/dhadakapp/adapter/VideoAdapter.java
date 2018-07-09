package com.lexcorp.dhadakapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.lexcorp.dhadakapp.R;
import com.lexcorp.dhadakapp.model.VideoListModel;

import java.util.ArrayList;
/**
 * Created by Boss on 7/3/2017.
 */
public class VideoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<VideoListModel> data;

    public VideoAdapter(Context context, ArrayList<VideoListModel> list) {
        this.context = context;
        this.data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public VideoListModel getItem(int position) {
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.video_item_list, viewGroup, false);
        } else {
            v = (View) convertView;
        }
        ImageView img_thuumb = (ImageView) v.findViewById(R.id.img_thumb);
        TextView txt_title = (TextView) v.findViewById(R.id.tv_video_title);
        final AdView adView = (AdView) v.findViewById(R.id.adView);
        adView.setVisibility(View.GONE);
        txt_title.setText(data.get(position).title);
        String img = "http://img.youtube.com/vi/" + data.get(position).youtubeCode + "/hqdefault.jpg";
        Glide.with(context).load(img).into(img_thuumb);
        return v;
    }

    public void doRefresh(ArrayList<VideoListModel> list) {
        this.data = list;
        notifyDataSetChanged();
    }
}
