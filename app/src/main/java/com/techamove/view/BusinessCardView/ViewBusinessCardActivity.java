package com.techamove.view.BusinessCardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.view.BusinessCardSave.SaveCardActivity;
import com.techamove.view.Home.CardListModel;
import com.techamove.view.WebView.WebViewActivity;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.techamove.Utils.Utility.setStatusBarGradiant;

public class ViewBusinessCardActivity extends AppCompatActivity implements ResponseListener {
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.imgQrCode)
    ImageView imgQrCode;
    @BindView(R.id.edtFullName)
    TextView edtFullName;
    @BindView(R.id.edtEmail)
    TextView edtEmail;
    @BindView(R.id.edtMobile)
    TextView edtMobile;
    @BindView(R.id.edtBusinessName)
    TextView edtBusinessName;
    @BindView(R.id.edtBusinessservice)
    TextView edtBusinessservice;
    @BindView(R.id.edtFacebook)
    TextView edtFacebook;
    @BindView(R.id.edtLinkedin)
    TextView edtLinkedin;
    @BindView(R.id.edtGmail)
    TextView edtGmail;
    @BindView(R.id.edtTwitter)
    TextView edtTwitter;
    @BindView(R.id.edtInstagram)
    TextView edtInstagram;
    @BindView(R.id.edtWebsite)
    TextView edtWebsite;
    @BindView(R.id.btnSaveCard)
    Button btnSaveCard;
    @BindView(R.id.txtUserId)
    TextView txtUserId;


    Context mContext;
    String strIdentification = "";
    CardListModel.Datum model;
    ResponsePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_view_business_card);
        ButterKnife.bind(this);
        mContext = ViewBusinessCardActivity.this;
        strIdentification = getIntent().getStringExtra(Constants.IDENTIFICATION);
        presenter = new ResponsePresenter(mContext, this);
        intview();
    }

    private void intview() {
        imgDrawer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_white_left));
        if (!TextUtils.isEmpty(strIdentification) && strIdentification.equalsIgnoreCase(Constants.SAVECARDACTIVITY)) {
            txtTitle.setText(mContext.getResources().getString(R.string.menu_save_business_card));
            model = new Gson().fromJson(getIntent().getStringExtra(Constants.CARDLISTMODEL), CardListModel.Datum.class);
            setData();
        } else if (!TextUtils.isEmpty(strIdentification) && strIdentification.equalsIgnoreCase(Constants.SCANCARDACTIVITY)) {
            txtTitle.setText(mContext.getResources().getString(R.string.title_connection));
            btnSaveCard.setVisibility(View.VISIBLE);
            model = new Gson().fromJson(getIntent().getStringExtra(Constants.CARDLISTMODEL), CardListModel.Datum.class);
            setData();
        } else {
            txtTitle.setText(mContext.getResources().getString(R.string.menu_my_business_card));
            model = new Gson().fromJson(getIntent().getStringExtra(Constants.CARDLISTMODEL), CardListModel.Datum.class);
            setData();
        }


    }


    private void setData() {
        edtFullName.setText(model.full_name);
        edtEmail.setText(model.email);
        edtMobile.setText(model.countryCode + model.mobile_number);
        edtBusinessName.setText(model.business_name);
        edtBusinessservice.setText(model.business_service);
        edtFacebook.setText(model.facebook);
        edtLinkedin.setText(model.linkedin);
        edtGmail.setText(model.google);
        edtTwitter.setText(model.twitter);
        edtInstagram.setText(model.instagram);
        edtWebsite.setText(model.website_link);
        txtUserId.setText(model.users_id);

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(model.id, BarcodeFormat.QR_CODE, 120, 120);
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

        onTouchEvent();
    }

    @OnClick({R.id.imgDrawer, R.id.btnSaveCard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgDrawer:
                finish();
                break;
            case R.id.btnSaveCard:
                presenter.gsonSaveCard(model.id);
                break;

        }
    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        if (apiTag.equals(Constants.API_SAVE_CARD)) {
            Intent i = new Intent(mContext, SaveCardActivity.class);
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

    private void onTouchEvent() {
        edtFacebook.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (edtFacebook.getRight() - edtFacebook.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (model.facebook != null) {
                            Intent i = new Intent(mContext, WebViewActivity.class);
                            i.putExtra(Constants.IDENTIFICATION, Constants.VIEW_BUSINESS_CARD);
                            i.putExtra(Constants.SOCIAL_URL, model.facebook);
                            startActivity(i);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        edtGmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (edtGmail.getRight() - edtGmail.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (model.google != null) {
                            Intent i = new Intent(mContext, WebViewActivity.class);
                            i.putExtra(Constants.IDENTIFICATION, Constants.VIEW_BUSINESS_CARD);
                            i.putExtra(Constants.SOCIAL_URL, model.google);
                            startActivity(i);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        edtInstagram.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (edtInstagram.getRight() - edtInstagram.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (model.instagram != null) {
                            Intent i = new Intent(mContext, WebViewActivity.class);
                            i.putExtra(Constants.IDENTIFICATION, Constants.VIEW_BUSINESS_CARD);
                            i.putExtra(Constants.SOCIAL_URL, model.instagram);
                            startActivity(i);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        edtLinkedin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (edtLinkedin.getRight() - edtLinkedin.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (model.linkedin != null) {
                            Intent i = new Intent(mContext, WebViewActivity.class);
                            i.putExtra(Constants.IDENTIFICATION, Constants.VIEW_BUSINESS_CARD);
                            i.putExtra(Constants.SOCIAL_URL, model.linkedin);
                            startActivity(i);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        edtTwitter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (edtTwitter.getRight() - edtTwitter.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (model.twitter != null) {
                            Intent i = new Intent(mContext, WebViewActivity.class);
                            i.putExtra(Constants.IDENTIFICATION, Constants.VIEW_BUSINESS_CARD);
                            i.putExtra(Constants.SOCIAL_URL, model.twitter);
                            startActivity(i);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        edtWebsite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (edtWebsite.getRight() - edtWebsite.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (model.website_link != null) {
                            Intent i = new Intent(mContext, WebViewActivity.class);
                            i.putExtra(Constants.IDENTIFICATION, Constants.VIEW_BUSINESS_CARD);
                            i.putExtra(Constants.SOCIAL_URL, model.website_link);
                            startActivity(i);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }


}
