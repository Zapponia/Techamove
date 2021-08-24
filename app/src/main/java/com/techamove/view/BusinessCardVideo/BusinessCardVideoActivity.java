package com.techamove.view.BusinessCardVideo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.techamove.R;
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Constants;
import com.techamove.view.BaseActivity;
import com.techamove.view.VideoRecod.VideoRecoderActivity;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BusinessCardVideoActivity extends BaseActivity {
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 111;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.imgRecoder)
    ImageView imgRecoder;


    TabAdapter adapter;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        mContext = BusinessCardVideoActivity.this;
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        iniview();
    }

    private void iniview() {
        txtTitle.setText(mContext.getResources().getString(R.string.menu_video_material));
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new TabAdapter(getSupportFragmentManager());
        if (AppPreferences.getInstance(mContext).getAccountType().equals("2")) {
            tabLayout.setVisibility(View.GONE);
            imgRecoder.setVisibility(View.GONE);
            adapter.addFragment(new OwnVideoFragment().newInstance(Constants.SHAREVIDEO, 2), mContext.getResources().getString(R.string.tab_title_shared_video));

        } else {
            tabLayout.setVisibility(View.VISIBLE);
            imgRecoder.setVisibility(View.VISIBLE);
            adapter.addFragment(new OwnVideoFragment().newInstance(Constants.OWNVIDEO, 1), mContext.getResources().getString(R.string.tab_title_ownvideo));
            adapter.addFragment(new OwnVideoFragment().newInstance(Constants.SHAREVIDEO, 2), mContext.getResources().getString(R.string.tab_title_shared_video));

        }
        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.imgRecoder)
    public void onRecoder() {
        if (checkPermission()) {
            Intent i = new Intent(mContext, VideoRecoderActivity.class);
            startActivity(i);
        } else {
            requestPermission();
        }
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(mContext, VideoRecoderActivity.class);
                    startActivity(i);
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
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
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


    @OnClick(R.id.imgDrawer)
    public void onViewClicked() {
        openDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(mContext.getClass().toString(), "onResume: ");
        initDrawer();
    }
}
