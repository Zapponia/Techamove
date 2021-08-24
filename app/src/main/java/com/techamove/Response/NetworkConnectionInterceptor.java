package com.techamove.Response;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class NetworkConnectionInterceptor implements Interceptor {

    public abstract boolean isInternetAvailable();

    @Override
    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
        if (!isInternetAvailable()) {
            throw new NoConnectivityException();
        } else {
            Request request = chain.request().newBuilder().addHeader("AccessToken", "$2y$10$QI49JvNHcVJbldi9KMCBeeNRxatK/XVYUNgyL2HsG1HR4RiPyayDq").build();
            return chain.proceed(request);
        }
    }

    public class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return "It seems that you are not connected to the Internet. Kindly check your network settings.";
        }
    }
}