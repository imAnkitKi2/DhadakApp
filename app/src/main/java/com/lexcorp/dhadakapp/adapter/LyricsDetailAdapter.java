package com.lexcorp.dhadakapp.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lexcorp.dhadakapp.R;
import com.lexcorp.dhadakapp.model.LyricsListModel;

import java.util.ArrayList;
import java.util.Random;

public class LyricsDetailAdapter extends ArrayAdapter<LyricsListModel> {
    Context context;

    public LyricsDetailAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        final ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.lyrics_list, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        LyricsListModel datalist = getItem(position);
        holder.description.setText(Html.fromHtml(datalist.lyrics));

        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, holder.description.getText().toString());
                try {
                    context.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, holder.description.getText().toString());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

        holder.coppy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", holder.description.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Text copied", Toast.LENGTH_SHORT).show();
            }
        });
        return contentView;
    }

    private static class ViewHolder {
        private TextView description;
        private ImageView whatsapp, share, coppy;
        private LinearLayout backcolor;

        public ViewHolder(View view) {
            this.description = (TextView) view.findViewById(R.id.description);
            this.backcolor = (LinearLayout) view.findViewById(R.id.backcolor);
            backcolor.setBackgroundColor(getRandomColor());
            this.whatsapp = (ImageView) view.findViewById(R.id.whatsapp);
            this.coppy = (ImageView) view.findViewById(R.id.coppy);
            this.share = (ImageView) view.findViewById(R.id.share);
//            this.favorite = (ImageView) view.findViewById(R.id.favorite);

        }

        private int getRandomColor() {
            Random rnd = new Random();
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }
    }
}