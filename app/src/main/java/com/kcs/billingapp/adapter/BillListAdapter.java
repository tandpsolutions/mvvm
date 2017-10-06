package com.kcs.billingapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kcs.billingapp.R;
import com.kcs.billingapp.databinding.RowBillHeaderBinding;
import com.kcs.billingapp.models.BillHeaderModel;

import java.util.ArrayList;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class BillListAdapter extends RecyclerView.Adapter<BillListAdapter.ProductHolder> {

    private ArrayList<BillHeaderModel> mListBillHeader;
    private OnItemClickListener onItemClickListener;


    public BillListAdapter(ArrayList<BillHeaderModel> mListBillHeader, OnItemClickListener onItemClickListener) {
        this.mListBillHeader = mListBillHeader;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RowBillHeaderBinding listBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_bill_header, parent, false);
        return new ProductHolder(listBinding);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {
        holder.rowBillHeaderBinding.setBillheader(mListBillHeader.get(position));
        holder.rowBillHeaderBinding.editBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(holder.rowBillHeaderBinding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListBillHeader.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        private RowBillHeaderBinding rowBillHeaderBinding;

        public ProductHolder(RowBillHeaderBinding itemView) {
            super(itemView.getRoot());
            rowBillHeaderBinding = itemView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RowBillHeaderBinding item);
    }
}
