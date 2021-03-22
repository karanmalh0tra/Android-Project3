package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mButton ;
    private static final String BROADCAST_ACTION = "com.example.SendBroadcast";
    private static final String OPEN_A2 = "android.intent.action.OPEN_APP_A2";

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
                checkPermissionAndOpenApp2();
            }
        }) ;
    }

    private void checkPermissionAndOpenApp2() {
        if (checkSelfPermission(APP3_PERMISSION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mReceiver == null){
                Log.i(TAG, "checkPermissionAndBroadcast: registering receiver");
                mReceiver = new MyReceiver();
                mFilter = new IntentFilter();
                mFilter.addAction(BROADCAST_ACTION);
                mFilter.setPriority(10);
                registerReceiver(mReceiver, mFilter);
            }
                
            // start A2
            Log.i(TAG, "checkPermissionAndOpenApp2: creating intent now to open A2");
            Intent a2Intent = new Intent();
            a2Intent.setAction(OPEN_A2);
            startActivity(a2Intent);

        }else{
            requestPermissions(new String[]{APP3_PERMISSION}, APP3_PERMISSION_CODE);
        }

    }

    public void onRequestPermissionsResult(int code, String[] permissions, int[] results) {
        if (results.length > 0) {
            if (code == APP3_PERMISSION_CODE && results[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionAndOpenApp2();
                Log.i(TAG, "onRequestPermissionsResult: Granted");
            }
            else {
                Log.i(TAG, "onRequestPermissionsResult: Denied");
                Toast.makeText(this, "Bummer: No permission", Toast.LENGTH_SHORT)
                        .show();
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