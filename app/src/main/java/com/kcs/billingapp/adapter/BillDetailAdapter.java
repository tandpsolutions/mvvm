package com.kcs.billingapp.adapter;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kcs.billingapp.R;
import com.kcs.billingapp.activities.HomeActivity;
import com.kcs.billingapp.databinding.RowBillDetailBinding;
import com.kcs.billingapp.fragments.BaseFragment;
import com.kcs.billingapp.models.BillDetailModel;
import com.kcs.billingapp.models.BillHeaderModel;
import com.kcs.billingapp.utils.CM;

import java.util.ArrayList;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.BillDetailHolder> {

    private ArrayList<BillDetailModel> mListBillDetail;
    private BaseFragment mBaseFragment;


    public BillDetailAdapter(BaseFragment baseFragment, ArrayList<BillDetailModel> mListBillDetail) {
        this.mListBillDetail = mListBillDetail;
        this.mBaseFragment = baseFragment;
    }

    @Override
    public BillDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RowBillDetailBinding billDetailBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_bill_detail, parent, false);
        return new BillDetailHolder(billDetailBinding);
    }

    @Override
    public void onBindViewHolder(final BillDetailHolder holder, final int position) {
        holder.billDetailBinding.setBilldetailmodel(mListBillDetail.get(position));
        holder.billDetailBinding.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CM.showDialog(mBaseFragment.getActivity(), "Do you want to delete this item from bill?", "Yes", "No"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final BillHeaderModel header = ((HomeActivity) mBaseFragment.getActivity()).getmBillHeader();
                                header.setTotal(header.getTotal() - (mListBillDetail.get(position).getAmt()));
                                mListBillDetail.remove(position);
                                notifyDataSetChanged();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return mListBillDetail.size();
    }

    public class BillDetailHolder extends RecyclerView.ViewHolder {

        private RowBillDetailBinding billDetailBinding;

        public BillDetailHolder(RowBillDetailBinding itemView) {
            super(itemView.getRoot());
            billDetailBinding = itemView;
        }
    }


}
