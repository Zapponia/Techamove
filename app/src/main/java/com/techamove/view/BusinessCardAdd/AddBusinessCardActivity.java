package com.techamove.view.BusinessCardAdd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Utility;
import com.techamove.view.Home.HomeActivity;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.techamove.Utils.Utility.setStatusBarGradiant;

public class AddBusinessCardActivity extends AppCompatActivity implements ResponseListener {
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.edtFullName)
    EditText edtFullName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtMobile)
    EditText edtMobile;
    @BindView(R.id.edtBusinessName)
    EditText edtBusinessName;
    @BindView(R.id.edtBusinessservice)
    EditText edtBusinessservice;
    @BindView(R.id.edtFacebook)
    EditText edtFacebook;
    @BindView(R.id.edtLinkedin)
    EditText edtLinkedin;
    @BindView(R.id.edtGmail)
    EditText edtGmail;
    @BindView(R.id.edtTwitter)
    EditText edtTwitter;
    @BindView(R.id.edtInstagram)
    EditText edtInstagram;
    @BindView(R.id.edtWebsite)
    EditText edtWebsite;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.txtUserId)
    TextView txtUserId;

    Context mContext;
    ResponsePresenter presenter;
    String countryCode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_add_business_card);
        ButterKnife.bind(this);
        mContext = AddBusinessCardActivity.this;
        presenter = new ResponsePresenter(mContext, this);
        intview();
    }

    private void intview() {
        imgDrawer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_white_left));
        txtTitle.setText(mContext.getResources().getString(R.string.title_add_business_card));

        txtUserId.setText(""+AppPreferences.getInstance(mContext).getID());

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

    @OnClick({R.id.imgDrawer, R.id.btnSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgDrawer:
                finish();
                break;
            case R.id.btnSave:
                if (!isFullName()) {
                    Utility.showError(mContext, edtFullName, R.string.err_msg_full_name);
                } else if (!isEmail()) {
                    Utility.showError(mContext, edtEmail, R.string.err_msg_email);
                } else if (!isValideEmail()) {
                    Utility.showError(mContext, edtEmail, R.string.err_msg_invalid_email);
                } else if (countryCode.equals("")) {
                    Utility.customToast(mContext, mContext.getResources().getString(R.string.err_msg_country_code));
                } else if (!isMobile()) {
                    Utility.showError(mContext, edtMobile, R.string.err_msg_mobile);
                } else if (!isBusinessName()) {
                    Utility.showError(mContext, edtBusinessName, R.string.err_msg_business_name);
                } else if (!isService()) {
                    Utility.showError(mContext, edtBusinessservice, R.string.err_msg_business_services);
                } else if (!isWebsite()) {
                    Utility.showError(mContext, edtWebsite, R.string.err_msg_website);
                } else {
                    presenter.gsonAddCard(edtFullName, edtEmail, edtMobile, edtBusinessName, edtBusinessservice,
                            edtFacebook, edtLinkedin, edtGmail, edtTwitter, edtWebsite, edtInstagram, countryCode);
                }
                break;
        }
    }

    public boolean isFullName() {
        if (Utility.isEmpty(edtFullName)) {
            return false;
        }
        return true;
    }

    public boolean isEmail() {
        if (Utility.isEmpty(edtEmail)) {
            return false;
        }
        return true;
    }

    public boolean isValideEmail() {
        if (Utility.isEmailValid(edtEmail)) {
            return true;
        }
        return false;
    }

    public boolean isMobile() {
        if (Utility.isEmpty(edtMobile)) {
            return false;
        }
        return true;
    }

    public boolean isBusinessName() {
        if (Utility.isEmpty(edtBusinessName)) {
            return false;
        }
        return true;
    }

    public boolean isService() {
        if (Utility.isEmpty(edtBusinessservice)) {
            return false;
        }
        return true;
    }

    public boolean isWebsite() {
        if (Utility.isEmpty(edtWebsite)) {
            return false;
        }
        return true;
    }


    @Override
    public void onSuccessHandler(String response, String apiTag) {
        Intent i = new Intent(mContext, HomeActivity.class);
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
