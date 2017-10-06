package com.kcs.billingapp.fragments;


import android.content.DialogInterface;
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
import com.kcs.billingapp.database.Tbl_Bill;
import com.kcs.billingapp.databinding.FragmentBillDetailBinding;
import com.kcs.billingapp.utils.CM;
import com.kcs.billingapp.viewmodels.BillDetailViewModel;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillDetailFragment extends BaseFragment {

    private FragmentBillDetailBinding mBillDetailBinding;
    private BillDetailViewModel mBillDetailViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        mBillDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bill_detail, container, false);
        mBillDetailViewModel = new BillDetailViewModel(mBillDetailBinding, this);
        mBillDetailBinding.setBilldetailmodel(mBillDetailViewModel);

        return mBillDetailBinding.getRoot();
    }

    @Override
    public void initActionBar() {
        ((HomeActivity) getActivity()).setToolBar(getString(R.string.bill_entry), true);
    }

    @Override
    public void initView(View view) {
        final LinearLayoutManager manager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        mBillDetailBinding.rvDetailList.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mBillDetailBinding.rvDetailList.getContext(),
                manager.getOrientation());
        mBillDetailBinding.rvDetailList.addItemDecoration(dividerItemDecoration);
        mBillDetailViewModel.setData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_save_bill, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.save_bill:
                if (((HomeActivity) getActivity()).getmListDetailModel().size() > 0) {
                    CM.showDialog(getActivity(), getString(R.string.save_bill_confirmation), getString(R.string.yes), getString(R.string.no)
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final Tbl_Bill bills = new Tbl_Bill(getActivity());
                                    ((HomeActivity) getActivity()).getmBillHeader().setTime_stamp(new Date().getTime());
                                    bills.insertUpdateBill(((HomeActivity) getActivity()).getmBillHeader(), ((HomeActivity) getActivity()).getmListDetailModel());
                                    ((HomeActivity) getActivity()).emptyCurrentBill();
                                    ((BaseActivity) getActivity()).replaceFragmentClearStack(new ListBillFragment());
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                } else {
                    CM.showDialog(getActivity(), getString(R.string.please_add_product));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }
}
