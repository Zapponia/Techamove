package com.techamove.Response;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.techamove.R;
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Constants;
import com.techamove.Utils.LoadingDialog;
import com.techamove.Utils.ProgressedRequestBody;
import com.techamove.Utils.Utility;
import com.techamove.view.Home.HomeActivity;
import com.techamove.view.Login.NewLoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ResponsePresenter implements ProgressedRequestBody.UploadCallbacks {

    Context mContext;
    ResponseListener responseListener;
    LoadingDialog loadingDialog;
    AppPreferences appPreferences;

    public static final String TAG = ResponsePresenter.class.getName();

    public ResponsePresenter(Context mContext, ResponseListener responseListener) {
        this.mContext = mContext;
        this.responseListener = responseListener;
        loadingDialog = new LoadingDialog(mContext);
        appPreferences = AppPreferences.getInstance(mContext);
    }

    public void gsonRegister(String profilePath, String fullname, String strEmail, String countryCode, String mobile, String strAccountType,
                             String strPassword, String strReferralCode) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            File fileProfile = new File(profilePath);

            MultipartBody.Part bodyProfileImage;
            RequestBody requestprofileImage = RequestBody.create(MediaType.parse("image/jpeg"), new File(profilePath));

            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), fullname);
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmail);
            RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
            RequestBody referralCode = RequestBody.create(MediaType.parse("text/plain"), strReferralCode);
            RequestBody mobilenumber = RequestBody.create(MediaType.parse("text/plain"), mobile);
            RequestBody country_Code = RequestBody.create(MediaType.parse("text/plain"), countryCode);
            RequestBody accountType = RequestBody.create(MediaType.parse("text/plain"), strAccountType);
            RequestBody purchaseid = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody deviceType = RequestBody.create(MediaType.parse("text/plain"), Constants.DEVICE_NAME);
            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), Utility.getPref(mContext, Constants.DEVICE_TOKEN, ""));

            if (fileProfile.getPath().equals("")) {
                bodyProfileImage = MultipartBody.Part.createFormData("image", "");
            } else {
                bodyProfileImage = MultipartBody.Part.createFormData("image", fileProfile.getName().replace(" ", ""), requestprofileImage);
            }

            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .register(name, email, mobilenumber, accountType, token, purchaseid, bodyProfileImage, deviceType, country_Code, password, referralCode)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            Log.e("Register", " --> " + new Gson().toJson(response));
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_REGISTER);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonLogin(String code, String mobile) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
//            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .login(mobile, code)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, "");
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {

//                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }


    public void gsonLogin1(String email, String password) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            if (loadingDialog != null && !loadingDialog.isShowing()) {
                loadingDialog.show();
            }
            NetworkUtility.getClientInterface(mContext)
                    .loginEmail(email, password, Utility.getPref(mContext, Constants.DEVICE_TOKEN, ""), Constants.DEVICE_NAME)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            Log.e("Body", "---> " + email + " - " + password);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_LOGIN);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }


    public void gsonOtpVarify(String strOtp) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .verifyotp(strOtp, Utility.getPref(mContext, Constants.DEVICE_TOKEN, ""), Constants.DEVICE_NAME)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_VERIFYOTP);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonCardList() {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .showcardlist(String.valueOf(appPreferences.getID()))
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SHOWCARDLISt);
                                } else {
                                    responseListener.onFailureHandler();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            responseListener.onNetworkFailure(mContext.getResources().getString(R.string.network_error_msg));
        }
    }

    public void gsonAddCard(EditText edtFullName, EditText edtEmail, EditText edtMobile, EditText edtBusinessName, EditText edtBusinessservice, EditText edtFacebook, EditText edtLinkedin,
                            EditText edtGmail, EditText edtTwitter, EditText edtWebsite, EditText edtInstagram, String countryCode) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .addnewcard(String.valueOf(appPreferences.getID()), edtFullName.getText().toString(), edtEmail.getText().toString(), edtMobile.getText().toString(),
                            edtBusinessName.getText().toString(), edtBusinessservice.getText().toString(), edtFacebook.getText().toString(), edtGmail.getText().toString(),
                            edtLinkedin.getText().toString(), edtInstagram.getText().toString(), edtWebsite.getText().toString(), edtTwitter.getText().toString(), "", countryCode)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_ADDNEWCARD);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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

    }

    public void gsonEditCard(EditText edtFullName, EditText edtEmail, EditText edtMobile, EditText edtBusinessName, EditText edtBusinessservice, EditText edtFacebook, EditText edtLinkedin,
                             EditText edtGmail, EditText edtTwitter, EditText edtWebsite, EditText edtInstagram, String countryCode, String id) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .updatecarddetails(edtFullName.getText().toString(), edtEmail.getText().toString(), edtMobile.getText().toString(),
                            edtBusinessName.getText().toString(), edtBusinessservice.getText().toString(), edtFacebook.getText().toString(), edtGmail.getText().toString(),
                            edtLinkedin.getText().toString(), edtInstagram.getText().toString(), edtWebsite.getText().toString(), edtTwitter.getText().toString(), id, countryCode)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_ADDNEWCARD);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonDeleteCard(String id) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .deletecarddetails(id)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_DELETECARDDETAILS);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonUserList(String strCardId) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .premium(strCardId)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_PREMIUM);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
                                        builder1.setCancelable(true);
                                        builder1.setPositiveButton(mContext.getResources().getString(R.string.lable_ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                Intent i = new Intent(mContext, HomeActivity.class);
                                                mContext.startActivity(i);
                                                ((Activity) mContext).finish();
                                            }
                                        });
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            responseListener.onNetworkFailure(mContext.getResources().getString(R.string.network_error_msg));
        }
    }


    public void gsonShareCard(String strCardId, String selectedIds) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .sharecard(strCardId, String.valueOf(appPreferences.getID()), selectedIds)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    Utility.customToast(mContext, jsonObject.getString(Constants.MESSAGE));
                                    responseListener.onSuccessHandler(response, Constants.API_SHARECARD);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonGetShareCard() {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .sharelist(String.valueOf(appPreferences.getID()))
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SHARELIST);
                                } else {
                                    responseListener.onFailureHandler();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            responseListener.onNetworkFailure(mContext.getResources().getString(R.string.network_error_msg));
        }
    }

    public void gsonsharecarddelete(String id) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .sharecarddelete(id)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SHARECARDDELETE);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonLogOut() {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .logout()
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    appPreferences.clearAll(mContext);
                                    Intent i = new Intent(mContext, NewLoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mContext.startActivity(i);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonSaveCardList() {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .savecardlist()
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SAVECARDLIST);
                                } else {
                                    responseListener.onFailureHandler();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            responseListener.onNetworkFailure(mContext.getResources().getString(R.string.network_error_msg));
        }
    }


    public void gsonSaveCardDelete(String id) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .savecarddelete(id)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SAVE_CARD_DELETE);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }


    public void gsonSaveCard(String id) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .savecard(id)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SAVE_CARD);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }


    public void gsonCardDetails(String id) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .showcarddetails(id)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SHARECARDDELETE);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {

        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }

        return bitmap;
    }


    public void gsonVideoUpload(String strFileUrl, File thumbnailFile) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            File fileProfile = new File(strFileUrl);
            ProgressedRequestBody fileBody = new ProgressedRequestBody(fileProfile, "video/mp4", this);
            MultipartBody.Part bodyVideo = MultipartBody.Part.createFormData("videos_url", fileProfile.getName().replace(" ", ""), fileBody);

            ProgressedRequestBody fileBody1 = new ProgressedRequestBody(thumbnailFile, "image/jpeg", this);
            MultipartBody.Part bodyImage = MultipartBody.Part.createFormData("thumb_images", fileProfile.getName().replace(" ", ""), fileBody1);

//            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .uploadvideo(bodyVideo, bodyImage)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SHARECARDDELETE);
                                } else {
                                    if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
                                        builder1.setCancelable(true);
                                        builder1.setPositiveButton(mContext.getResources().getString(R.string.lable_ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                ((Activity) mContext).finish();
                                            }
                                        });
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {

//                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonVideoUserList(String strVideoId) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .videopremium(strVideoId)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_VIDEO_PREMIUM);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
                                        builder1.setCancelable(true);
                                        builder1.setPositiveButton(mContext.getResources().getString(R.string.lable_ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                Intent i = new Intent(mContext, HomeActivity.class);
                                                mContext.startActivity(i);
                                                ((Activity) mContext).finish();
                                            }
                                        });
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            responseListener.onNetworkFailure(mContext.getResources().getString(R.string.network_error_msg));
        }
    }

    public void gsonShareVideo(String strVideoId, String selectedIds) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .sharevideos(strVideoId, selectedIds)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    Utility.customToast(mContext, jsonObject.getString(Constants.MESSAGE));
                                    responseListener.onSuccessHandler(response, Constants.API_SHARE_VIDEO);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }


    @Override
    public void onProgressUpdate(int percentage) {
        Log.e(TAG, "onProgressUpdate: ");
        responseListener.onSuccessHandler(percentage + "", Constants.VIDEO_PROGRESS);
    }

    @Override
    public void onError() {
        Log.e(TAG, "onError: ");
    }

    @Override
    public void onFinish() {
        Log.e(TAG, "onFinish: ");
    }

    @Override
    public void fileCount(int count) {
        Log.e(TAG, "fileCount: ");
    }

    public void gsonOwnVideoList() {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .ownvideo()
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_OWNVIDEO);
                                } else {
                                    responseListener.onFailureHandler();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            responseListener.onNetworkFailure(mContext.getResources().getString(R.string.network_error_msg));
        }
    }

    public void gsonShareVideoList() {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .sharevideoslist()
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SHARE_VIDEO_LIST);
                                } else {
                                    responseListener.onFailureHandler();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            responseListener.onNetworkFailure(mContext.getResources().getString(R.string.network_error_msg));
        }
    }

    public void gsonpurchase(String strEmail, String appId, String productName) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .purchase(strEmail, appId, productName)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_PURCHASE);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonLicenseVerify(String strLincenseKey) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .licenseverify(strLincenseKey)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_LICENSEVERIFY);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonSetting(String profilePath, String strName) {

        if (NetworkUtility.isInternetAvailable(mContext)) {
            File fileProfile = new File(profilePath);

            MultipartBody.Part bodyProfileImage;
            RequestBody requestprofileImage = RequestBody.create(MediaType.parse("image/jpeg"), new File(profilePath));

            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), strName);
//            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmail);

            if (fileProfile.getPath().equals("")) {
                bodyProfileImage = MultipartBody.Part.createFormData("image", "");
            } else {
                bodyProfileImage = MultipartBody.Part.createFormData("image", fileProfile.getName().replace(" ", ""), requestprofileImage);
            }
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .setting(name, bodyProfileImage)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            Log.e("Body", "---> " + name);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    Utility.customToast(mContext, jsonObject.getString(Constants.MESSAGE));
                                    responseListener.onSuccessHandler(response, Constants.API_SETTING);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonOwnVideoDelete(int id) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .ownvideodelete(id)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_OWNVIDEODELETE);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonShareVideoDelete(int id) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .sharevideodelete(id)
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_SHAREVIDEODELETE);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonNotificationList(String PageCount) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .shownotificationlist()
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_NOTIFICATION);
                                } else {
                                    responseListener.onFailureHandler();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            responseListener.onNetworkFailure(mContext.getResources().getString(R.string.network_error_msg));
        }
    }

    public void gsoncheckSubscription() {
        NetworkUtility.getClientInterface(mContext)
                .checkSubscription()
                .enqueue(new ResponseHandler(mContext) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            responseListener.onSuccessHandler(response, Constants.API_CHECKSUBSCRIPTION);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Call<ResponseBody> call, Throwable t) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });

    }

    public void gsonApi1(String email) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            Network1.getClientInterface(mContext)
                    .checkEmail(email)
                    .enqueue(new ResponseHandler1(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                responseListener.onSuccessHandler(response, Constants.API_API1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonAddMember(EditText edtFullName, EditText edtEmail, String countryCode, EditText edtMobile) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .newUsers(edtFullName.getText().toString(), edtEmail.getText().toString(), countryCode,
                            edtMobile.getText().toString(), String.valueOf(appPreferences.getID()))
                    .enqueue(new ResponseHandler(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(mContext.getResources().getString(R.string.msg_temporary_password_));
                                        builder1.setCancelable(true);
                                        builder1.setPositiveButton(mContext.getResources().getString(R.string.lable_ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                responseListener.onSuccessHandler(response, Constants.API_SHAREVIDEODELETE);
                                            }
                                        });
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }

                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonAllRecord() {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            Network1.getClientInterface(mContext)
                    .customers()
                    .enqueue(new ResponseHandler1(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                responseListener.onSuccessHandler(response, Constants.API_CUSTOMERS);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonSendSms(String strCardId, String strSendSms) {
        NetworkUtility.getClientInterface(mContext)
                .sendSms(strCardId, strSendSms)
                .enqueue(new ResponseHandler(mContext) {
                    @Override
                    public void onSuccess(Call<ResponseBody> call, String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                responseListener.onSuccessHandler(response, Constants.API_SHENDSMS);
                            } else {
                                Intent i = new Intent(mContext, HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(i);
                                ((Activity) mContext).finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Call<ResponseBody> call, Throwable t) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });

    }

    public void gsonChangePassword(String oldPassword, String newPassword) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .changePassword(oldPassword, newPassword)
                    .enqueue(new ResponseHandler1(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    Utility.customToast(mContext, jsonObject.getString(Constants.MESSAGE));
                                    responseListener.onSuccessHandler(response, Constants.API_SHAREVIDEODELETE);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonForgotPass(String email) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .forgotPassword(email)
                    .enqueue(new ResponseHandler1(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    Utility.customToast(mContext, jsonObject.getString(Constants.MESSAGE));
                                    responseListener.onSuccessHandler(response, Constants.API_FORGOT_PASSWORD);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }

    public void gsonCheckEmailExit(String email) {
        if (NetworkUtility.isInternetAvailable(mContext)) {
            loadingDialog.show();
            NetworkUtility.getClientInterface(mContext)
                    .checkEmailExit(email)
                    .enqueue(new ResponseHandler1(mContext) {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                                    responseListener.onSuccessHandler(response, Constants.API_EMAIL_EXIT);
                                } else {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Call<ResponseBody> call, Throwable t) {

                        }

                        @Override
                        public void onFinished() {
                            loadingDialog.dismiss();
                        }
                    });
        } else {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(mContext.getResources().getString(R.string.network_error_msg));
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
    }
}
