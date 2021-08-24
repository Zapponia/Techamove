package com.techamove.view.BusinessCardSave;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.BaseActivity;
import com.techamove.view.BusinessCardView.ViewBusinessCardActivity;
import com.techamove.view.Home.CardListModel;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.techamove.Utils.Utility.setStatusBarGradiant;

public class SaveCardActivity extends BaseActivity implements ResponseListener {
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.rvCard)
    RecyclerView rvCard;
    @BindView(R.id.imgBell)
    ImageView imgBell;
    @BindView(R.id.txtError)
    TextView txtError;
    @BindView(R.id.imgTryAgain)
    ImageView imgTryAgain;
    @BindView(R.id.llError)
    LinearLayout llError;
    @BindView(R.id.rlMain)
    RelativeLayout rlMain;
    @BindView(R.id.framBottom)
    FrameLayout framBottom;

    Context mContext;
    SaveCardAdapter adapter;
    ResponsePresenter presenter;
    int cardPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mContext = SaveCardActivity.this;
        presenter = new ResponsePresenter(mContext, this);
        iniview();
    }

    private void iniview() {
        framBottom.setVisibility(View.GONE);
        txtTitle.setText(mContext.getResources().getString(R.string.menu_save_business_card));
        rvCard.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SaveCardAdapter(mContext);
        rvCard.setHasFixedSize(true);
        rvCard.setAdapter(adapter);
        adapter.setEventListener(new SaveCardAdapter.EventListener() {
            @Override
            public void onItemClick(CardListModel.Datum model) {
                Intent i = new Intent(mContext, ViewBusinessCardActivity.class);
                i.putExtra(Constants.IDENTIFICATION, Constants.SAVECARDACTIVITY);
                i.putExtra(Constants.CARDLISTMODEL, new Gson().toJson(model));
                startActivity(i);
            }

            @Override
            public void onDelete(String id, int position) {
                cardPosition = position;
                openDeleteDialog(id);
            }
        });
        presenter.gsonSaveCardList();
    }

    private void openDeleteDialog(String id) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_card, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnYes = alertDialog.findViewById(R.id.btnYes);
        Button btnNo = alertDialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                presenter.gsonSaveCardDelete(id);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
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
        if (apiTag.equals(Constants.API_SAVECARDLIST)) {
            rvCard.setVisibility(View.VISIBLE);
            framBottom.setVisibility(View.GONE);
            llError.setVisibility(View.GONE);
            CardListModel cardListModel = Utility.getModelData(response, CardListModel.class);
            adapter.addData(cardListModel.data);
        } else if (apiTag.equals(Constants.API_SAVE_CARD_DELETE)) {
            adapter.itemRemoved(cardPosition);
            if (adapter.getItemCount() == 0) {
                onFailureHandler();
            }
        }
    }

    @Override
    public void onFailureHandler() {
        framBottom.setVisibility(View.GONE);
        rvCard.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        txtError.setText(mContext.getResources().getString(R.string.empty_list_msg));
        imgTryAgain.setVisibility(View.GONE);
    }

    @Override
    public void onNetworkFailure(String message) {
        framBottom.setVisibility(View.GONE);
        rvCard.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        imgTryAgain.setVisibility(View.VISIBLE);
        txtError.setText(message);
    }

    @OnClick(R.id.imgTryAgain)
    public void tryAgain() {
        presenter.gsonSaveCardList();
    }
}
