package com.kcs.billingapp.viewmodels;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.BaseObservable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kcs.billingapp.R;
import com.kcs.billingapp.callbacks.OnPermissionGrantChecking;
import com.kcs.billingapp.fragments.ProfileFragment;
import com.kcs.billingapp.retrofit.WebServiceClient;
import com.kcs.billingapp.utils.CM;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhaumik.shah on 09-Aug-17.
 */

public class ProfileViewModel extends BaseObservable {

    private ProfileFragment mProfileFragment;
    public final int REQ_CODE_ASK_PERM_CAMERA = 1112;
    public final int REQ_CODE_ASK_PERM_GALLERY = 1113;
    public final int REQUEST_FROM_GALLERY = 1114;

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ProfileViewModel(ProfileFragment mProfileFragment) {
        this.mProfileFragment = mProfileFragment;

    }

    public View.OnClickListener onImageClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogImageForCameraGallery();
            }
        };
    }


    //click attachement question to open attachment dialog
    //three selection available
    //1.Capture Photo
    //3.Select Photo from gallary
    public void showDialogImageForCameraGallery() {


        final CharSequence[] choice = {mProfileFragment.getString(R.string.choosePhoto), mProfileFragment.getString(R.string.capturePhoto)};

        AlertDialog.Builder alert = new AlertDialog.Builder(mProfileFragment.getActivity());
        alert.setTitle(mProfileFragment.getString(R.string.selectPhoto));

        alert.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {


                    dialog.dismiss();

                    applyMarshmallowPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new String[]{"Write sdcard"}, REQ_CODE_ASK_PERM_GALLERY, new OnPermissionGrantChecking() {
                        @Override
                        public void onPermssionAlreadyGranted() {
                            openGallery();
                        }
                    });
                    dialog.dismiss();
                } else if (which == 1) {


                    dialog.dismiss();
//                    applyMarshmallowPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new String[]{"Write sdcard", "Access Camera"}, REQ_CODE_ASK_PERM_CAMERA, new OnPermissionGrantChecking() {
//                        @Override
//                        public void onPermssionAlreadyGranted() {
//                            captureImage();
//                        }
//                    });
                }
            }
        });
        alert.setPositiveButton(mProfileFragment.getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }


    private void applyMarshmallowPermission(String[] permissions, String[] permissionMessage, final int requsetCode
            , OnPermissionGrantChecking permissionCallback) {
        final List<String> permissionsList = new ArrayList<String>();
        List<String> permissionsNeeded = new ArrayList<String>();

        for (int i = 0; i < permissions.length; i++) {
            if (CM.addPermission(mProfileFragment.getActivity(), permissionsList, permissions[i])) {
                permissionsNeeded.add(permissions[i]);
            }
        }

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = mProfileFragment.getString(R.string.msg_permission_access);
                message += permissionMessage[0];
                for (int i = 1; i < permissionMessage.length; i++) {
                    message += ", " + permissionMessage[i];
                }
                CM.showDialog(mProfileFragment.getActivity(), message,
                        mProfileFragment.getString(R.string.yes), mProfileFragment.getString(R.string.no)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mProfileFragment.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        requsetCode);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
            } else {
                mProfileFragment.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        requsetCode);
                return;
            }
        } else {
            if (permissionCallback != null) {
                permissionCallback.onPermssionAlreadyGranted();
            }
        }
    }

    public void onCameraGalleryIntentHandle(int requestCode, String[] permissions, int[] grantPermission) {
        final Map<String, Integer> params = new HashMap();
        for (int i = 0; i < permissions.length; i++) {
            params.put(permissions[i], grantPermission[i]);
        }

        if (requestCode == REQ_CODE_ASK_PERM_GALLERY) {
            if (params.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                CM.permissionDeniedSetting(mProfileFragment.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
            }
        }

    }

    private void openGallery() {
        final PackageManager packageManager = mProfileFragment.getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            mProfileFragment.startActivityForResult(intent, REQUEST_FROM_GALLERY);
        } else {
            Toast.makeText(mProfileFragment.getActivity(), mProfileFragment.getString(R.string.msg_notfind_intent), Toast
                    .LENGTH_LONG).show();
            return;
        }
    }

    public View.OnClickListener onUploadClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getImagePath().startsWith("http:")) {
                    File file = new File(getImagePath());
                    RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("bhaumik", file.getName(), reqFile);
                    WebServiceClient.getService().uploadImage(body).enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(mProfileFragment.getActivity(), "Success", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("Error", response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            Toast.makeText(mProfileFragment.getActivity(), "Failure", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
    }

}
