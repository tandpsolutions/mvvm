package com.kcs.billingapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kcs.billingapp.R;
import com.kcs.billingapp.activities.BaseActivity;
import com.kcs.billingapp.databinding.RowProductListBinding;
import com.kcs.billingapp.models.ProductModel;

import java.util.ArrayList;

/**
 * Created by bhaumik.shah on 03-Aug-17.
 */

public class ProductArrayAdapter extends ArrayAdapter<ProductModel> {

    private ArrayList<ProductModel> mListProductModel;

    public ProductArrayAdapter(BaseActivity context, int resouceId, int textviewId, ArrayList<ProductModel> list) {
        super(context, resouceId, textviewId, list);
        this.mListProductModel = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RowProductListBinding rowProductListBinding;
        if (convertView == null) {
            rowProductListBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.row_product_list, parent, false);
            convertView = rowProductListBinding.getRoot();
        } else {
            rowProductListBinding = (RowProductListBinding) convertView.getTag();
        }
        rowProductListBinding.setProductModel(mListProductModel.get(position));
        convertView.setTag(rowProductListBinding);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return mListProductModel.get(position).getId();
    }

    @Nullable
    @Override
    public ProductModel getItem(int position) {
        return mListProductModel.get(position);
    }

    @Override
    public int getCount() {
        return mListProductModel.size();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RowProductListBinding rowProductListBinding;
        if (convertView == null) {
            rowProductListBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.row_product_list, parent, false);
            convertView = rowProductListBinding.getRoot();
        } else {
            rowProductListBinding = (RowProductListBinding) convertView.getTag();
        }
        rowProductListBinding.setProductModel(mListProductModel.get(position));
        convertView.setTag(rowProductListBinding);
        return convertView;
    }
}
