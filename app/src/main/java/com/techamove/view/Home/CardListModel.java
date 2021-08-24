package com.techamove.view.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardListModel {
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("message")
    @Expose
    public String message;

    public class Datum {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("users_id")
        @Expose
        public String users_id;
        @SerializedName("save_status")
        @Expose
        public String save_status;
        @SerializedName("full_name")
        @Expose
        public String full_name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("mobile_number")
        @Expose
        public String mobile_number;
        @SerializedName("business_name")
        @Expose
        public String business_name;
        @SerializedName("business_service")
        @Expose
        public String business_service;
        @SerializedName("facebook")
        @Expose
        public String facebook;
        @SerializedName("linkedin")
        @Expose
        public String linkedin;
        @SerializedName("google")
        @Expose
        public String google;
        @SerializedName("instagram")
        @Expose
        public String instagram;
        @SerializedName("twitter")
        @Expose
        public String twitter;
        @SerializedName("website_link")
        @Expose
        public String website_link;
        @SerializedName("created_at")
        @Expose
        public String created_at;
        @SerializedName("updated_at")
        @Expose
        public String updated_at;
        @SerializedName("country_code")
        @Expose
        public String countryCode;
    }
}
