package com.kcs.billingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static String PACKAGENAME;
    private static String DB_PATH = "/data/data/" + PACKAGENAME + "/databases/";
    public static String DB_NAME = "inventory.sqlite";
    private SQLiteDatabase mDatabase;
    private final Context mContext;

    public DbHelper(final Context context) {
        super(context, DB_NAME, null, 1);
        PACKAGENAME = context.getPackageName();
        DB_PATH = "/data/data/" + PACKAGENAME + "/databases/";
        this.mContext = context;
    }


    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDatabase() throws IOException {
        final boolean dbExist = checkDataBase();
        SQLiteDatabase db_read = null;
        if (dbExist) {

        } else {
            db_read = this.getReadableDatabase();
            db_read.close();
            copyDatabaseFromFile();
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        final File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }


    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     *
     * @throws IOException
     */
    private void copyDatabaseFromFile() throws IOException {
        final InputStream myInput = mContext.getAssets().open(DB_NAME);

        final String outFileName = DB_PATH + DB_NAME;

        final OutputStream myOutput = new FileOutputStream(outFileName);

        final byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDatabase() {
        final String db_path = DB_PATH + DB_NAME;
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
        }

        return mDatabase = SQLiteDatabase.openDatabase(db_path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
