package com.techamove.view.AddMember;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMemberActivity extends AppCompatActivity implements ResponseListener {
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.imgBell)
    ImageView imgBell;
    @BindView(R.id.edtFullName)
    EditText edtFullName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtMobile)
    EditText edtMobile;
    @BindView(R.id.btnAddMember)
    Button btnAddMember;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    Context mContext;
    ResponsePresenter presenter;
    String countryCode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        ButterKnife.bind(this);
        mContext = AddMemberActivity.this;
        presenter = new ResponsePresenter(mContext, this);
        initView();
    }

    private void initView() {
        imgDrawer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_white_left));
        txtTitle.setText(mContext.getResources().getString(R.string.btn_add_new_contact));

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                countryCode = selectedCountry.getPhoneCode();
                Log.e("countryCode--->", "" + countryCode);

            }
        });
        ccp.setCountryForPhoneCode(45);
        countryCode = "45";
    }

    @OnClick(R.id.btnAddMember)
    void onAddMember() {
        if (Utility.isEmpty(edtFullName)) {
            Utility.showError(mContext, edtFullName, R.string.err_msg_full_name);
        } else if (Utility.isEmpty(edtEmail)) {
            Utility.showError(mContext, edtEmail, R.string.err_msg_email);
        } else if (!Utility.isEmailValid(edtEmail)) {
            Utility.showError(mContext, edtEmail, R.string.err_msg_invalid_email);
        } else if (countryCode.equals("")) {
            Utility.customToast(mContext, mContext.getResources().getString(R.string.err_msg_country_code));
        } else if (Utility.isEmpty(edtMobile)) {
            Utility.showError(mContext, edtMobile, R.string.err_msg_mobile);
        } else {
            presenter.gsonAddMember(edtFullName, edtEmail,countryCode,edtMobile);
        }
    }

    @OnClick(R.id.imgDrawer)
    void imgBack() {
        onBackPressed();
    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (!((Activity) mContext).isFinishing()) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
                builder1.setCancelable(true);
                builder1.setPositiveButton(mContext.getResources().getString(R.string.lable_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent();
                        setResult(Activity.RESULT_OK, i);
                        finish();
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailureHandler() {

    }

    @Override
    public void onNetworkFailure(String message) {

    }
}
