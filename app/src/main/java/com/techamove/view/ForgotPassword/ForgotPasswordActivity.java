package com.techamove.view.ForgotPassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Utility;
import com.techamove.view.Login.NewLoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity implements ResponseListener {
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    Context mContext;
    ResponsePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        mContext = ForgotPasswordActivity.this;
        presenter = new ResponsePresenter(mContext, this);
        initView();
    }

    private void initView() {
        txtTitle.setText(mContext.getResources().getString(R.string.title_forgot_password));
    }

    @OnClick({R.id.imgBack, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                if (Utility.isEmpty(edtEmail)) {
                    Utility.showError(mContext, edtEmail, R.string.err_msg_email);
                } else if (!Utility.isEmailValid(edtEmail)) {
                    Utility.showError(mContext, edtEmail, R.string.err_msg_invalid_email);
                } else {
                    presenter.gsonForgotPass(edtEmail.getText().toString());
                }
                break;
        }
    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        Intent i = new Intent(mContext, NewLoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onFailureHandler() {

    }

    @Override
    public void onNetworkFailure(String message) {

    }
}
