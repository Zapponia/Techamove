package com.techamove.view.BusinessCardVideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoModel {
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;

    public class Datum {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("users_id")
        @Expose
        public String users_id;
        @SerializedName("videos_url")
        @Expose
        public String videos_url;
        @SerializedName("thumb_images")
        @Expose
        public String thumbImages;
        @SerializedName("created_at")
        @Expose
        public String created_at;
        @SerializedName("updated_at")
        @Expose
        public String updated_at;

    }
}
