package com.kcs.billingapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.kcs.billingapp.database.DbHelper;
import com.kcs.billingapp.retrofit.WebServiceClient;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class BillingApp extends Application {

    private SQLiteDatabase sqLiteDatabase;
    private DbHelper dbHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DbHelper(this);
        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sqLiteDatabase = dbHelper.openDatabase();

        WebServiceClient.init(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Ubuntu-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }
}
