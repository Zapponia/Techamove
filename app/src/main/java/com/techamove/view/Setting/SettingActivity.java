package com.techamove.view.Setting;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Constants;
import com.techamove.Utils.UriHelper;
import com.techamove.Utils.Utility;
import com.techamove.view.BaseActivity;
import com.techamove.view.BusinessCardSave.SaveCardActivity;
import com.techamove.view.BusinessCardShare.ShareCardActivity;
import com.techamove.view.Home.HomeActivity;
import com.techamove.view.UserModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends BaseActivity implements ResponseListener {

    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.imgUserProfile)
    CircleImageView imgUserProfile;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.radioEnglish)
    RadioButton radioEnglish;
    @BindView(R.id.radioDenish)
    RadioButton radioDenish;
    @BindView(R.id.radioGerman)
    RadioButton radioGerman;
    @BindView(R.id.radioLanguage)
    RadioGroup radioLanguage;
    @BindView(R.id.btnSave)
    Button btnSave;

    Context mContext;
    String strLanguageCode = "";
    String strLanguage = "";
    File file;
    String profilePath = "";
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 111;
    ResponsePresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mContext = SettingActivity.this;
        presenter = new ResponsePresenter(mContext, this);
        iniview();
    }

    private void iniview() {
        txtTitle.setText(mContext.getResources().getString(R.string.menu_setting));
        Utility.loadImageFromUrl(mContext, imgUserProfile, AppPreferences.getInstance(mContext).getAvatar(), R.drawable.ic_defualt);
        edtName.setText(AppPreferences.getInstance(mContext).getFullName());
        txtEmail.setText(AppPreferences.getInstance(mContext).getEmail());
        edtName.setEnabled(true);

        if (Utility.getPref(SettingActivity.this, Constants.PREF_LANGUAGE, "").equalsIgnoreCase("English")) {
            radioEnglish.setChecked(true);
            radioDenish.setChecked(false);
            radioGerman.setChecked(false);
            strLanguageCode = Constants.ENGLISH_CODE;
            strLanguage = Constants.ENGLISH;

        } else if (Utility.getPref(SettingActivity.this, Constants.PREF_LANGUAGE, "").equalsIgnoreCase("German")) {
            radioGerman.setChecked(true);
            radioDenish.setChecked(false);
            radioEnglish.setChecked(false);
            strLanguageCode = Constants.GERMAN_CODE;
            strLanguage = Constants.GERMAN;
        } else {
            radioDenish.setChecked(true);
            radioGerman.setChecked(false);
            radioEnglish.setChecked(false);
            strLanguageCode = Constants.DANISH_CODE;
            strLanguage = Constants.DANISH;
        }

        radioLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioEnglish) {
                    strLanguageCode = Constants.ENGLISH_CODE;
                    strLanguage = Constants.ENGLISH;
//                    setLanguage(strLanguageCode);
                } else if (checkedId == R.id.radioDenish) {
                    strLanguageCode = Constants.DANISH_CODE;
                    strLanguage = Constants.DANISH;
//                    setLanguage(strLanguageCode);
                } else if (checkedId == R.id.radioGerman) {
                    strLanguageCode = Constants.GERMAN_CODE;
                    strLanguage = Constants.GERMAN;
//                    setLanguage(strLanguageCode);
                }
            }
        });
    }

    private void setLanguage(String strLanguageCode) {
        Utility.localization(strLanguageCode, SettingActivity.this);
        Utility.setPref(mContext, Constants.PREF_LANGUAGE, strLanguage);
        AppPreferences.getInstance(mContext).setLanguage(strLanguage);

        if (AppPreferences.getInstance(mContext).getAccountType().equals("2")) {
            Intent i = new Intent(mContext, SaveCardActivity.class); // ShareCardActivity.class
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            Intent i = new Intent(mContext, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDrawer();
    }

    @OnClick({R.id.imgDrawer, R.id.btnSave, R.id.imgUserProfile, R.id.edtName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgDrawer:
                openDrawer();
                break;
            case R.id.edtName:
                edtName.setEnabled(true);
                break;
            case R.id.btnSave:
                presenter.gsonSetting(profilePath, edtName.getText().toString().trim());
                break;
            case R.id.imgUserProfile:
                if (checkPermission()) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.OFF)
                            .start(this);
                } else {
                    requestPermission();
                }
                break;

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
                Utility.loadImageFromUrl(mContext, imgUserProfile, selectedImagePath, R.drawable.ic_defualt);
                imgUserProfile.setImageURI(resultUri);
                profilePath = selectedImagePath;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(mContext, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.OFF)
                            .start(this);
                } else {
//                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
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
        UserModel model = Utility.getModelData(response, UserModel.class);
        AppPreferences.getInstance(mContext).setFullName(model.data.full_name);
        AppPreferences.getInstance(mContext).setAvatar(model.data.avatar);
        edtName.setEnabled(false);
        setLanguage(strLanguageCode);
    }

    @Override
    public void onFailureHandler() {

    }

    @Override
    public void onNetworkFailure(String message) {

    }

}
