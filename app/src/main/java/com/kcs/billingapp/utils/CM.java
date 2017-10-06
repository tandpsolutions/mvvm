package com.kcs.billingapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.kcs.billingapp.R;
import com.kcs.billingapp.activities.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by bhaumik.shah on 02-Aug-17.
 */

public class CM {

    public static void startActivity(BaseActivity activity, Class<?> cls) {
        final Intent mIntent = new Intent(activity, cls);
        activity.startActivity(mIntent);
    }

    public static void showDialog(Context context, String message, String positiveButton, String negativeButon, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton(positiveButton, positive)
                .setNegativeButton(negativeButon, negative).show();
    }


    public static void showDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton(R.string.ok, null).show();
    }


    public static boolean addPermission(Activity
                                                context, List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                return false;
            }

        }
        return true;

    }

    public static void permissionDeniedSetting(final Activity thisActivity, final String[] permissions) {
        int countBlock = CM.checkNeverAskAgainList(thisActivity, permissions);
        if (countBlock > 0) {
            // Permission Denied
            Toast.makeText(thisActivity, thisActivity.getResources().getString(R.string.marshmellow_permission_denied_text), Toast.LENGTH_SHORT)
                    .show();
        } else {
            CM.showDialog(thisActivity, thisActivity.getString(R.string.marshmellow_setting_permission_text)
                    , thisActivity.getString(R.string.yes), thisActivity.getString(R.string.no)
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CM.goToSettings(thisActivity);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
        }
    }

    public static void goToSettings(Activity context) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myAppSettings);
    }

    public static int checkNeverAskAgainList(Activity
                                                     context, String[] permissionsList) {
        int deniedList = 0;
        for (String strPermission : permissionsList) {
            boolean isRational = ActivityCompat.shouldShowRequestPermissionRationale(context, strPermission);
            if (isRational) {
                deniedList++;
            }
        }
        return deniedList;
    }


    /**
     * Lolipop issue fixing If gallery image selected
     *
     * @param activity
     * @param contentUri
     * @return
     */
    public static String getPathOfSelectedImage(Activity activity, Uri contentUri) {
        String picturePath = "";

        String ImagePath;

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri,
                filePathColumn, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        } else {
            picturePath = contentUri.getPath();
        }
        /////////////////////////////////////////////////
        // picturePath = data.getData().getPath();


        File fileAttached = new File(picturePath);
        final File path = new File(Environment.getExternalStorageDirectory(), CV.APP_FOLDER_NAME);
        if (!path.exists()) {
            path.mkdir();
        }
        ImagePath = Environment.getExternalStorageDirectory() + File.separator + CV
                .APP_FOLDER_NAME + File.separator + System.currentTimeMillis() + fileAttached.getName();
        final File DestFile = new File(ImagePath);
        try {
            copyFile(fileAttached, DestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ImagePath;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    public static String getCaptureImagePath() {
        //it will return /sdcard/image.tmp
        final File path = new File(Environment.getExternalStorageDirectory(), CV.APP_FOLDER_NAME);
        if (!path.exists()) {
            path.mkdir();
        }
        String imagePath = Environment
                .getExternalStorageDirectory() + File.separator + CV.APP_FOLDER_NAME + File.separator + System.currentTimeMillis() + ".png";
        return imagePath;
    }

}
