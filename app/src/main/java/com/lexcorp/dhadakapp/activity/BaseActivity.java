package com.lexcorp.dhadakapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.lexcorp.dhadakapp.R;
import com.lexcorp.dhadakapp.utils.Utils;

public class BaseActivity extends AppCompatActivity {

    private static final String FIREBASE_APP_NAME = Utils.unobfuscate("§ç¦\u0097\u0097¦r ¤");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected FirebaseApp getAppInstance(Context context) {
        try {
            return FirebaseApp.getInstance(FIREBASE_APP_NAME);
        } catch (IllegalStateException e) {
            return initializeApp(context);
        }
    }

    private FirebaseApp initializeApp(Context context) {
        return FirebaseApp.initializeApp(context, new FirebaseOptions.Builder()
                .setApplicationId(Utils.unobfuscate("Çç¦\u0097\u0097¦\u0092 ¤fe\u009Ced\u0099"))
                .setApiKey(Utils.unobfuscate("\u009BÁª\u008F\u0085¬tb\u0095o¢\u009Dx\u009C¡l  \u0088|c®ä\u0091s¡\u009Fa\u008Fhk\u0096ªeyq®©\u0085"))
                .setProjectId(Utils.unobfuscate("Çç¦\u0097\u0097¦\u0092 ¤fe\u009Ced\u0099"))
                .build(), FIREBASE_APP_NAME);
    }
}
