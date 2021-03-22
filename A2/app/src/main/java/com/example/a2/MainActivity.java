package com.example.a2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mButton ;
    private static final String BROADCAST_ACTION = "com.example.SendBroadcast";
    private static final String OPEN_A3 = "android.intent.action.OPEN_APP_A3";

    private static final String APP3_PERMISSION =
            "edu.uic.cs478.s19.kaboom";
    private static final int APP3_PERMISSION_CODE =
            0;

    BroadcastReceiver mReceiver;
    IntentFilter mFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button) ;
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkPermissionAndOpenApp3();
            }
        }) ;
    }

    private void checkPermissionAndOpenApp3() {
        if (checkSelfPermission(APP3_PERMISSION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mReceiver == null){
                Log.i(TAG, "checkPermissionAndBroadcast: ");
                mReceiver = new MyReceiver();
                mFilter = new IntentFilter();
                mFilter.addAction(BROADCAST_ACTION);
                mFilter.setPriority(100);
                registerReceiver(mReceiver, mFilter);
            }

            // start A3
            Intent a3Intent = new Intent();
            a3Intent.setAction(OPEN_A3);
            startActivity(a3Intent);
        }else{
            requestPermissions(new String[]{APP3_PERMISSION}, APP3_PERMISSION_CODE);
        }

    }

    public void onRequestPermissionsResult(int code, String[] permissions, int[] results) {
        if (results.length > 0) {
            if (code == APP3_PERMISSION_CODE && results[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionAndOpenApp3();
                Log.i(TAG, "onRequestPermissionsResult: Granted");
            }
            else {
                Log.i(TAG, "onRequestPermissionsResult: Denied");
                Toast.makeText(this, "Bummer: No permission for A2", Toast.LENGTH_SHORT)
                        .show();
                 finish();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
    }
}