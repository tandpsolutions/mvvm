package com.kcs.billingapp.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kcs.billingapp.R;
import com.kcs.billingapp.activities.BaseActivity;
import com.kcs.billingapp.activities.HomeActivity;
import com.kcs.billingapp.databinding.FragmentListBillBinding;
import com.kcs.billingapp.viewmodels.ListBillViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListBillFragment extends BaseFragment {

    private FragmentListBillBinding mListBillBinding;
    private ListBillViewModel mListBillViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        mListBillBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_bill, container, false);
        mListBillViewModel = new ListBillViewModel(this, mListBillBinding);
        mListBillBinding.setListViewModel(mListBillViewModel);

        return mListBillBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_add, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.add_product:
                ((HomeActivity) getActivity()).emptyCurrentBill();
                ((BaseActivity) getActivity()).addFragment(this, new AddBillFragment());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initActionBar() {
        ((HomeActivity) getActivity()).setToolBar(getString(R.string.bill_listing), false);
    }

    @Override
    public void initView(View view) {
        final LinearLayoutManager manager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        mListBillBinding.billRvList.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mListBillBinding.billRvList.getContext(),
                manager.getOrientation());
        mListBillBinding.billRvList.addItemDecoration(dividerItemDecoration);
        mListBillViewModel.setData();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initActionBar();
            mListBillViewModel.setData();
        }
    }
}
