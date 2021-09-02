package com.techamove.view.Login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.BusinessCardShare.ShareCardActivity;
import com.techamove.view.ForgotPassword.ForgotPasswordActivity;
import com.techamove.view.Home.HomeActivity;
import com.techamove.view.PaymentPlan.PaymentPlanActivity;
import com.techamove.view.SignUp.SignUpActivity;
import com.techamove.view.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewLoginActivity extends AppCompatActivity implements ResponseListener {
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.txtSignUp)
    TextView txtSignUp;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.imgToggleShow)
    ImageView imgToggleShow;
    @BindView(R.id.imgToggleHide)
    ImageView imgToggleHide;


    Context mContext;
    ResponsePresenter presenter;
    @BindView(R.id.txtForgot)
    TextView txtForgot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = NewLoginActivity.this;
        presenter = new ResponsePresenter(mContext, this);
    }

    @OnClick({R.id.btnSignIn, R.id.txtSignUp, R.id.txtForgot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                if (Utility.isEmpty(edtEmail)) {
                    Utility.showError(mContext, edtEmail, R.string.err_msg_email);
                } else if (!Utility.isEmailValid(edtEmail)) {
                    Utility.showError(mContext, edtEmail, R.string.err_msg_invalid_email);
                } else if (Utility.isEmpty(edtPassword)) {
                    Utility.showError(mContext, edtPassword, R.string.err_msg_password);
                } else {
                    presenter.gsonLogin1(edtEmail.getText().toString(), edtPassword.getText().toString());
                }

                break;
            case R.id.txtSignUp:
                Intent j = new Intent(NewLoginActivity.this, SignUpActivity.class);
                startActivity(j);
                break;

            case R.id.txtForgot:
                Intent i = new Intent(NewLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                break;
        }
    }

    UserModel model;

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        if (apiTag.equalsIgnoreCase(Constants.API_LOGIN)) {
            model = Utility.getModelData(response, UserModel.class);
            if (model.data.account_type.equals("2")) {
                saveData(model);
                Intent i = new Intent(mContext, ShareCardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                saveData(model);
                Intent i = new Intent(mContext, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

//                if (model.data.accountStatus.equals("0")) {
//                    Intent i = new Intent(mContext, PaymentPlanActivity.class);
//                    i.putExtra(Constants.EMAIL, model.data.email);
//                    startActivity(i);
//                } else if (model.data.accountStatus.equals("1") && model.data.status.equals("0")) {
//                    dialogLicence();
//                } else {
//                    saveData(model);
//                    Intent i = new Intent(mContext, HomeActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(i);
//                }
            }
        } else if (apiTag.equalsIgnoreCase(Constants.API_LICENSEVERIFY)) {
            saveData(model);
            Intent i = new Intent(mContext, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    private void saveData(UserModel model) {
        AppPreferences.getInstance(mContext).setID(model.data.id);
        AppPreferences.getInstance(mContext).setFullName(model.data.full_name);
        AppPreferences.getInstance(mContext).setInAppId(model.data.in_app_purchase_id);
        AppPreferences.getInstance(mContext).setAccountType(model.data.account_type);
        AppPreferences.getInstance(mContext).setAvatar(model.data.avatar);
        AppPreferences.getInstance(mContext).setEmail(model.data.email);
        AppPreferences.getInstance(mContext).setLicenceKey(model.data.licence_key);
        AppPreferences.getInstance(mContext).setMobile(model.data.mobile_number);
        AppPreferences.getInstance(mContext).setToken(model.data.token);
        AppPreferences.getInstance(mContext).setDeviceToken(model.data.device_token);
    }

    @Override
    public void onFailureHandler() {

    }

    @Override
    public void onNetworkFailure(String message) {

    }

    private void dialogLicence() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_licence_key, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        ImageView imgClose = alertDialog.findViewById(R.id.imgClose);
        EditText edtLicenceKey = alertDialog.findViewById(R.id.edtLicenceKey);
        Button btnContinue = alertDialog.findViewById(R.id.btnContinue);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                presenter.gsonLicenseVerify(edtLicenceKey.getText().toString().trim());
            }
        });
    }


    @OnClick({R.id.imgToggleShow, R.id.imgToggleHide})
    public void onViewPasswordClicked(View view) {
        switch (view.getId()) {
            case R.id.imgToggleShow:
                edtPassword.setTransformationMethod(null);
                imgToggleShow.setVisibility(View.GONE);
                imgToggleHide.setVisibility(View.VISIBLE);
                break;
            case R.id.imgToggleHide:
                edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                imgToggleShow.setVisibility(View.VISIBLE);
                imgToggleHide.setVisibility(View.GONE);
                break;
        }
    }
}
