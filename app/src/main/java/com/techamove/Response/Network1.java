package com.techamove.Response;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network1 {
    static Retrofit getRetrofit(Context mContext) {
        return new Retrofit.Builder()
                .baseUrl(Constants.Check_Account_Url)
                .client(provideOkHttpClient(mContext))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .build();
    }

    public static boolean isInternetAvailable(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static OkHttpClient provideOkHttpClient(final Context mContext) {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(120, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(120, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(120, TimeUnit.SECONDS);

        okhttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder ongoing = chain.request().newBuilder();
                ongoing.addHeader("Content-Type", "application/json");
                if (!AppPreferences.getInstance(mContext).getEmail().equals("")) {
                    Log.e("Authorization", "" + AppPreferences.getInstance(mContext).getToken());
                    ongoing.addHeader("Authorization", "Bearer " + AppPreferences.getInstance(mContext).getToken());
                }
                return chain.proceed(ongoing.build());
            }
        });
        return okhttpClientBuilder.build();
    }

    public static APIList getClientInterface(Context mContext) {
        return getRetrofit(mContext).create(APIList.class);
    }
}
