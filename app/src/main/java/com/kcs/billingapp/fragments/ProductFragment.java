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
import com.kcs.billingapp.database.Tbl_Product;
import com.kcs.billingapp.databinding.FragmentProductBinding;
import com.kcs.billingapp.viewmodels.ProductViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends BaseFragment {

    private FragmentProductBinding mProproductBinding;
    private ProductViewModel mProductViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        mProproductBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false);
        mProductViewModel = new ProductViewModel(this, mProproductBinding);
        mProproductBinding.setProductviewmodel(mProductViewModel);

        return mProproductBinding.getRoot();
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
                final AddProductFragment addProductFragment = new AddProductFragment();
                final Bundle bundle = new Bundle();
                bundle.putLong(Tbl_Product.ID, 0);
                bundle.putString(Tbl_Product.PRODUCT_NAME, "");
                bundle.putDouble(Tbl_Product.PRODUCT_RATE, 0.00);
                addProductFragment.setArguments(bundle);
                ((BaseActivity) getActivity()).addFragment(this, addProductFragment);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initActionBar() {
        ((HomeActivity) getActivity()).setToolBar(getString(R.string.product_master), false);
    }

    @Override
    public void initView(View view) {
        final LinearLayoutManager manager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        mProproductBinding.productRvList.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mProproductBinding.productRvList.getContext(),
                manager.getOrientation());
        mProproductBinding.productRvList.addItemDecoration(dividerItemDecoration);
        mProductViewModel.setData();
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initActionBar();
            mProductViewModel.setData();
        }
    }


}
