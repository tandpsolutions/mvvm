package com.kcs.billingapp.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.kcs.billingapp.R;
import com.kcs.billingapp.activities.HomeActivity;
import com.kcs.billingapp.databinding.FragmentProfileBinding;
import com.kcs.billingapp.retrofit.WebServiceClient;
import com.kcs.billingapp.utils.CM;
import com.kcs.billingapp.utils.ImageLoadingUtils;
import com.kcs.billingapp.viewmodels.ProfileViewModel;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    private FragmentProfileBinding mProfileBinding;
    private String captureImagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        mProfileBinding.setProfile(new ProfileViewModel(this));
        return mProfileBinding.getRoot();
    }

    @Override
    public void initActionBar() {
        ((HomeActivity) getActivity()).setToolBar(getString(R.string.edit_profile), false);
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mProfileBinding.getProfile().REQ_CODE_ASK_PERM_GALLERY) {
            mProfileBinding.getProfile().onCameraGalleryIntentHandle(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mProfileBinding.getProfile().REQUEST_FROM_GALLERY) {
            String galleryPath = CM.getPathOfSelectedImage(getActivity(), data.getData());
            captureImagePath = galleryPath;
        }

        if (TextUtils.isEmpty(captureImagePath)) {
            return;
        }

        if (!new File(captureImagePath).exists()) {
            CM.showDialog(getActivity(), getString(R.string.msg_file_notexists));
            return;
        }

        ImageLoadingUtils imgUtils = new ImageLoadingUtils(getActivity());
        String mImagePath = imgUtils.compressImage(getActivity(), captureImagePath, new ImageLoadingUtils(getActivity()));
        mProfileBinding.getProfile().setImagePath(mImagePath);
//        DebugLog.i("CompressedPath", "CompressedPath: " + captureImagePath);
        Glide.with(getActivity()).load(mImagePath).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileBinding.profileImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mProfileBinding.profileImage.setImageDrawable(circularBitmapDrawable);

            }
        });

    }
}
