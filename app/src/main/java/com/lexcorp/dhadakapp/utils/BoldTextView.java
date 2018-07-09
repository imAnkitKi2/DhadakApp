package com.lexcorp.dhadakapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Boss on 10/3/2017.
 */

public class BoldTextView extends TextView {

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        //"semplicita.light.otf"
        if(!TextUtils.isEmpty(Constants.BOLD_FONT)){
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Constants.BOLD_FONT);
            setTypeface(tf);
        }
    }
}
