package com.kcs.billingapp.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kcs.billingapp.R;
import com.kcs.billingapp.activities.HomeActivity;
import com.kcs.billingapp.database.Tbl_Product;
import com.kcs.billingapp.databinding.FragmentAddProductBinding;
import com.kcs.billingapp.models.ProductModel;
import com.kcs.billingapp.viewmodels.AddProductViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends BaseFragment {
    private FragmentAddProductBinding mAddProductBinding;
    private ProductModel mProductModel;
    private AddProductViewModel mAddProductViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Bundle bundle = getArguments();
        mProductModel = new ProductModel();
        mProductModel.setProduct_name(bundle.getString(Tbl_Product.PRODUCT_NAME));
        mProductModel.setId(bundle.getLong(Tbl_Product.ID));
        mProductModel.setRate(bundle.getDouble(Tbl_Product.PRODUCT_RATE));

        mAddProductBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product, container, false);
        mAddProductViewModel = new AddProductViewModel(mProductModel, this, mAddProductBinding);
        mAddProductBinding.setAddProductViewModel(mAddProductViewModel);
        return mAddProductBinding.getRoot();
    }

    @Override
    public void initActionBar() {
        ((HomeActivity) getActivity()).setToolBar(getString(R.string.add_product), true);
    }

    @Override
    public void initView(View view) {
    }

    @Override
    public void onClick(View view) {

    }
}
