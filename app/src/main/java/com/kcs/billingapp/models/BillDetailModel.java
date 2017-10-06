package com.kcs.billingapp.models;

import java.text.DecimalFormat;

/**
 * Created by bhaumik.shah on 03-Aug-17.
 */

public class BillDetailModel {
    private long bill_no;
    private long prd_id;
    private String prd_name;
    private double rate;
    private double qty;
    private double amt;


    public long getBill_no() {
        return bill_no;
    }

    public void setBill_no(long bill_no) {
        this.bill_no = bill_no;
    }

    public long getPrd_id() {
        return prd_id;
    }

    public void setPrd_id(long prd_id) {
        this.prd_id = prd_id;
    }

    public double getRate() {
        return rate;
    }

    public String getFormatedRate() {
        return String.valueOf(getRate());
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getQty() {
        return qty;
    }

    public String getFormatedQty() {
        return String.valueOf(qty);
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getAmt() {
        return amt;
    }

    public String getFormatedAmt() {
        return new DecimalFormat("#.##").format(amt);
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getPrd_name() {
        return prd_name;
    }

    public void setPrd_name(String prd_name) {
        this.prd_name = prd_name;
    }
}
