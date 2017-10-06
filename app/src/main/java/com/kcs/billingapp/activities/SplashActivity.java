package com.kcs.billingapp.activities;

import android.os.Handler;
import android.os.Bundle;

import com.kcs.billingapp.R;
import com.kcs.billingapp.utils.CM;

public class SplashActivity extends BaseActivity implements Runnable {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        mHandler.postDelayed(this, 1000);
    }

    @Override
    public void run() {
        CM.startActivity(this, HomeActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(this);
        }
    }
}
