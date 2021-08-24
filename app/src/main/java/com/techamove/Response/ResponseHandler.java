package com.techamove.Response;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.techamove.R;
import com.techamove.Utils.Debug;
import com.techamove.Utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ResponseHandler implements Callback<ResponseBody> {
    Context mContext;

    public ResponseHandler(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        onFinished();
        Debug.e("URL-->>", mContext.getClass().getSimpleName() + " " + call.request().url());
        Utility.printParam("Param ==>" + mContext.getClass().getSimpleName(), call.request());
        try {

            if (response.code() == 200) {
                String r = response.body().source().readString(Charset.forName("UTF-8"));
                Debug.e("Success-->>", mContext.getClass().getSimpleName() + " " + r);
                JSONObject jsonObject = new JSONObject(r);
                if (String.valueOf(jsonObject.get("success")).equals("4")) {
                    sendMessage();
                } else {
                    onSuccess(call, r);
                }
            } else {
                String r = response.errorBody().source().readString(Charset.forName("UTF-8"));
                Debug.e("Success-->>", mContext.getClass().getSimpleName() + " " + r);
                JSONObject jsonObject = new JSONObject(r);
                if (String.valueOf(jsonObject.get("success")).equals("4")) {
                } else {
                    onSuccess(call, r);
                }
            }
        } catch (IOException e) {
            Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (NullPointerException e) {
            Utility.showAlertDialog(mContext, mContext.getString(R.string.app_name),
                    e.getLocalizedMessage(), mContext.getString(R.string.lable_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, null, null);
        } catch (JSONException e) {
            Utility.showAlertDialog(mContext, mContext.getString(R.string.app_name),
                    e.getLocalizedMessage(), mContext.getString(R.string.lable_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, null, null);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        onFinished();
        Debug.e("URL-->>", mContext.getClass().getSimpleName() + " " + call.request().url());
        onFailed(call, t);
        Debug.e("Failed-->>", mContext.getClass().getSimpleName() + " " + t.getMessage());
        if (!((Activity) mContext).isFinishing()) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
            builder1.setTitle(mContext.getResources().getString(R.string.app_name));
            builder1.setMessage(t.getLocalizedMessage());
            builder1.setCancelable(true);
            builder1.setPositiveButton(mContext.getResources().getString(R.string.lable_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    public abstract void onSuccess(Call<ResponseBody> call, String response);

    public abstract void onFailed(Call<ResponseBody> call, Throwable t);

    public abstract void onFinished();

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

}
