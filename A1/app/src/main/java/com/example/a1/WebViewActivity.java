package com.example.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";
    private static String TitlesURL = "";
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        if (savedInstanceState != null){
            Log.i(TAG, "onCreate: ");
            webView.restoreState(savedInstanceState);
            Log.i(TAG, "onCreate: "+TitlesURL);
        }
        else {
            TitlesURL = getIntent().getStringExtra("TitlesURL");
            if (TitlesURL == null){
                finish();
            }
            webView.loadUrl(TitlesURL);
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        webView.saveState(outState);
        super.onSaveInstanceState(outState);

    }

}