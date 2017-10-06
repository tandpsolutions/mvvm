package com.kcs.billingapp.retrofit;


import android.content.Context;

import com.kcs.billingapp.BuildConfig;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * This is a singleton for webservice API.
 * <p/>Header is included for all service.
 */
public class WebServiceClient {
    /**
     * Static Object reference
     */
    private static WebServiceClient webServiceClient;
    private static Context mContext;
    private static final int CONNECT_TIME_OUT_SEC = 60;
    private static final int READ_TIME_OUT_SEC = 60;
    /**
     * The API reference
     */
    private WebServiceAPI service;

    /**
     * Private constructor for singleton purpose
     */
    private WebServiceClient() {

    }

    Retrofit retrofit;

    /**
     * will init retrofit. needs to be called before using API. preferably from Application class
     *
     * @param context
     */
    public static void init(Context context) {
        if (webServiceClient == null) {
            webServiceClient = new WebServiceClient();
            webServiceClient.initRetrofit();
            mContext = context;
        }
    }

    /*
    *
    *               Internal helper and initializer
    *****************************************************************************
     */
    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .client(getClient())
                .build();
        service = retrofit.create(WebServiceAPI.class);
    }

    public static Retrofit getRetrofitClient() {
        return webServiceClient.retrofit;
    }

    /**
     * @return OkHttpClient with log and custom header interceptors
     */
    private OkHttpClient getClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT_SEC, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT_SEC, TimeUnit.SECONDS)
                .addInterceptor(customHeaderInterceptor())
                .addInterceptor(loggingInterceptor())
                .build();
       /* httpClient.addInterceptor(customHeaderInterceptor());
        httpClient.addInterceptor(loggingInterceptor());*/

        return httpClient;
    }

    /**
     * @return Web API
     */
    public static WebServiceAPI getService() {
        if (webServiceClient == null) {
            throw new IllegalStateException("Please initialise retrofit first");
        }

        return webServiceClient.service;
    }

    public static Retrofit getClientRetrofit() {
        return webServiceClient.retrofit;
    }

    private Interceptor customHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
//                DebugLog.i("log_tag",String.valueOf(CM.getSp(mContext, CV.SP_CUSTOMER_TOKEN, "")));
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        };
    }

    /**
     * @return Interceptor that provides logging
     */
    private Interceptor loggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    private class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }
}
