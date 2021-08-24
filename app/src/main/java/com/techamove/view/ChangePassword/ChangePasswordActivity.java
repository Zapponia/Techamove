package com.techamove.view.ChangePassword;

import android.content.Context;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Utility;
import com.techamove.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements ResponseListener {
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.edtOldPassword)
    EditText edtOldPassword;
    @BindView(R.id.edtNewPassword)
    EditText edtNewPassword;
    @BindView(R.id.edtConfiPassword)
    EditText edtConfiPassword;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.toggleOldPassShow)
    ImageView toggleOldPassShow;
    @BindView(R.id.toggleOldPassHide)
    ImageView toggleOldPassHide;
    @BindView(R.id.toggleNewPassShow)
    ImageView toggleNewPassShow;
    @BindView(R.id.toggleNewPassHide)
    ImageView toggleNewPassHide;
    @BindView(R.id.toggleConfiPassShow)
    ImageView toggleConfiPassShow;
    @BindView(R.id.toggleConfiPassHide)
    ImageView toggleConfiPassHide;
    Context mContext;
    ResponsePresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        mContext = ChangePasswordActivity.this;
        presenter = new ResponsePresenter(mContext, this);
        initView();
    }

    private void initView() {
        txtTitle.setText(mContext.getResources().getString(R.string.menu_change_password));
    }

    @OnClick({R.id.imgDrawer, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgDrawer:
                break;
            case R.id.btnSubmit:
                if (Utility.isEmpty(edtOldPassword)) {
                    Utility.showError(mContext, edtOldPassword, R.string.err_msg_old_password);
                } else if (Utility.isEmpty(edtNewPassword)) {
                    Utility.showError(mContext, edtNewPassword, R.string.err_msg_new_password);
                } else if (Utility.isEmpty(edtConfiPassword)) {
                    Utility.showError(mContext, edtConfiPassword, R.string.err_msg_confirm_password);
                } else if (!edtNewPassword.getText().toString().matches(edtConfiPassword.getText().toString())) {
                    Utility.showError(mContext, edtConfiPassword, R.string.err_msg_password_not_match);
                } else {
                    presenter.gsonChangePassword(edtOldPassword.getText().toString(), edtNewPassword.getText().toString());
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDrawer();
    }

    @OnClick(R.id.imgDrawer)
    void drawerOpen() {
        openDrawer();
    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        edtOldPassword.setText("");
        edtNewPassword.setText("");
        edtConfiPassword.setText("");
    }

    @Override
    public void onFailureHandler() {

    }

    @Override
    public void onNetworkFailure(String message) {

    }

    @OnClick({R.id.toggleOldPassShow, R.id.toggleOldPassHide, R.id.toggleNewPassShow, R.id.toggleNewPassHide, R.id.toggleConfiPassShow, R.id.toggleConfiPassHide})
    public void onPassViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toggleOldPassShow:
                edtOldPassword.setTransformationMethod(null);
                toggleOldPassShow.setVisibility(View.GONE);
                toggleOldPassHide.setVisibility(View.VISIBLE);
                break;
            case R.id.toggleOldPassHide:
                edtOldPassword.setTransformationMethod(new PasswordTransformationMethod());
                toggleOldPassShow.setVisibility(View.VISIBLE);
                toggleOldPassHide.setVisibility(View.GONE);

                break;
            case R.id.toggleNewPassShow:
                edtNewPassword.setTransformationMethod(null);
                toggleNewPassShow.setVisibility(View.GONE);
                toggleNewPassHide.setVisibility(View.VISIBLE);
                break;
            case R.id.toggleNewPassHide:
                edtNewPassword.setTransformationMethod(new PasswordTransformationMethod());
                toggleNewPassShow.setVisibility(View.VISIBLE);
                toggleNewPassHide.setVisibility(View.GONE);
                break;
            case R.id.toggleConfiPassShow:
                edtConfiPassword.setTransformationMethod(null);
                toggleConfiPassShow.setVisibility(View.GONE);
                toggleConfiPassHide.setVisibility(View.VISIBLE);
                break;
            case R.id.toggleConfiPassHide:
                edtConfiPassword.setTransformationMethod(new PasswordTransformationMethod());
                toggleConfiPassShow.setVisibility(View.VISIBLE);
                toggleConfiPassHide.setVisibility(View.GONE);
                break;
        }
    }
}
