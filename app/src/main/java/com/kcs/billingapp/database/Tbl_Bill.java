package com.kcs.billingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kcs.billingapp.BillingApp;
import com.kcs.billingapp.R;
import com.kcs.billingapp.models.BillDetailModel;
import com.kcs.billingapp.models.BillHeaderModel;
import com.kcs.billingapp.models.ResultModel;

import java.util.ArrayList;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class Tbl_Bill {
    public static final String TABLE_HEADER = "BillMaster";
    public static final String TABLE_DETAIL = "BillDetail";
    public static final String BILL_NO = "bill_no";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_ADDRESS = "customer_address";
    public static final String GENDER = "gender";
    public static final String DATE_TIME = "date_time";
    public static final String TOTAL_AMOUNT = "total_amount";
    public static final String SR_NO = "sr_no";
    public static final String PRODUCT_ID = "product_id";
    public static final String RATE = "rate";
    public static final String QTY = "qty";
    public static final String AMOUNT = "amount";


    private Context mContext;
    private SQLiteDatabase mSqLiteDatabase;

    public Tbl_Bill(Context mContext) {
        this.mContext = mContext;
        mSqLiteDatabase = ((BillingApp) mContext.getApplicationContext()).getSqLiteDatabase();
    }

    /**
     * received header and detail model for table billhd and billdt respectively
     *
     * @param billHeaderModel  bill header model that contains bill header information
     * @param billDetailModels arraylist of billdetail model that contains all items that present in the bill
     * @return {@link ResultModel} that contains the information the bill is successfully saved or not
     */
    public ResultModel insertUpdateBill(BillHeaderModel billHeaderModel, ArrayList<BillDetailModel> billDetailModels) {
        final ResultModel resultModel = new ResultModel();
        mSqLiteDatabase.beginTransaction();

        final ContentValues cv = new ContentValues();
        cv.put(CUSTOMER_NAME, billHeaderModel.getCustomer_name());
        cv.put(CUSTOMER_ADDRESS, billHeaderModel.getAddress());
        cv.put(GENDER, billHeaderModel.isGender());
        cv.put(DATE_TIME, billHeaderModel.getTime_stamp());
        cv.put(TOTAL_AMOUNT, billHeaderModel.getTotal());
        if (billHeaderModel.getBill_No() != 0) {
            mSqLiteDatabase.update(TABLE_HEADER, cv, BILL_NO + "=" + billHeaderModel.getBill_No(), null);
        } else {
            long id = mSqLiteDatabase.insert(TABLE_HEADER, null, cv);
            billHeaderModel.setBill_No(id);
        }

        mSqLiteDatabase.delete(TABLE_DETAIL, BILL_NO + "=?", new String[]{String.valueOf(billHeaderModel.getBill_No())});

        for (int i = 0; i < billDetailModels.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(BILL_NO, billHeaderModel.getBill_No());
            values.put(SR_NO, i + 1);
            values.put(PRODUCT_ID, billDetailModels.get(i).getPrd_id());
            values.put(QTY, billDetailModels.get(i).getQty());
            values.put(RATE, billDetailModels.get(i).getRate());
            values.put(AMOUNT, billDetailModels.get(i).getAmt());
            mSqLiteDatabase.insert(TABLE_DETAIL, null, values);
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
        resultModel.setStatusCode(ResultModel.SUCCESS);
        resultModel.setMessage(mContext.getString(R.string.sucess));
        return resultModel;
    }

    /**
     * returns all the bills that are stored in the database
     *
     * @return arraylist of the header model that will show on @{@link com.kcs.billingapp.fragments.ListBillFragment}
     */
    public ArrayList<BillHeaderModel> getBillHeader() {
        final ArrayList<BillHeaderModel> headerModels = new ArrayList<>();
        final Cursor cursor;

        final String sql = "select * from " + Tbl_Bill.TABLE_HEADER;
        cursor = mSqLiteDatabase.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final BillHeaderModel headerModel = new BillHeaderModel();
                headerModel.setBill_No(cursor.getLong(cursor.getColumnIndex(Tbl_Bill.BILL_NO)));
                headerModel.setCustomer_name(cursor.getString(cursor.getColumnIndex(Tbl_Bill.CUSTOMER_NAME)));
                headerModel.setAddress(cursor.getString(cursor.getColumnIndex(Tbl_Bill.CUSTOMER_ADDRESS)));
                headerModel.setGender(cursor.getInt(cursor.getColumnIndex(Tbl_Bill.GENDER)) > 0);
                headerModel.setTime_stamp(cursor.getLong(cursor.getColumnIndex(Tbl_Bill.DATE_TIME)));
                headerModel.setTotal(cursor.getDouble(cursor.getColumnIndex(Tbl_Bill.TOTAL_AMOUNT)));
                headerModels.add(headerModel);
            } while (cursor.moveToNext());
        }
        return headerModels;
    }


    /**
     * returns all the items that are stored in the database for particular bill
     *
     * @param bill_no bill number of which items will populated
     * @return arraylist of {@link BillDetailModel} that will show in {@link com.kcs.billingapp.fragments.BillDetailFragment}
     */
    public ArrayList<BillDetailModel> getDetailModels(long bill_no) {
        final ArrayList<BillDetailModel> billDetailModels = new ArrayList<>();
        final Cursor cursor;

        final String sql = "select * from " + Tbl_Bill.TABLE_DETAIL + "," + Tbl_Product.TABLENAME + " " +
                " where " + Tbl_Product.ID + "=" + PRODUCT_ID + " and " + BILL_NO + "=" + bill_no;
        cursor = mSqLiteDatabase.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final BillDetailModel detailModel = new BillDetailModel();
                detailModel.setBill_no(cursor.getLong(cursor.getColumnIndex(Tbl_Bill.BILL_NO)));
                detailModel.setPrd_id(cursor.getLong(cursor.getColumnIndex(Tbl_Bill.PRODUCT_ID)));
                detailModel.setPrd_name(cursor.getString(cursor.getColumnIndex(Tbl_Product.PRODUCT_NAME)));
                detailModel.setRate(cursor.getDouble(cursor.getColumnIndex(Tbl_Bill.RATE)));
                detailModel.setQty(cursor.getInt(cursor.getColumnIndex(Tbl_Bill.QTY)));
                detailModel.setAmt(cursor.getDouble(cursor.getColumnIndex(Tbl_Bill.AMOUNT)));
                billDetailModels.add(detailModel);
            } while (cursor.moveToNext());
        }
        return billDetailModels;
    }

}
