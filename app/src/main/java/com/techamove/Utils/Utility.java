package com.techamove.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.techamove.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Request;
import okio.Buffer;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Utility {

    public static void printParam(String name, Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            Log.e(name, buffer.readUtf8());
        } catch (final IOException e) {
            Log.e(name, "did not work");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.toolbar_bg);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public static AlertDialog showAlertDialog(Context mContext, String title, String message,
                                              String positiveButton, DialogInterface.OnClickListener positiveButtonListerner,
                                              String negativeButton, DialogInterface.OnClickListener negativeButtonListerner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.myDialog)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveButtonListerner)
                .setNegativeButton(negativeButton, negativeButtonListerner);
        AlertDialog alertDialog = builder.create();

        if (!((Activity) mContext).isFinishing()) {
            alertDialog.show();
        }

//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.colorwhite));
//        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.colorwhite));
        return alertDialog;
    }

    public static void setPref(Context mContext, String key, Object value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.apply();
    }

    public static void localization(String language, Activity mContext) {
        Locale myLocale = new Locale(language);
        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLayoutDirection(myLocale);
        }
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static String getPref(Context mContext, String key, String defValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(key, defValue);
    }

    public static void loadImageFromUrl(Context mContext, ImageView imgView, String url, int placeholderId) {
        RequestOptions options = new RequestOptions().placeholder(placeholderId);
        Glide.with(mContext).
                load(url).
                apply(options)
                .into(imgView);
    }

    public static void showError(Context mContext, EditText editText, int stringResourceId) {
        editText.setError(mContext.getResources().getString(stringResourceId));
        editText.requestFocus();
    }

    public static boolean isEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString().trim());
    }

    public static boolean isEmailValid(EditText editText) {
        return Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches();
    }

    public static void customToast(Context context, String Message) {
        Toast toast = Toast.makeText(context, Message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static <T> T getModelData(String response, final Class<T> modelClass) {
        return modelClass.cast(new Gson().fromJson(response, modelClass));
    }

    public static String formatTime(String tm) throws ParseException {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("hh:mm a");
        fromUser.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return myFormat.format(fromUser.parse(tm));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }



}

