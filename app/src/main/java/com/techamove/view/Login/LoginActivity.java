//package com.techamove.view.Login;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.rilixtech.CountryCodePicker;
//import com.techamove.R;
//import com.techamove.Response.ResponseListener;
//import com.techamove.Response.ResponsePresenter;
//import com.techamove.Utils.AppPreferences;
//import com.techamove.Utils.Constants;
//import com.techamove.Utils.Utility;
//import com.techamove.view.BusinessCardShare.ShareCardActivity;
//import com.techamove.view.Home.HomeActivity;
//import com.techamove.view.SignUp.SignUpActivity;
//import com.techamove.view.UserModel;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class LoginActivity extends AppCompatActivity implements ResponseListener {
//    @BindView(R.id.edtEmail)
//    EditText edtEmail;
//    @BindView(R.id.btnSignIn)
//    Button btnSignIn;
//    @BindView(R.id.txtSignUp)
//    TextView txtSignUp;
//    @BindView(R.id.ccp)
//    CountryCodePicker ccp;
//
//    Context mContext;
//    ResponsePresenter presenter;
//    String userName = "";
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        ButterKnife.bind(this);
//        mContext = LoginActivity.this;
//        presenter = new ResponsePresenter(mContext, this);
//    }
//
//    @OnClick({R.id.btnSignIn, R.id.txtSignUp})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btnSignIn:
//                if (Utility.isEmpty(edtEmail)) {
//                    Utility.showError(mContext, edtEmail, R.string.err_msg_email);
//                } else if (!Utility.isEmailValid(edtEmail)) {
//                    Utility.showError(mContext, edtEmail, R.string.err_msg_invalid_email);
//                } else {
//                    presenter.gsonAllRecord();
////                    presenter.gsonApi1(edtEmail.getText().toString());
//                }
//
//                break;
//            case R.id.txtSignUp:
//                Intent j = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(j);
//                break;
//        }
//    }
//
//    @Override
//    public void onSuccessHandler(String response, String apiTag) {
//        if (apiTag.equalsIgnoreCase(Constants.API_LOGIN)) {
//            UserModel model = Utility.getModelData(response, UserModel.class);
//            AppPreferences.getInstance(mContext).setID(model.data.id);
//            AppPreferences.getInstance(mContext).setFullName(model.data.full_name);
//            AppPreferences.getInstance(mContext).setInAppId(model.data.in_app_purchase_id);
//            AppPreferences.getInstance(mContext).setAccountType(model.data.account_type);
//            AppPreferences.getInstance(mContext).setAvatar(model.data.avatar);
//            AppPreferences.getInstance(mContext).setEmail(model.data.email);
//            AppPreferences.getInstance(mContext).setLicenceKey(model.data.licence_key);
//            AppPreferences.getInstance(mContext).setMobile(model.data.mobile_number);
//            AppPreferences.getInstance(mContext).setToken(model.data.token);
//            AppPreferences.getInstance(mContext).setDeviceToken(model.data.device_token);
//
//            if (model.data.account_type.equals("2")) {
//                Intent i = new Intent(mContext, ShareCardActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//            } else {
//                Intent i = new Intent(mContext, HomeActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//
//            }
//        } else if (apiTag.equals(Constants.API_API1)) {
//            try {
//                JSONObject object = new JSONObject(response);
//                if (object.getString("ReturnValue").equalsIgnoreCase("Free")) {
//                    Log.e("ReturnValue", " --> " + object.getString("ReturnValue"));
//                    presenter.gsonLogin1(edtEmail.getText().toString(), "2",userName);
//
//                } else {
//                    presenter.gsonLogin1(edtEmail.getText().toString(), "1",userName);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        } else if (apiTag.equals(Constants.API_CUSTOMERS)) {
//            CustomerDataModel model = Utility.getModelData(response, CustomerDataModel.class);
//            boolean isRegister = false;
//            for (int i = 0; i < model.data.size(); i++) {
//                if (model.data.get(i).email.equals(edtEmail.getText().toString())) {
//                    isRegister = true;
//                    userName = model.data.get(i).firstname + " " + model.data.get(i).lastname;
//                    presenter.gsonApi1(edtEmail.getText().toString());
//                }
//            }
//            if (!isRegister) {
//                if (!((Activity) mContext).isFinishing()) {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
//                    builder1.setTitle(mContext.getResources().getString(R.string.app_name));
//                    builder1.setMessage(mContext.getResources().getString(R.string.email_not_valid));
//                    builder1.setCancelable(true);
//                    builder1.setPositiveButton(mContext.getResources().getString(R.string.lable_ok), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onFailureHandler() {
//
//    }
//
//    @Override
//    public void onNetworkFailure(String message) {
//
//    }
//
//
//}
