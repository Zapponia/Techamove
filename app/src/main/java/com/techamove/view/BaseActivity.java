package com.techamove.view;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Utility;
import com.techamove.view.BusinessCardSave.SaveCardActivity;
import com.techamove.view.BusinessCardShare.ShareCardActivity;
import com.techamove.view.ChangePassword.ChangePasswordActivity;
import com.techamove.view.Home.HomeActivity;
import com.techamove.view.ScanBusinessCard.ScanBusinessCardActivity;
import com.techamove.view.Setting.SettingActivity;
import com.techamove.view.BusinessCardVideo.BusinessCardVideoActivity;
import com.techamove.view.WebView.WebViewActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    Drawer drawer;
    ResponsePresenter presenter;
    ResponseListener listener;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    Context mContext;
    Bundle savedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        presenter = new ResponsePresenter(mContext, listener);

    }


    public void initDrawer() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View header = (ViewGroup) inflater.inflate(R.layout.nav_header_home, null, false);
        final View footer = inflater.inflate(R.layout.slide_footer, null, false);
        ImageView imgUserPhoto = header.findViewById(R.id.imgUserProfile);
        TextView txtName = header.findViewById(R.id.txtName);
        TextView txtEmail = header.findViewById(R.id.txtEmail);

        Button btnLogout = footer.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                dialogLogOut();
            }
        });

        txtName.setText(AppPreferences.getInstance(mContext).getFullName());
        txtEmail.setText(AppPreferences.getInstance(mContext).getEmail());
        Utility.loadImageFromUrl(mContext, imgUserPhoto, AppPreferences.getInstance(mContext).getAvatar(), R.drawable.ic_defualt);

        RecyclerView rvDrawerList = header.findViewById(R.id.rv_drawer_list);
        ArrayList<BaseModel> arrDrawerlist = new ArrayList<>();

        if (AppPreferences.getInstance(mContext).getAccountType().equals("2")) {
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_share_business_card), ContextCompat.getDrawable(mContext, R.drawable.ic_share_card)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_video_material), ContextCompat.getDrawable(mContext, R.drawable.ic_color_video)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_setting), ContextCompat.getDrawable(mContext, R.drawable.ic_color_settings)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_privacypolicy), ContextCompat.getDrawable(mContext, R.drawable.ic_color_privacypolicy)));
            rvDrawerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            final BaseAdapter mBaseAdapter = new BaseAdapter(mContext, arrDrawerlist, -1);
            rvDrawerList.setAdapter(mBaseAdapter);

            mBaseAdapter.setEventListener(new BaseAdapter.EventListener() {
                @Override
                public void onItemClick(int position) {
                    if (position == 0) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, ShareCardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (position == 1) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, BusinessCardVideoActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (position == 2) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, SettingActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                        closeDrawer();
                    }
                    if (position == 3) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, WebViewActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                }
            });
        } else {
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_my_business_card), ContextCompat.getDrawable(mContext, R.drawable.ic_color_card)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_qr_scanner), ContextCompat.getDrawable(mContext, R.drawable.ic_color_qrcode)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_video_material), ContextCompat.getDrawable(mContext, R.drawable.ic_color_video)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_save_business_card), ContextCompat.getDrawable(mContext, R.drawable.ic_color_save_card)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_share_business_card), ContextCompat.getDrawable(mContext, R.drawable.ic_share_card)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_setting), ContextCompat.getDrawable(mContext, R.drawable.ic_color_settings)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_change_password), ContextCompat.getDrawable(mContext, R.drawable.ic_password)));
            arrDrawerlist.add(new BaseModel(mContext.getString(R.string.menu_privacypolicy), ContextCompat.getDrawable(mContext, R.drawable.ic_color_privacypolicy)));
            rvDrawerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            final BaseAdapter mBaseAdapter = new BaseAdapter(mContext, arrDrawerlist, -1);
            rvDrawerList.setAdapter(mBaseAdapter);

            mBaseAdapter.setEventListener(new BaseAdapter.EventListener() {
                @Override
                public void onItemClick(int position) {

                    if (position == 0) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (position == 1) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, ScanBusinessCardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (position == 2) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, BusinessCardVideoActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (position == 3) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, SaveCardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (position == 4) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, ShareCardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (position == 5) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, SettingActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (position == 6) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, ChangePasswordActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    if (position == 7) {
                        mBaseAdapter.getPos(position);
                        closeDrawer();
                        Intent i = new Intent(mContext, WebViewActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                        closeDrawer();
                    }
                }
            });
        }


        drawer = new DrawerBuilder()
                .withActivity(this).withCloseOnClick(true)
                .withHeader(header)
                .withDisplayBelowStatusBar(false)
                .withHasStableIds(true)
                .withHeaderDivider(true)
                .withFooterClickable(true)
                .withStickyFooterShadow(false)
                .withSelectedItem(-1)
                .withStickyFooter((ViewGroup) footer)
                .withSliderBackgroundColor(mContext.getResources().getColor(R.color.white))
                .build();


        drawer.getSlider().setFitsSystemWindows(true);


    }

    private void dialogLogOut() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_log_out, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btnYes = alertDialog.findViewById(R.id.btnYes);
        Button btnNo = alertDialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                presenter.gsonLogOut();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }

    public void openDrawer() {
        drawer.openDrawer();
    }

    public void closeDrawer() {
        drawer.closeDrawer();
    }


}