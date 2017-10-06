package com.kcs.billingapp.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.kcs.billingapp.BR;
import com.kcs.billingapp.activities.BaseActivity;
import com.kcs.billingapp.activities.HomeActivity;
import com.kcs.billingapp.adapter.BillListAdapter;
import com.kcs.billingapp.database.Tbl_Bill;
import com.kcs.billingapp.databinding.FragmentListBillBinding;
import com.kcs.billingapp.databinding.RowBillHeaderBinding;
import com.kcs.billingapp.fragments.AddBillFragment;
import com.kcs.billingapp.fragments.BaseFragment;
import com.kcs.billingapp.models.BillHeaderModel;

import java.util.ArrayList;

/**
 * Created by bhaumik.shah on 03-Aug-17.
 */

public class ListBillViewModel extends BaseObservable {
    private BaseFragment mBaseFragment;
    private FragmentListBillBinding mListBillBinding;
    private boolean refreshing = false;

    public ListBillViewModel(BaseFragment mBaseFragment, FragmentListBillBinding mListBillBinding) {
        this.mBaseFragment = mBaseFragment;
        this.mListBillBinding = mListBillBinding;

    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
            }
        };
    }

    @Bindable
    public boolean isRefreshing() {
        return refreshing;
    }

    private void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        notifyPropertyChanged(BR.refreshing);
    }

    /**
     * gets the data from the billhd table and populate in the
     * {@link android.support.v7.widget.RecyclerView} of {@link com.kcs.billingapp.fragments.ListBillFragment}
     */
    public void setData() {
        final Tbl_Bill tbl_bill = new Tbl_Bill(mListBillBinding.billRvList.getContext());
        final ArrayList<BillHeaderModel> headers = tbl_bill.getBillHeader();
        final BillListAdapter listAdapter = new BillListAdapter(headers, new BillListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RowBillHeaderBinding item) {
                final Tbl_Bill tbl_bill = new Tbl_Bill(mBaseFragment.getActivity());
                ((HomeActivity) mBaseFragment.getActivity()).setmBillHeader(item.getBillheader());
                ((HomeActivity) mBaseFragment.getActivity()).setmListDetailModel(tbl_bill.getDetailModels(item.getBillheader().getBill_No()));
                ((BaseActivity) mBaseFragment.getActivity()).addFragment(mBaseFragment, new AddBillFragment());
            }
        });
        mListBillBinding.billRvList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        setRefreshing(false);
    }
}
