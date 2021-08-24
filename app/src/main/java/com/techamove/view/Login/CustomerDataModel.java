package com.techamove.view.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerDataModel {
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public List<Datum> data = new ArrayList<Datum>();


    public class Datum implements Serializable {

        @SerializedName("store_id")
        @Expose
        public String storeId;
        @SerializedName("customer_id")
        @Expose
        public String customerId;
        @SerializedName("firstname")
        @Expose
        public String firstname;
        @SerializedName("lastname")
        @Expose
        public String lastname;
        @SerializedName("telephone")
        @Expose
        public String telephone;
        @SerializedName("fax")
        @Expose
        public String fax;
        @SerializedName("email")
        @Expose
        public String email;


    }
}
