package com.kcs.billingapp.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kcs.billingapp.R;
import com.kcs.billingapp.activities.HomeActivity;
import com.kcs.billingapp.databinding.FragmentAddBillBinding;
import com.kcs.billingapp.viewmodels.AddBillViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBillFragment extends BaseFragment {

    private FragmentAddBillBinding mAddBillBinding;
    private AddBillViewModel mAddBillViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        mAddBillBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_bill, container, false);
        mAddBillViewModel = new AddBillViewModel(this, mAddBillBinding);
        mAddBillBinding.setAddBillViewModel(mAddBillViewModel);
        mAddBillBinding.setBillHeaderModel(((HomeActivity) getActivity()).getmBillHeader());
        return mAddBillBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_cart, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.item_cart:
                if (mAddBillViewModel.validateForm()) {
                    ((HomeActivity) getActivity()).setmBillHeader(mAddBillBinding.getBillHeaderModel());
                    ((HomeActivity) getActivity()).addFragment(this, new BillDetailFragment());
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initActionBar() {
        ((HomeActivity) getActivity()).setToolBar(getString(R.string.bill_entry), true);
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initActionBar();
        }
    }
}
