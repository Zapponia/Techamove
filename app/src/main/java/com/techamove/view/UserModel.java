package com.techamove.view;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public Data data;
    @SerializedName("message")
    @Expose
    public String message;

    public class Data {
        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("full_name")
        @Expose
        public String full_name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("mobile_number")
        @Expose
        public String mobile_number;
        @SerializedName("verify_otp")
        @Expose
        public String verify_otp;
        @SerializedName("email_verified_at")
        @Expose
        public String email_verified_at;
        @SerializedName("account_type")
        @Expose
        public String account_type;
        @SerializedName("licence_key")
        @Expose
        public String licence_key;
        @SerializedName("in_app_purchase_id")
        @Expose
        public String in_app_purchase_id;
        @SerializedName("avatar")
        @Expose
        public String avatar;
        @SerializedName("device_type")
        @Expose
        public String device_type;
        @SerializedName("device_token")
        @Expose
        public String device_token;
        @SerializedName("created_at")
        @Expose
        public String created_at;
        @SerializedName("updated_at")
        @Expose
        public String updated_at;
        @SerializedName("token")
        @Expose
        public String token;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("account_status")
        @Expose
        public String accountStatus;
        @SerializedName("country_code")
        @Expose
        public String countryCode;
    }
}
