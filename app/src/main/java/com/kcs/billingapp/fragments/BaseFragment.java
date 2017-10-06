package com.kcs.billingapp.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.kcs.billingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private FragmentManager localChildFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setLocalChildFragmentManager(getChildFragmentManager());
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initActionBar();
    }

    public abstract void initActionBar();

    public abstract void initView(View view);

    /**
     * Gets the fragment manager object of activity required for fragment transaction
     * <p>This method can be customised on the need of application,in which it returns {@link FragmentManager} or {@link android.support.v4.app.FragmentManager}</p>
     *
     * @return object of {@link FragmentManager} or {@link android.support.v4.app.FragmentManager}
     */
    public FragmentManager getLocalChildFragmentManager() {
        return localChildFragmentManager;
    }

    public void setLocalChildFragmentManager(FragmentManager localChildFragmentManager) {
        this.localChildFragmentManager = localChildFragmentManager;
    }

    /**
     * removes current fragment from container and replace with the new Fragment recieves in parameter
     *
     * @param newFragment a fragment object that replaces current fragment
     */
    public void replceChildFragment(BaseFragment newFragment) {
        getLocalChildFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, newFragment, newFragment.getClass().getSimpleName())
                .commit();
    }
}
