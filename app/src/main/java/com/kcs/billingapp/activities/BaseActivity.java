package com.kcs.billingapp.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kcs.billingapp.R;
import com.kcs.billingapp.fragments.BaseFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BaseActivity extends AppCompatActivity {

    private FragmentManager localFragmentManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocalFragmentManager(getSupportFragmentManager());
    }


    /**
     * Gets the fragment manager object of activity required for fragment transaction
     * <p>This method can be customised on the need of application,in which it returns {@link FragmentManager} or {@link android.support.v4.app.FragmentManager}</p>
     *
     * @return object of {@link FragmentManager} or {@link android.support.v4.app.FragmentManager}
     */
    public FragmentManager getLocalFragmentManager() {
        return localFragmentManager;
    }


    public void setLocalFragmentManager(FragmentManager localFragmentManager) {
        this.localFragmentManager = localFragmentManager;
    }


    /***
     * Add new fragment in given container.
     * <p>
     * This method will add new fragment in container and hide the current fragment.
     * And also will add current fragment in backstack.
     * </p>
     *
     * @param oldFragment This parameter will take fragmnet name which you want to hide.
     * @param newFragment  This parameter will take new fragment name which need to be add.
     */
    public void addFragment(BaseFragment oldFragment, BaseFragment newFragment) {
        getLocalFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, newFragment, newFragment.getClass().getSimpleName())
                .hide(oldFragment)
                .addToBackStack(oldFragment.getClass().getSimpleName())
                .commit();
    }

    /**
     * removes current fragment from container and replace with the new Fragment recieves in parameter
     *
     * @param newFragment a fragment object that replaces current fragment
     */
    public void replaceFragment(BaseFragment newFragment) {
        getLocalFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, newFragment, newFragment.getClass().getSimpleName())
                .commit();
    }


    /**
     * removes all backstack fragment from container and replace with the new Fragment recieves in parameter
     *
     * @param newFragment a fragment object that replaces current fragment
     */
    public void replaceFragmentClearStack(BaseFragment newFragment) {
        getLocalFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getLocalFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, newFragment, newFragment.getClass().getSimpleName())
                .commit();
    }


    /**
     * hides the soft input keyboard from screen
     */
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
