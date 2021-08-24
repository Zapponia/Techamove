package com.techamove.Response;

public interface ResponseListener {
    void onSuccessHandler(String response, String apiTag);
    void onFailureHandler();
    void onNetworkFailure(String message);
}
