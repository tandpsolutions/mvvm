package com.kcs.billingapp.retrofit;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by bhaumik.shah on 09-Aug-17.
 */

public interface WebServiceAPI {

    @Multipart
    @POST("UploadImage/UploadFile")
    public Call<JSONObject> uploadImage(@Part MultipartBody.Part formData);
}
