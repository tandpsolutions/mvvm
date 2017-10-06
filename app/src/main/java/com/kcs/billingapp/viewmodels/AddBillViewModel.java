package com.kcs.billingapp.viewmodels;

import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.kcs.billingapp.R;
import com.kcs.billingapp.activities.BaseActivity;
import com.kcs.billingapp.activities.HomeActivity;
import com.kcs.billingapp.adapter.ProductArrayAdapter;
import com.kcs.billingapp.database.Tbl_Product;
import com.kcs.billingapp.databinding.FragmentAddBillBinding;
import com.kcs.billingapp.fragments.BaseFragment;
import com.kcs.billingapp.models.BillDetailModel;
import com.kcs.billingapp.models.ProductModel;
import com.kcs.billingapp.utils.CM;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by bhaumik.shah on 03-Aug-17.
 */

public class AddBillViewModel extends BaseObservable {
    private BaseFragment mBaseFragment;
    private FragmentAddBillBinding mAddBillBinding;
    private ArrayList<ProductModel> mListProductModel;

    public AddBillViewModel(BaseFragment mBaseFragment, FragmentAddBillBinding mAddBillBinding) {
        this.mBaseFragment = mBaseFragment;
        this.mAddBillBinding = mAddBillBinding;
        this.mAddBillBinding.productSpinner.setOnItemSelectedListener(getItemSelected());
        this.mAddBillBinding.itemRate.setOnFocusChangeListener(onFocusChangeListener());
        this.mAddBillBinding.billEtQty.setOnFocusChangeListener(onFocusChangeListener());
    }


    /**
     * creates adapter for {@link android.widget.Spinner} in {@link com.kcs.billingapp.fragments.AddBillFragment}
     *
     * @return ArrayAdapter of {@link ProductModel}
     */
    public ArrayAdapter<ProductModel> getSpinerAdapter() {
        mListProductModel = new Tbl_Product(mBaseFragment.getActivity()).getAllProducts();
        return new ProductArrayAdapter((BaseActivity) mBaseFragment.getActivity()
                , R.layout.row_product_list, R.id.row_product_name, mListProductModel);
    }

    /**
     * sets the rate of the product that is selected by the user
     *
     * @return
     */
    private AdapterView.OnItemSelectedListener getItemSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAddBillBinding.itemRate.setText(String.valueOf(mListProductModel.get(i).getRate()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }


    private View.OnFocusChangeListener onFocusChangeListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((BaseActivity) mBaseFragment.getActivity()).hideKeyboard();
                double qty = TextUtils.isEmpty(mAddBillBinding.billEtQty.getText().toString()) ? 0 : Integer.parseInt(mAddBillBinding.billEtQty.getText().toString());
                double rate = TextUtils.isEmpty(mAddBillBinding.itemRate.getText().toString()) ? 0 : Double.parseDouble(mAddBillBinding.itemRate.getText().toString());
                double amt = qty * rate;
                mAddBillBinding.tvTotAmt.setText(new DecimalFormat("#.##").format(amt));
            }
        };
    }

    /**
     * validates the bill before going to displayt cart detail
     *
     * @return true if it matches the all validation otherwise false
     */
    public boolean validateForm() {
        boolean flag = true;
        ((BaseActivity) mBaseFragment.getActivity()).hideKeyboard();
        if (TextUtils.isEmpty(mAddBillBinding.getBillHeaderModel().getCustomer_name())) {
            mAddBillBinding.addBillEtCustomer.setError(mBaseFragment.getString(R.string.enter_customer_name));
            mAddBillBinding.addBillEtCustomer.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mAddBillBinding.getBillHeaderModel().getAddress())) {
            mAddBillBinding.addBillEtAddress.setError(mBaseFragment.getString(R.string.enter_customer_address));
            mAddBillBinding.addBillEtAddress.requestFocus();
            return false;
        }

        if (((HomeActivity) mBaseFragment.getActivity()).getmListDetailModel().size() == 0) {
            CM.showDialog(mBaseFragment.getActivity(), mBaseFragment.getString(R.string.please_add_product));
            return false;
        }
        return flag;
    }

    /**
     * adds the item currently selected item in the temp {@link ArrayList} of {@link BillDetailModel}
     * after it add it will remove data from the form
     *
     * @return
     */
    public View.OnClickListener onAddClick() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BillDetailModel billDetailModel = new BillDetailModel();
                double qty = TextUtils.isEmpty(mAddBillBinding.billEtQty.getText().toString()) ? 0 : Integer.parseInt(mAddBillBinding.billEtQty.getText().toString());
                double rate = TextUtils.isEmpty(mAddBillBinding.itemRate.getText().toString()) ? 0 : Double.parseDouble(mAddBillBinding.itemRate.getText().toString());
                double amt = qty * rate;
                if (amt == 0) {
                    mAddBillBinding.billEtQty.setError(mBaseFragment.getString(R.string.invalid_amt));
                    return;
                }
                billDetailModel.setPrd_id(mListProductModel.get(mAddBillBinding.productSpinner.getSelectedItemPosition()).getId());
                billDetailModel.setPrd_name(mListProductModel.get(mAddBillBinding.productSpinner.getSelectedItemPosition()).getProduct_name());
                billDetailModel.setQty(qty);
                billDetailModel.setRate(rate);
                billDetailModel.setAmt(amt);
                ((HomeActivity) mBaseFragment.getActivity()).getmListDetailModel().add(billDetailModel);
                mAddBillBinding.getBillHeaderModel().setTotal(mAddBillBinding.getBillHeaderModel().getTotal() + amt);
                mAddBillBinding.itemRate.setText("");
                mAddBillBinding.billEtQty.setText("");
                mAddBillBinding.tvTotAmt.setText("");
                mAddBillBinding.productSpinner.setSelection(0);
                ((BaseActivity) mBaseFragment.getActivity()).hideKeyboard();
            }
        };
    }
}
