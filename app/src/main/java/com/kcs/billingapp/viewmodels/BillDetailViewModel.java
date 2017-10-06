package com.kcs.billingapp.viewmodels;

import android.databinding.BaseObservable;

import com.kcs.billingapp.activities.HomeActivity;
import com.kcs.billingapp.adapter.BillDetailAdapter;
import com.kcs.billingapp.databinding.FragmentBillDetailBinding;
import com.kcs.billingapp.fragments.BaseFragment;

/**
 * Created by bhaumik.shah on 03-Aug-17.
 */

public class BillDetailViewModel extends BaseObservable {

    private FragmentBillDetailBinding mBillDetailBinding;
    private BaseFragment mBaseFragment;
    private BillDetailAdapter mBillDetailAdapter;

    public BillDetailViewModel(FragmentBillDetailBinding mBillDetailBinding, BaseFragment mBaseFragment) {
        this.mBillDetailBinding = mBillDetailBinding;
        this.mBaseFragment = mBaseFragment;
    }

    /**
     * show data from the temp arraylist from {@link HomeActivity} and show it in
     * {@link android.support.v7.widget.RecyclerView} of {@link com.kcs.billingapp.fragments.BillDetailFragment}
     */
    public void setData() {
        mBillDetailAdapter = new BillDetailAdapter(mBaseFragment, ((HomeActivity) mBaseFragment.getActivity()).getmListDetailModel());
        mBillDetailBinding.rvDetailList.setAdapter(mBillDetailAdapter);
        mBillDetailAdapter.notifyDataSetChanged();
    }
}
