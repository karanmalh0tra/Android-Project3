package com.example.a3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity implements
        TitlesFragment.ListSelectionListener {

    private static final String TAG = "MainActivity";
    private static final String BROADCAST_ACTION = "com.example.SendBroadcast";

    private int currentOrientation;

    private static final String TOAST_INTENT =
            "edu.uic.cs478.BroadcastReceiver3.showToast";

    private static final String APP3_PERMISSION =
            "edu.uic.cs478.s19.kaboom";

    public static TypedArray mImageLinks;
    public static String[] mTitleArray;
    public static String[] mTitlesURLArray;
    public static int arrayIndex;

//    private TitlesFragment mTitlesFragment;
    private ImagesFragment mImagesFragment;
    private TitlesFragment mTitlesFragment;
//    private ImagesFragment mImagesFragment;

    FragmentManager mFragmentManager;

    private FrameLayout mTitleFrameLayout, mImageFrameLayout;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: first call");

        mTitleArray = getResources().getStringArray(R.array.Titles);
        mTitlesURLArray = getResources().getStringArray(R.array.TitlesURL);
        mImageLinks = getResources().obtainTypedArray(R.array.Images);
        currentOrientation = getResources().getConfiguration().orientation;

        setContentView(R.layout.activity_main);

        // Get references to the TitleFragment and to the QuotesFragment
        mTitleFrameLayout = (FrameLayout) findViewById(R.id.titles);
        mImageFrameLayout = (FrameLayout) findViewById(R.id.tv_images);

        // Get a reference to the SupportFragmentManager instead of original FragmentManager
        mFragmentManager = getSupportFragmentManager();

        // Start a new FragmentTransaction
        FragmentTransaction fragmentTransaction =mFragmentManager.beginTransaction();

        // Add a OnBackStackChangedListener to reset the layout when the back stack changes
        mFragmentManager.addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setLayout();
                    }
                });

        mTitlesFragment = (TitlesFragment) mFragmentManager.findFragmentByTag("mTitlesFragment");
        if (mTitlesFragment == null){
            Log.i(TAG, "onCreate: new mTitlesFragment");
            mTitlesFragment = new TitlesFragment();
            // Add the TitleFragment to the layout
            fragmentTransaction.add(R.id.titles,
                    mTitlesFragment,"mTitlesFragment");

            // Commit the FragmentTransaction
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
        }

        mImagesFragment = (ImagesFragment) mFragmentManager.findFragmentByTag("mImagesFragment");

        if (mImagesFragment == null) {
            Log.i(TAG, "onCreate: new mImagesFragment");
            mImagesFragment = new ImagesFragment();
        } else {
            if(!mImagesFragment.isAdded()){
                // Add the ImagesFragment to the layout
                fragmentTransaction.add(R.id.tv_images,
                        mImagesFragment,"mImagesFragment");

                // Add this FragmentTransaction to the back-stack
                fragmentTransaction.addToBackStack(null);

                // Commit the FragmentTransaction
                fragmentTransaction.commit();

                // Force Android to execute the committed FragmentTransaction
                mFragmentManager.executePendingTransactions();
            }
            setLayout();
        }

        if (savedInstanceState != null){
            arrayIndex = savedInstanceState.getInt("ArrayIndex");
        }

    }

    private void setLayout() {
        // Determine whether the ImagesFragment has been added
        if (!mImagesFragment.isAdded()) {
            // Make the TitlesFragment occupy the entire layout
            mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            mImageFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT));

        } else {
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscape
                // Make the TitlesLayout take 1/3 of the layout's width
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));

                // Make the ImagesLayout take 2/3's of the layout's width
                mImageFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
            } else {
                // In portrait
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT));

                mImageFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                        MATCH_PARENT));
            }
        }
    }

    // Create Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu: before inflating");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_tv_show);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        return true; //display menu in host activity now
    }

    //Process clicks on Options Menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.launch:
                if(!mImagesFragment.isAdded()){
                    Toast.makeText(this, "No tv show selected",
                            Toast.LENGTH_LONG).show();
                } else{
                    Log.i(TAG, "onOptionsItemSelected: arrayIndex is "+arrayIndex);
                    Intent aIntent = new Intent();
                    aIntent.setAction(BROADCAST_ACTION);
//                    aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    aIntent.putExtra("TitlesURL",mTitlesURLArray[arrayIndex]);
                    sendOrderedBroadcast(aIntent, APP3_PERMISSION);
                }
                return true; //done processing this menu selection
            case R.id.exit:
                finishAndRemoveTask();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onListSelection(int index) {
        arrayIndex = index;
        Log.i(TAG, "onListSelection: index value is "+index);
        // If the ImagesFragment has not been added, add it now
        if (!mImagesFragment.isAdded()) {
            // Start a new FragmentTransaction
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();

            // Add the ImagesFragment to the layout
            fragmentTransaction.add(R.id.tv_images,
                    mImagesFragment,"mImagesFragment");

            // Add this FragmentTransaction to the back-stack
            fragmentTransaction.addToBackStack(null);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

            // Force Android to execute the committed FragmentTransaction
            mFragmentManager.executePendingTransactions();
        }

        if (mImagesFragment.getShownIndex() != index) {
            // Tell the ImagesFragment to show the image at position index
            mImagesFragment.showImageAtIndex(index);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ArrayIndex",arrayIndex);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":onPause()");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":onResume()");
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":onStop()");
        super.onStop();
    }
}