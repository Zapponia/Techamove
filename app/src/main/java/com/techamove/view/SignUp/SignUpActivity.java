package com.techamove.view.SignUp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.Utils.UriHelper;
import com.techamove.Utils.Utility;
import com.techamove.view.Login.NewLoginActivity;
import com.techamove.view.PaymentPlan.PaymentPlanActivity;
import com.techamove.view.ScanBusinessCard.ScanBusinessCardActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements ResponseListener {
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.edtFullName)
    EditText edtFullName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtConfiPassword)
    EditText edtConfiPassword;
    @BindView(R.id.edtMobile)
    EditText edtMobile;
    @BindView(R.id.edtReferralCode)
    EditText edtReferralCode;
    @BindView(R.id.radioPremium)
    RadioButton radioPremium;
    @BindView(R.id.radioFree)
    RadioButton radioFree;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.txtSignIn)
    TextView txtSignIn;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.imgScan)
    ImageView imgScan;
    @BindView(R.id.togglePassShow)
    ImageView togglePassShow;
    @BindView(R.id.togglePassHide)
    ImageView togglePassHide;
    @BindView(R.id.toggleConfiPassShow)
    ImageView toggleConfiPassShow;
    @BindView(R.id.toggleConfiPassHide)
    ImageView toggleConfiPassHide;


    Context mContext;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 111;
    File file;
    String profilePath = "", strAccountType = "", countryCode = "";
    ResponsePresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mContext = SignUpActivity.this;
        presenter = new ResponsePresenter(mContext, this);
        iniview();
    }

    private void iniview() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioPremium) {
                    strAccountType = Constants.PREMIUM_ACCOUNT;
                } else if (checkedId == R.id.radioFree) {
                    strAccountType = Constants.FREE_ACCOUNT;
                } else if (checkedId == R.id.radioAffiliate) {
                    strAccountType = Constants.FREE_AFFILIATE;
                }
            }
        });

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

    @OnClick({R.id.btnSignUp, R.id.txtSignIn, R.id.imgBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
//                if (profilePath.equals("")) {
//                    Utility.customToast(mContext, mContext.getResources().getString(R.string.err_msg_profile));
//                } else

                if (Utility.isEmpty(edtFullName)) {
                    Utility.showError(mContext, edtFullName, R.string.err_msg_full_name);
                } else if (Utility.isEmpty(edtEmail)) {
                    Utility.showError(mContext, edtEmail, R.string.err_msg_email);
                } else if (!Utility.isEmailValid(edtEmail)) {
                    Utility.showError(mContext, edtEmail, R.string.err_msg_invalid_email);
                } else if (Utility.isEmpty(edtMobile)) {
                    Utility.showError(mContext, edtMobile, R.string.err_msg_mobile);
                } else if (countryCode.equals("")) {
                    Utility.customToast(mContext, mContext.getResources().getString(R.string.err_msg_country_code));
                } else if (Utility.isEmpty(edtPassword)) {
                    Utility.showError(mContext, edtPassword, R.string.err_msg_password);
                } else if (Utility.isEmpty(edtConfiPassword)) {
                    Utility.showError(mContext, edtConfiPassword, R.string.err_msg_confirm_password);
                } else if (!edtPassword.getText().toString().matches(edtConfiPassword.getText().toString())) {
                    Utility.showError(mContext, edtConfiPassword, R.string.err_msg_password_not_match);
                } else if (strAccountType.equals("")) {
                    Utility.customToast(mContext, mContext.getResources().getString(R.string.err_msg_account_type));
                } else {
                    if (strAccountType.equals("2")) {
                        presenter.gsonRegister(profilePath, edtFullName.getText().toString().trim(), edtEmail.getText().toString().trim(),
                                countryCode, edtMobile.getText().toString().trim(), strAccountType, edtPassword.getText().toString().trim(), edtReferralCode.getText().toString());
                    } else {
                        // TODO: 06/03/2020 --->  check user email exit or not
                        presenter.gsonCheckEmailExit(edtEmail.getText().toString().trim());
                    }
                }

                break;
            case R.id.txtSignIn:
                Intent j = new Intent(SignUpActivity.this, NewLoginActivity.class);
                startActivity(j);
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    @OnClick(R.id.imgProfile)
    public void onViewClicked() {
        if (checkPermission()) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    .start(this);
        } else {
            requestPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String selectedImagePath = UriHelper.getPath(mContext, resultUri);
                file = new File(selectedImagePath);
                Utility.loadImageFromUrl(mContext, imgProfile, selectedImagePath, R.drawable.ic_defualt);
                imgProfile.setImageURI(resultUri);
                profilePath = selectedImagePath;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(mContext, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 789) {
            if (resultCode == RESULT_OK) {
                edtReferralCode.setText(data.getStringExtra(Constants.USERID));
            }
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.OFF)
                            .start(this);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel(mContext.getResources().getString(R.string.camera_permission_message),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel(mContext.getResources().getString(R.string.camera_permission_message),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton(mContext.getResources().getString(R.string.lable_ok), okListener)
                .setNegativeButton(mContext.getResources().getString(R.string.lable_cancel), null)
                .create()
                .show();
    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        if (apiTag.equalsIgnoreCase(Constants.API_EMAIL_EXIT)) {
            SignUpModel model = new SignUpModel();
            model.ProfilePath = profilePath;
            model.FullName = edtFullName.getText().toString().trim();
            model.Email = edtEmail.getText().toString().trim();
            model.CountryCode = countryCode;
            model.Mobile = edtMobile.getText().toString().trim();
            model.AccountType = strAccountType;
            model.Password = edtPassword.getText().toString().trim();
            model.ReferralCode = edtReferralCode.getText().toString();

            Intent i = new Intent(mContext, PaymentPlanActivity.class);
            i.putExtra(Constants.IDENTIFICATION, Constants.SIGNUPACTIVITY);
            i.putExtra(Constants.EMAIL, edtEmail.getText().toString().trim());
            i.putExtra(Constants.SIGNUPMODEL, new Gson().toJson(model));
            startActivity(i);

        } else {
            Intent i = new Intent(mContext, NewLoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onFailureHandler() {

    }

    @Override
    public void onNetworkFailure(String message) {

    }


    @OnClick(R.id.imgScan)
    void scan() {
        Intent i = new Intent(mContext, ScanBusinessCardActivity.class);
        i.putExtra(Constants.IDENTIFICATION, Constants.SIGNUPACTIVITY);
        startActivityForResult(i, 789);
    }


    @OnClick({R.id.togglePassShow, R.id.togglePassHide, R.id.toggleConfiPassShow, R.id.toggleConfiPassHide})
    public void onPassViewClicked(View view) {
        switch (view.getId()) {
            case R.id.togglePassShow:
                edtPassword.setTransformationMethod(null);
                togglePassShow.setVisibility(View.GONE);
                togglePassHide.setVisibility(View.VISIBLE);
                break;
            case R.id.togglePassHide:
                edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                togglePassShow.setVisibility(View.VISIBLE);
                togglePassHide.setVisibility(View.GONE);
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
