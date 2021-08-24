package com.techamove.view.BusinessCardEdit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.Home.CardListModel;
import com.techamove.view.Home.HomeActivity;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.techamove.Utils.Utility.setStatusBarGradiant;

public class EditBusinessCardActivity extends AppCompatActivity implements ResponseListener {
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
    @BindView(R.id.imgQrCode)
    ImageView imgQrCode;
    @BindView(R.id.txtUserId)
    TextView txtUserId;

    Context mContext;
    CardListModel.Datum model;
    String countryCode = "";
    ResponsePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_edit_business_card);
        ButterKnife.bind(this);
        mContext = EditBusinessCardActivity.this;
        model = new Gson().fromJson(getIntent().getStringExtra(Constants.CARDLISTMODEL), CardListModel.Datum.class);
        presenter = new ResponsePresenter(mContext, this);
        intview();
    }

    private void intview() {
        imgDrawer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_white_left));
        txtTitle.setText(mContext.getResources().getString(R.string.title_edit_business_card));
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                countryCode = selectedCountry.getPhoneCode();
                Log.e("countryCode--->", "" + countryCode);

            }
        });

        edtFullName.setText(model.full_name);
        edtEmail.setText(model.email);
        edtMobile.setText(model.mobile_number);
        edtBusinessName.setText(model.business_name);
        edtBusinessservice.setText(model.business_service);
        edtFacebook.setText(model.facebook);
        edtLinkedin.setText(model.linkedin);
        edtGmail.setText(model.google);
        edtTwitter.setText(model.twitter);
        edtInstagram.setText(model.instagram);
        edtWebsite.setText(model.website_link);
        txtUserId.setText(model.users_id);

        ccp.setCountryForPhoneCode(Integer.parseInt(model.countryCode));
        countryCode = model.countryCode;

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(model.id, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imgQrCode.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }

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
                } else if (!isMobile()) {
                    Utility.showError(mContext, edtMobile, R.string.err_msg_mobile);
                } else if (!isBusinessName()) {
                    Utility.showError(mContext, edtBusinessName, R.string.err_msg_business_name);
                } else if (!isService()) {
                    Utility.showError(mContext, edtBusinessservice, R.string.err_msg_business_services);
                } else if (!isWebsite()) {
                    Utility.showError(mContext, edtWebsite, R.string.err_msg_website);
                } else {
                    presenter.gsonEditCard(edtFullName, edtEmail, edtMobile, edtBusinessName, edtBusinessservice,
                            edtFacebook, edtLinkedin, edtGmail, edtTwitter, edtWebsite, edtInstagram, countryCode, model.id);
                }
                break;
        }
    }

    public boolean isWebsite() {
        if (Utility.isEmpty(edtWebsite)) {
            return false;
        }
        return true;
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
