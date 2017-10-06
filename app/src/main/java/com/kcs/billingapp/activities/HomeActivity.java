package com.kcs.billingapp.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.kcs.billingapp.R;
import com.kcs.billingapp.databinding.ActivityHomeBinding;
import com.kcs.billingapp.databinding.NavHeaderHomeBinding;
import com.kcs.billingapp.fragments.ListBillFragment;
import com.kcs.billingapp.fragments.ProductFragment;
import com.kcs.billingapp.models.BillDetailModel;
import com.kcs.billingapp.models.BillHeaderModel;
import com.kcs.billingapp.viewmodels.HeaderViewModel;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private ActivityHomeBinding mHomeBinding;
    private BillHeaderModel mBillHeader;
    private ArrayList<BillDetailModel> mListDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        final NavHeaderHomeBinding headerBinding =
                DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_home, mHomeBinding.navView, false);
        headerBinding.setHeaderViewModel(new HeaderViewModel(this));
        mHomeBinding.navView.addHeaderView(headerBinding.getRoot());
        init();
    }

    public BillHeaderModel getmBillHeader() {
        return (mBillHeader == null) ? mBillHeader = new BillHeaderModel() : mBillHeader;
    }

    public ArrayList<BillDetailModel> getmListDetailModel() {
        return (mListDetailModel == null) ? mListDetailModel = new ArrayList<>() : mListDetailModel;
    }

    public void setmListDetailModel(ArrayList<BillDetailModel> current_detail) {
        this.mListDetailModel = current_detail;
    }

    public void setmBillHeader(BillHeaderModel mBillHeader) {
        this.mBillHeader = mBillHeader;
    }

    /**
     * removes temp data of bill after it saves and discarded
     */
    public void emptyCurrentBill() {
        mBillHeader = null;
        mListDetailModel = null;
    }

    public ActivityHomeBinding getmHomeBinding() {
        return mHomeBinding;
    }

    private void init() {
        mToolbar = mHomeBinding.appBar.toolbar;
        setSupportActionBar(mToolbar);

        mDrawerLayout = mHomeBinding.drawerLayout;
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        mToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final NavigationView nav = mHomeBinding.navView;
        nav.setNavigationItemSelectedListener(this);

        replaceFragment(new ListBillFragment());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.menu_product_master:
                replaceFragment(new ProductFragment());
                break;
            case R.id.menu_add_bill:
                replaceFragment(new ListBillFragment());
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            hideKeyboard();
            if (getLocalFragmentManager().getBackStackEntryCount() > 0) {
                getLocalFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * sets the title in the actionbar and handle back flag in toolbar
     *
     * @param title     sets as text in actionbar (toolbar)
     * @param back_flag add or remove back arrow in actionbar
     */
    public void setToolBar(String title, boolean back_flag) {
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setTitle(title);
        mToolbar.getMenu().clear();
        if (back_flag) {
            mToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            mToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        }
    }



}
