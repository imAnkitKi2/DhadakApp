package com.lexcorp.dhadakapp;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;

public class MoviesApplication extends MultiDexApplication {

    private static MoviesApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, "ca-app-pub-3252068940864081~8924179277");
        MultiDex.install(this);
        mInstance = this;
    }

    public static synchronized MoviesApplication getInstance() {
        return mInstance;
    }
}
