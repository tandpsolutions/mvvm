package com.kcs.billingapp.viewmodels;

import android.databinding.BaseObservable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.kcs.billingapp.R;
import com.kcs.billingapp.activities.BaseActivity;
import com.kcs.billingapp.database.Tbl_Product;
import com.kcs.billingapp.databinding.FragmentAddProductBinding;
import com.kcs.billingapp.fragments.BaseFragment;
import com.kcs.billingapp.models.ProductModel;
import com.kcs.billingapp.models.ResultModel;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class AddProductViewModel extends BaseObservable {
    private ProductModel mProductModel;
    private BaseFragment mBaseFragment;
    private FragmentAddProductBinding mAddProductBinding;

    public AddProductViewModel(ProductModel mProductModel, BaseFragment mBaseFragment, FragmentAddProductBinding mAddProductBinding) {
        this.mProductModel = mProductModel;
        this.mBaseFragment = mBaseFragment;
        this.mAddProductBinding = mAddProductBinding;
    }

    public String getProductName() {
        return mProductModel.getProduct_name();
    }

    public long getProductID() {
        return mProductModel.getId();
    }

    public String getRate() {
        return String.valueOf(mProductModel.getRate());
    }

    public View.OnClickListener onCancelClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaseFragment.getActivity().onBackPressed();
            }
        };
    }

    /**
     * saves the product in the database
     *
     * @return the click listner to be assigned in the layout
     */
    public View.OnClickListener onSaveClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateProductName()) {
                    ((BaseActivity) mBaseFragment.getActivity()).hideKeyboard();
                    final ProductModel productModel = new ProductModel();
                    productModel.setId(getProductID());
                    productModel.setProduct_name(mAddProductBinding.addproductEtProductname.getText().toString());
                    productModel.setRate(Double.parseDouble(mAddProductBinding.addproductEtRate.getText().toString()));
                    Tbl_Product tbl_product = new Tbl_Product(mBaseFragment.getContext());
                    final ResultModel resultModel = tbl_product.insertUpdateProduct(productModel);
                    if (resultModel.getStatusCode() == ResultModel.SUCCESS) {
                        mBaseFragment.getActivity().onBackPressed();
                    } else {
                        mAddProductBinding.addproductTiProductname.setErrorEnabled(true);
                        mAddProductBinding.addproductTiProductname.setError(resultModel.getMessage());
                    }
                }
            }
        };
    }

    /**
     * checks the product name is not empty and rate for that product is greater then 0
     *
     * @return true if validation matches requirements otherwise false
     */
    private boolean validateProductName() {

        if (TextUtils.isEmpty(mAddProductBinding.addproductEtProductname.getText().toString())) {
            mAddProductBinding.addproductEtProductname.requestFocus();
            mAddProductBinding.addproductTiProductname.setErrorEnabled(true);
            mAddProductBinding.addproductTiProductname.setError(mBaseFragment.getString(R.string.enter_product_name));
            return false;
        }

        if (TextUtils.isEmpty(mAddProductBinding.addproductEtRate.getText().toString()) || Double.parseDouble(mAddProductBinding.addproductEtRate.getText().toString()) == 0) {
            mAddProductBinding.addproductEtRate.requestFocus();
            mAddProductBinding.addproductTiRate.setErrorEnabled(true);
            mAddProductBinding.addproductTiRate.setError(mBaseFragment.getString(R.string.enter_valid_rate));
            return false;
        }
        return true;
    }

    /**
     * removes error while typing on product name in {@link com.kcs.billingapp.fragments.AddProductFragment}
     *
     * @return
     */
    public TextWatcher getProductNameWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAddProductBinding.addproductTiProductname.setError(null);
                mAddProductBinding.addproductTiProductname.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    /**
     * removes error while typing on product rate in {@link com.kcs.billingapp.fragments.AddProductFragment}
     *
     * @return
     */
    public TextWatcher getRateWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAddProductBinding.addproductTiRate.setError(null);
                mAddProductBinding.addproductTiRate.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }
}
