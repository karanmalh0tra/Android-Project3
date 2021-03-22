package com.example.a1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Companion receiver", "Companion Receiver called into action!");
        Bundle bundle = intent.getExtras();
        bundle.getString("TitlesURL");

        Log.i(TAG, "onReceive: URL is "+bundle.getString("TitlesURL"));

        Intent bIntent = new Intent();
        bIntent.setClassName("com.example.a1","com.example.a1.WebViewActivity"); //setClassName
        bIntent.putExtra("TitlesURL",bundle.getString("TitlesURL"));
        context.startActivity(bIntent);
    }
}