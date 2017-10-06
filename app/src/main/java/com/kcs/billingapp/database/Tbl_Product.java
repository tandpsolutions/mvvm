package com.kcs.billingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kcs.billingapp.BillingApp;
import com.kcs.billingapp.R;
import com.kcs.billingapp.models.ProductModel;
import com.kcs.billingapp.models.ResultModel;

import java.util.ArrayList;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class Tbl_Product {
    public static final String TABLENAME = "ProductMaster";
    public static final String ID = "id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_RATE = "product_rate";

    private Context mContext;
    private SQLiteDatabase mSqLiteDatabase;

    public Tbl_Product(Context mContext) {
        this.mContext = mContext;
        mSqLiteDatabase = ((BillingApp) mContext.getApplicationContext()).getSqLiteDatabase();
    }


    /**
     * adds or updates the product name in the database
     *
     * @param productModel of which is being updated in the database
     * @return {@link ResultModel} that contains the information that product is successfully saved or not
     */
    public ResultModel insertUpdateProduct(ProductModel productModel) {
        final ResultModel resultModel = new ResultModel();
        if (isProductExistOrNot(productModel.getProduct_name(), productModel.getId())) {
            resultModel.setStatusCode(ResultModel.FAILURE);
            resultModel.setMessage(mContext.getString(R.string.product_already_exist));
        } else {
            mSqLiteDatabase.beginTransaction();
            final ContentValues cv = new ContentValues();
            cv.put(PRODUCT_NAME, productModel.getProduct_name());
            cv.put(PRODUCT_RATE, productModel.getRate());
            if (productModel.getId() != 0) {
                mSqLiteDatabase.update(TABLENAME, cv, ID + "=" + productModel.getId(), null);
            } else {
                long id = mSqLiteDatabase.insert(TABLENAME, null, cv);
                productModel.setId(id);
            }
            mSqLiteDatabase.setTransactionSuccessful();
            mSqLiteDatabase.endTransaction();
            resultModel.setStatusCode(ResultModel.SUCCESS);
            resultModel.setMessage(mContext.getString(R.string.sucess));
        }
        return resultModel;
    }

    /**
     * returns all the products that are stored in the database
     *
     * @return arraylist of {@link ProductModel}
     */
    public ArrayList<ProductModel> getAllProducts() {
        final ArrayList<ProductModel> products = new ArrayList<>();
        final Cursor cursor;

        final String sql = "select * from " + Tbl_Product.TABLENAME;
        cursor = mSqLiteDatabase.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final ProductModel product = new ProductModel();
                product.setId(cursor.getLong(cursor.getColumnIndex(Tbl_Product.ID)));
                product.setProduct_name(cursor.getString(cursor.getColumnIndex(Tbl_Product.PRODUCT_NAME)));
                product.setRate(cursor.getDouble(cursor.getColumnIndex(Tbl_Product.PRODUCT_RATE)));
                products.add(product);
            } while (cursor.moveToNext());
        }
        return products;
    }

    /**
     * checks that product name is already exist in the database or not
     * return true if its exist otherwise false
     *
     * @param product_name name of the product
     * @param product_id   id of current product that being edited other wise 0
     * @return boolean
     */
    private boolean isProductExistOrNot(String product_name, long product_id) {
        String sql = "select * from " + TABLENAME + " where " + PRODUCT_NAME + " ='" + product_name + "'";
        if (product_id != 0) {
            sql += " and " + ID + " <>" + product_id;
        }
        Cursor cursor = mSqLiteDatabase.rawQuery(sql, null);
        boolean isExists = (cursor.getCount() > 0);
        cursor.close();
        return isExists;
    }
}
