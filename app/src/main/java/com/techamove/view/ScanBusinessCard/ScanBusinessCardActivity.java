package com.techamove.view.ScanBusinessCard;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.BaseActivity;
import com.techamove.view.BusinessCardView.ViewBusinessCardActivity;
import com.techamove.view.Home.CardListModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class ScanBusinessCardActivity extends BaseActivity implements ZBarScannerView.ResultHandler, ResponseListener {
    ZBarScannerView mScannerView;
    Context mContext;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    ResponsePresenter presenter;
    private static final int PERMISSION_REQUEST_CODE = 200;
    String strIdentification = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card);
        ButterKnife.bind(this);
        mContext = ScanBusinessCardActivity.this;
        strIdentification = getIntent().getStringExtra(Constants.IDENTIFICATION);
        txtTitle.setText(mContext.getResources().getString(R.string.title_qr_scanner));
        Log.e("Activity", "Activity");
        presenter = new ResponsePresenter(mContext, this);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.scanner_fragment);
        mScannerView = new ZBarScannerView(mContext);
        contentFrame.addView(mScannerView);
        mScannerView.setAutoFocus(true);

        if (Constants.SIGNUPACTIVITY.equals(strIdentification)) {
            imgDrawer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_white_left));
        }
        if (!checkPermission()) {
            requestPermission();
        }

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // main logic
                    iniview();
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
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton(mContext.getResources().getString(R.string.lable_ok), okListener)
                .setNegativeButton(mContext.getResources().getString(R.string.lable_cancel), cancelListener)
                .create()
                .show();
    }


    private void iniview() {
        if (mScannerView == null) {
            mScannerView = new ZBarScannerView(this);
            setContentView(mScannerView);
        }
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @OnClick(R.id.imgDrawer)
    void drawer() {
        if (Constants.SIGNUPACTIVITY.equals(strIdentification)) {
            onBackPressed();
        } else {
            openDrawer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initDrawer();
        if (checkPermission()) {
            iniview();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (checkPermission()) {
//            mScannerView.setAutoFocus(false);
            mScannerView.stopCamera();
        }
    }


    @Override
    public void handleResult(Result rawResult) {

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanBusinessCardActivity.this);
            }
        }, 2000);

        Log.e(" Result=====>> ", "  " + rawResult.getContents());
        Log.e(" Result=====>> ", " BarcodeFormat " + new Gson().toJson(rawResult.getBarcodeFormat()));

        presenter.gsonCardDetails(rawResult.getContents());


    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            CardListModel.Datum model = Utility.getModelData(jsonObject.getJSONObject("data").toString(), CardListModel.Datum.class);
            if (Constants.SIGNUPACTIVITY.equals(strIdentification)) {
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                i.putExtra(Constants.USERID, model.users_id);
                finish();
            } else {
                Intent i = new Intent(mContext, ViewBusinessCardActivity.class);
                i.putExtra(Constants.IDENTIFICATION, Constants.SCANCARDACTIVITY);
                i.putExtra(Constants.CARDLISTMODEL, new Gson().toJson(model));
                startActivity(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureHandler() {
        mScannerView.setAutoFocus(false);
    }

    @Override
    public void onNetworkFailure(String message) {

    }
}
