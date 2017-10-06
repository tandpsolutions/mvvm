package com.kcs.billingapp.viewmodels;

import android.databinding.BaseObservable;
import android.support.v4.view.GravityCompat;
import android.view.View;

import com.kcs.billingapp.activities.HomeActivity;
import com.kcs.billingapp.fragments.ProfileFragment;

/**
 * Created by bhaumik.shah on 09-Aug-17.
 */

public class HeaderViewModel extends BaseObservable {

    private HomeActivity mHomeActivity;

    public HeaderViewModel(HomeActivity mHomeActivity) {
        this.mHomeActivity = mHomeActivity;
    }

    public View.OnClickListener imageClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.replaceFragment(new ProfileFragment());
                mHomeActivity.getmHomeBinding().drawerLayout.closeDrawer(GravityCompat.START);
            }
        };
    }
}
