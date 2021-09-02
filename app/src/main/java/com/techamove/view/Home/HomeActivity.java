package com.techamove.view.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.BaseActivity;
import com.techamove.view.BusinessCardAdd.AddBusinessCardActivity;
import com.techamove.view.BusinessCardEdit.EditBusinessCardActivity;
import com.techamove.view.BusinessCardView.ViewBusinessCardActivity;
import com.techamove.view.ContactShare.ContactShareActivity;
import com.techamove.view.Notification.NotificationActivity;
import com.techamove.view.PaymentPlan.PaymentPlanActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.techamove.Utils.Utility.setStatusBarGradiant;

public class HomeActivity extends BaseActivity implements ResponseListener {

    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.rvCard)
    RecyclerView rvCard;
    @BindView(R.id.framBottom)
    FrameLayout framBottom;
    @BindView(R.id.txtError)
    TextView txtError;
    @BindView(R.id.imgTryAgain)
    ImageView imgTryAgain;
    @BindView(R.id.llError)
    LinearLayout llError;


    Context mContext;
    CardAdapter adapter;
    @BindView(R.id.imgBell)
    ImageView imgBell;
    ResponsePresenter presenter;
    int cardPosition;


    private static final String TAG = "HomeActivity";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mContext = HomeActivity.this;
        presenter = new ResponsePresenter(mContext, this);
        iniview();
    }

    private void iniview() {
        txtTitle.setText(mContext.getResources().getString(R.string.menu_my_business_card));
        rvCard.setLayoutManager(new LinearLayoutManager(this));
        imgBell.setVisibility(View.VISIBLE);

        adapter = new CardAdapter(mContext);
        rvCard.setHasFixedSize(true);
        rvCard.setAdapter(adapter);
        adapter.setEventListener(new CardAdapter.EventListener() {
            @Override
            public void onItemClick(CardListModel.Datum model) {
                Intent i = new Intent(mContext, ViewBusinessCardActivity.class);
                i.putExtra(Constants.CARDLISTMODEL, new Gson().toJson(model));
                startActivity(i);
            }

            @Override
            public void onEditItem(CardListModel.Datum model) {
                Intent i = new Intent(mContext, EditBusinessCardActivity.class);
                i.putExtra(Constants.CARDLISTMODEL, new Gson().toJson(model));
                startActivity(i);
            }

            @Override
            public void onShareItem(String id) {
                Intent i = new Intent(mContext, ContactShareActivity.class);
                i.putExtra(Constants.CARDID, id);
                i.putExtra(Constants.IDENTIFICATION, Constants.HOMEPAGE);
                startActivity(i);
            }

            @Override
            public void onDelete(String id, int position) {
                cardPosition = position;
                openDeleteDialog(id);
            }

        });


//        presenter.gsonCardList();
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
                presenter.gsonDeleteCard(id);
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
        adapter.clearData();
        presenter.gsoncheckSubscription();
    }

    @OnClick(R.id.imgDrawer)
    void drawerOpen() {
        openDrawer();
    }

    @OnClick({R.id.framBottom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.framBottom:
                Intent i = new Intent(mContext, AddBusinessCardActivity.class);
                startActivity(i);
                break;
        }
    }

    @OnClick(R.id.imgBell)
    public void onViewClicked() {
        Intent i = new Intent(mContext, NotificationActivity.class);
        startActivity(i);
    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        if (apiTag.equals(Constants.API_SHOWCARDLISt)) {
            rvCard.setVisibility(View.VISIBLE);
            framBottom.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
            CardListModel cardListModel = Utility.getModelData(response, CardListModel.class);
            adapter.addData(cardListModel.data);

        } else if (apiTag.equals(Constants.API_DELETECARDDETAILS)) {
            adapter.itemRemoved(cardPosition);
            if (adapter.getItemCount() == 0) {
                onFailureHandler();
            }
        } else if (apiTag.equals(Constants.API_CHECKSUBSCRIPTION)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString(Constants.SUCCESS).equals(Constants.TRUE)) {
                    presenter.gsonCardList();
                } else {
                    //TODO uncomment this shit
                   /* if (!((Activity) mContext).isFinishing()) {
                        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
                        builder1.setCancelable(false);
                        builder1.setPositiveButton(mContext.getResources().getString(R.string.lable_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent i = new Intent(mContext, PaymentPlanActivity.class);
                                i.putExtra(Constants.IDENTIFICATION, Constants.HOMEPAGE);
                                i.putExtra(Constants.EMAIL, AppPreferences.getInstance(mContext).getEmail());
                                i.putExtra(Constants.ACCOUNTTYPE, AppPreferences.getInstance(mContext).getAccountType());
                                startActivity(i);

                            }
                        });
                        androidx.appcompat.app.AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
*/                      presenter.gsonCardList();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onFailureHandler() {
        framBottom.setVisibility(View.VISIBLE);
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
        presenter.gsonCardList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Log.e(mContext.getClass().toString(), "onActivityResult: ");
        }
    }


}
