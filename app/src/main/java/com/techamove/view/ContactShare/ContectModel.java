package com.techamove.view.ContactShare;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ContectModel {
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("message")
    @Expose
    public String message;

    public static class Datum {
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
        public Object verify_otp;
        @SerializedName("email_verified_at")
        @Expose
        public Object email_verified_at;
        @SerializedName("account_type")
        @Expose
        public String account_type;
        @SerializedName("licence_key")
        @Expose
        public String licence_key;
        @SerializedName("avatar")
        @Expose
        public String avatar;
        @SerializedName("in_app_purchase_id")
        @Expose
        public String in_app_purchase_id;
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
        boolean isSelected = false;

    }


}
