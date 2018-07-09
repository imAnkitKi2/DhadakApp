package com.lexcorp.dhadakapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lexcorp.dhadakapp.R;
import com.lexcorp.dhadakapp.model.LyricsListModel;
import com.lexcorp.dhadakapp.model.MoviesModel;

import java.util.ArrayList;

/**
 * Created by Boss on 7/3/2017.
 */

public class MoviesAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<MoviesModel> data;
    private ArrayList<MoviesModel> dataListFiltered;
    private int[] colorList = new int[]{R.color.Tomato, R.color.LightCoral, R.color.peru, R.color.DarkCyan};
    private int count = 0;

    public MoviesAdapter(Context context, ArrayList<MoviesModel> list) {
        this.context = context;
        this.data = list;
        this.dataListFiltered = list;
    }

    @Override
    public int getCount() {
        return dataListFiltered.size();
    }

    @Override
    public MoviesModel getItem(int position) {
        return dataListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.movie_item_list, viewGroup, false);
        } else {
            v = (View) convertView;
        }
        TextView txt_title = (TextView) v.findViewById(R.id.tv_name);
        final AdView adView = (AdView) v.findViewById(R.id.adView);
        final CardView cardView = (CardView) v.findViewById(R.id.card_view);

        if (dataListFiltered.get(position) != null) {
            adView.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(dataListFiltered.get(position).movieName);
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

    public ArrayList<MoviesModel> getData() {
        return dataListFiltered;
    }

    public void doRefresh(ArrayList<MoviesModel> list) {
        this.dataListFiltered = list;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = data;
                } else {
                    ArrayList<MoviesModel> filteredList = new ArrayList<>();
                    for (MoviesModel row : data) {
                        if (row.movieName.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList<MoviesModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
