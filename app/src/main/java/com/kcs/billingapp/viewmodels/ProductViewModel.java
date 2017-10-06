package com.kcs.billingapp.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.kcs.billingapp.BR;
import com.kcs.billingapp.activities.BaseActivity;
import com.kcs.billingapp.adapter.ProductListAdapter;
import com.kcs.billingapp.database.Tbl_Product;
import com.kcs.billingapp.databinding.FragmentProductBinding;
import com.kcs.billingapp.databinding.RowProductListBinding;
import com.kcs.billingapp.fragments.AddProductFragment;
import com.kcs.billingapp.fragments.BaseFragment;
import com.kcs.billingapp.models.ProductModel;

import java.util.ArrayList;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class ProductViewModel extends BaseObservable {

    private BaseFragment mBaseFragment;
    private FragmentProductBinding mProductBinding;
    private ProductListAdapter mProductListAdapter;
    private boolean refreshing = false;

    public ProductViewModel(BaseFragment mBaseFragment, FragmentProductBinding mProductBinding) {
        this.mBaseFragment = mBaseFragment;
        this.mProductBinding = mProductBinding;
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
     * gets the data from prdtmst table and populate in the
     * {@link android.support.v7.widget.RecyclerView} of {@link com.kcs.billingapp.fragments.ProductFragment}
     */
    public void setData() {
        final Tbl_Product tbl_product = new Tbl_Product(mBaseFragment.getActivity());
        ArrayList<ProductModel> products = tbl_product.getAllProducts();
        mProductListAdapter = new ProductListAdapter(products, new ProductListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RowProductListBinding item) {
                final ProductModel productModel = item.getProductModel();
                final AddProductFragment addProductFragment = new AddProductFragment();
                final Bundle bundle = new Bundle();
                bundle.putLong(Tbl_Product.ID, productModel.getId());
                bundle.putString(Tbl_Product.PRODUCT_NAME, productModel.getProduct_name());
                bundle.putDouble(Tbl_Product.PRODUCT_RATE, productModel.getRate());

                addProductFragment.setArguments(bundle);
                ((BaseActivity) mBaseFragment.getActivity()).addFragment(mBaseFragment, addProductFragment);
            }
        });
        mProductBinding.productRvList.setAdapter(mProductListAdapter);
        mProductListAdapter.notifyDataSetChanged();
        setRefreshing(false);
    }


}
