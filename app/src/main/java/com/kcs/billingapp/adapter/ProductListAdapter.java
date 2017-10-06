package com.kcs.billingapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kcs.billingapp.R;
import com.kcs.billingapp.databinding.RowProductListBinding;
import com.kcs.billingapp.models.ProductModel;

import java.util.ArrayList;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductHolder> {

    private ArrayList<ProductModel> mListProductModel;
    private OnItemClickListener onItemClickListener;


    public ProductListAdapter(ArrayList<ProductModel> mListProductModel, OnItemClickListener onItemClickListener) {
        this.mListProductModel = mListProductModel;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RowProductListBinding productListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_product_list, parent, false);
        return new ProductHolder(productListBinding);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {
        holder.productListBinding.setProductModel(mListProductModel.get(position));
        holder.productListBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(holder.productListBinding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListProductModel.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        private RowProductListBinding productListBinding;

        public ProductHolder(RowProductListBinding itemView) {
            super(itemView.getRoot());
            productListBinding = itemView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RowProductListBinding item);
    }
}
