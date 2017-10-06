package com.kcs.billingapp.models;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class ProductModel {
    private long id;
    private String product_name;
    private double rate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

}
