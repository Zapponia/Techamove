package com.techamove.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * class to store user name related details
 * Created by envisiodevs on 15/10/2016.
 */
public class AppPreferences {

    private static final String APP_PREF = "businesscard";
    private static final String TAG = "AppPreferences";

    private static AppPreferences mAppPreferences;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    public static final String ID = "user_id";
    public static final String EMAIL = "email";
    public static final String USER_AVATAR = "userAvatar";
    public static final String LANGUAGE = "language";
    public static final String FULL_NAME = "full_name";
    public static final String INAPPID = "InAppId";
    public static final String ACCOUNT_TYPE = "AccountType";
    public static final String LICENCE_KEY = "LicenceKey";
    public static final String MOBILE = "Mobile";
    public static final String TOKEN = "Token";
    public static final String DEVICETOKEN = "Device_Token";


    /**
     * function to get singleton instance of Preferences class
     *
     * @param context
     * @return instance of this class
     */


    public static final String LOGINDATA = "logindata";


    public void clearAll(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.commit();

    }


    public static AppPreferences getInstance(Context context) {
        if (mAppPreferences == null) {
            mAppPreferences = new AppPreferences(context);
        }

        return mAppPreferences;
    }

    /**
     * constructor for initilaizing this class
     *
     * @param context
     */
    private AppPreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }



    public void setID(int UserID) {
        mEditor.putInt(ID, UserID);
        mEditor.commit();
    }

    public int getID() {
        return mSharedPreferences.getInt(ID, 0);
    }

    public void setEmail(String email) {
        mEditor.putString(EMAIL, email);
        mEditor.commit();
    }


    public String getEmail() {
        return mSharedPreferences.getString(EMAIL, "");
    }


    public void setAvatar(String userAvatar) {
        mEditor.putString(USER_AVATAR, userAvatar);
        mEditor.commit();
    }

    public String getAvatar() {
        return mSharedPreferences.getString(USER_AVATAR, "");
    }


    public void setLanguage(String language) {
        mEditor.putString(LANGUAGE, language);
        mEditor.commit();
    }

    public String getLanguage() {
        return mSharedPreferences.getString(LANGUAGE, "");
    }


    public void setFullName(String full_name) {
        mEditor.putString(FULL_NAME, full_name);
        mEditor.commit();
    }

    public String getFullName() {
        return mSharedPreferences.getString(FULL_NAME, "");
    }

    public void setInAppId(String in_app_purchase_id) {
        mEditor.putString(INAPPID, in_app_purchase_id);
        mEditor.commit();
    }

    public String getInAppId() {
        return mSharedPreferences.getString(INAPPID, "");
    }

    public void setAccountType(String account_type) {
        mEditor.putString(ACCOUNT_TYPE, account_type);
        mEditor.commit();
    }

    public String getAccountType() {
        return mSharedPreferences.getString(ACCOUNT_TYPE, "");
    }

    public void setLicenceKey(String licence_key) {
        mEditor.putString(LICENCE_KEY, licence_key);
        mEditor.commit();
    }

    public String getLicenceKey() {
        return mSharedPreferences.getString(LICENCE_KEY, "");
    }

    public void setMobile(String mobile_number) {
        mEditor.putString(MOBILE, mobile_number);
        mEditor.commit();
    }

    public String getMobile() {
        return mSharedPreferences.getString(MOBILE, "");
    }

    public void setToken(String token) {
        mEditor.putString(TOKEN, token);
        mEditor.commit();
    }

    public String getToken() {
        return mSharedPreferences.getString(TOKEN, "");
    }
    public void setDeviceToken(String token) {
        mEditor.putString(DEVICETOKEN, token);
        mEditor.commit();
    }

    public String getDeviceToken() {
        return mSharedPreferences.getString(DEVICETOKEN, "");
    }
}
