package com.techamove.view.Notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {

    @Expose
    @SerializedName("data")
    public List<DataEntity> data;
    @Expose
    @SerializedName("message")
    public String message;
    @Expose
    @SerializedName("success")
    public boolean success;

    public static class DataEntity {
        @Expose
        @SerializedName("updated_at")
        public String updatedAt;
        @Expose
        @SerializedName("created_at")
        public String createdAt;
        @Expose
        @SerializedName("admin_read")
        public int adminRead;
        @Expose
        @SerializedName("read")
        public int read;
        @Expose
        @SerializedName("description")
        public String description;
        @Expose
        @SerializedName("type")
        public String type;
        @Expose
        @SerializedName("title")
        public String title;
        @Expose
        @SerializedName("receiver_id")
        public int receiverId;
        @Expose
        @SerializedName("sender_id")
        public int senderId;
        @Expose
        @SerializedName("id")
        public int id;
    }
}
