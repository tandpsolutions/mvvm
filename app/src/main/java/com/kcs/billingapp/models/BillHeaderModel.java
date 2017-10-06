package com.kcs.billingapp.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.kcs.billingapp.BR;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bhaumik.shah on 03-Aug-17.
 */

public class BillHeaderModel extends BaseObservable {

    private long bill_No;
    private String customer_name;
    private String address;
    private boolean gender;
    private double total;
    private long time_stamp;


    public long getBill_No() {
        return bill_No;
    }

    public String getFormatedBillNo() {
        return "GST" + String.format("%0" + (5 - (getBill_No() + "").length()) + "d", getBill_No());
    }

    public void setBill_No(long bill_No) {
        this.bill_No = bill_No;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public double getTotal() {
        return total;
    }

    @Bindable
    public String getFormatedTotal() {
        return new DecimalFormat("#.##").format(getTotal());
    }

    public void setTotal(double total) {
        this.total = total;
        notifyPropertyChanged(BR.formatedTotal);
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getFormatedTime() {
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(new Date(getTime_stamp()));
    }
}
