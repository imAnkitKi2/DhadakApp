package com.lexcorp.dhadakapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Boss on 10/3/2017.
 */

public class RegularTextView extends TextView {

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        if(!TextUtils.isEmpty(Constants.REGULAR_FONT)){
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Constants.REGULAR_FONT);
            setTypeface(tf);
        }
    }
}
