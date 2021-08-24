package com.techamove.view.PaymentPlan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.AppPreferences;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.Home.HomeActivity;
import com.techamove.view.Login.NewLoginActivity;
import com.techamove.view.SignUp.SignUpModel;
import com.techamove.view.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentPlanActivity extends AppCompatActivity implements ResponseListener, PurchasesUpdatedListener {
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.rvPlan)
    RecyclerView rvPlan;

    Context mContext;
    PlanAdapter adapter;
    ResponsePresenter presenter;
    String strEmail = "", strIdentification = "", strProductName = "", strAccountType = "";
    private BillingClient billingClient;
    private static final String TAG = "PaymentPlanActivity";
    BillingFlowParams flowParams;
    SignUpModel modelSignup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_plan);
        ButterKnife.bind(this);
        mContext = PaymentPlanActivity.this;
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        presenter = new ResponsePresenter(mContext, PaymentPlanActivity.this);
        strEmail = getIntent().getStringExtra(Constants.EMAIL);
        strIdentification = getIntent().getStringExtra(Constants.IDENTIFICATION);
        modelSignup = new Gson().fromJson(getIntent().getStringExtra(Constants.SIGNUPMODEL), new TypeToken<SignUpModel>() {
        }.getType());

        iniview();
    }

    List<SkuDetails> compySkuDetailsList = new ArrayList<>();

    private void iniview() {
        txtTitle.setText(mContext.getResources().getString(R.string.title_payment_plan));
        rvPlan.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter = new PlanAdapter(mContext);
        rvPlan.setHasFixedSize(true);
        rvPlan.setAdapter(adapter);

        if (Constants.SIGNUPACTIVITY.equals(strIdentification)) {
            strAccountType = modelSignup.AccountType;
        } else {

            strAccountType = getIntent().getStringExtra(Constants.ACCOUNTTYPE);
        }

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.e("onBillingSetup: ", "CONNECTED: " + BillingClient.BillingResponseCode.OK + "");

                    List<String> skuList = new ArrayList<>();
                    if ("3".equals(strAccountType)) {
                        skuList.add("com.subscription.techamove.affiliate");
                    } else {
                        skuList.add("com.subscription.techamove.premium");
                    }
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {
                                    Log.e("BillingResponse: ", "CONNECTED: " + BillingClient.BillingResponseCode.OK + "");
                                    Log.e("BillingResponse: ", "skuDetailsList: " + skuDetailsList.size() + "");
                                    Log.e("BillingResponse: ", "ResponseCode: " + billingResult.getResponseCode() + "");
                                    Log.e("BillingResponse: ", "DebugMessage: " + billingResult.getDebugMessage() + "");
                                    Log.e("BillingResponse: ", "Response" + new Gson().toJson(skuDetailsList).replaceAll("\\\\", ""));

                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                        compySkuDetailsList.addAll(skuDetailsList);
                                        ArrayList<SkuDetailsModel> arraySkuDetails = new ArrayList<>();
                                        for (SkuDetails skuDetails : skuDetailsList) {

                                            SkuDetailsModel model = new SkuDetailsModel();
                                            model.mTitle = skuDetails.getTitle();
                                            model.mDescription = skuDetails.getDescription();
                                            model.mPrice = skuDetails.getPrice();
                                            model.mPriceCurrencyCode = skuDetails.getPriceCurrencyCode();
                                            model.mType = skuDetails.getType();
                                            model.mSku = skuDetails.getSku();

                                            arraySkuDetails.add(model);
                                        }
                                        Log.e("SkuDetails ", "--->  " + new Gson().toJson(arraySkuDetails));

                                        adapter.addData(arraySkuDetails);

                                    }

                                }
                            });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });


        adapter.setEventListener(new PlanAdapter.EventListener() {
            @Override
            public void onItemClick(SkuDetailsModel model, int position) {

                flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(compySkuDetailsList.get(position))
                        .build();

                BillingResult responseCode = billingClient.launchBillingFlow(PaymentPlanActivity.this, flowParams);
            }
        });
    }

    private void dialogLicence() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_licence_key, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        ImageView imgClose = alertDialog.findViewById(R.id.imgClose);
        EditText edtLicenceKey = alertDialog.findViewById(R.id.edtLicenceKey);
        Button btnContinue = alertDialog.findViewById(R.id.btnContinue);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                Intent i = new Intent(mContext, NewLoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                presenter.gsonLicenseVerify(edtLicenceKey.getText().toString().trim());
            }
        });
    }

    @OnClick(R.id.imgBack)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        if (apiTag.equals(Constants.API_PURCHASE)) {
            if (Constants.HOMEPAGE.equalsIgnoreCase(strIdentification)) {
                finish();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!((Activity) mContext).isFinishing()) {
                        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                        builder1.setTitle(mContext.getResources().getString(R.string.app_name));
                        builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(mContext.getResources().getString(R.string.lable_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                dialogLicence();
                            }
                        });
                        androidx.appcompat.app.AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else if (apiTag.equals(Constants.API_REGISTER)) {
            if (strPackageName.equalsIgnoreCase("com.subscription.techamove.affiliate")) {
                presenter.gsonpurchase(strEmail, strPackageName, "affiliate");
            } else {
                presenter.gsonpurchase(strEmail, strPackageName, "premium");
            }


        } else {
            if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.SIGNUPACTIVITY)) {
                Intent i = new Intent(mContext, NewLoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                UserModel model = Utility.getModelData(response, UserModel.class);
                AppPreferences.getInstance(mContext).setID(model.data.id);
                AppPreferences.getInstance(mContext).setFullName(model.data.full_name);
                AppPreferences.getInstance(mContext).setInAppId(model.data.in_app_purchase_id);
                AppPreferences.getInstance(mContext).setAccountType(model.data.account_type);
                AppPreferences.getInstance(mContext).setAvatar(model.data.avatar);
                AppPreferences.getInstance(mContext).setEmail(model.data.email);
                AppPreferences.getInstance(mContext).setLicenceKey(model.data.licence_key);
                AppPreferences.getInstance(mContext).setMobile(model.data.mobile_number);
                AppPreferences.getInstance(mContext).setToken(model.data.token);
                AppPreferences.getInstance(mContext).setDeviceToken(model.data.device_token);

                Intent i = new Intent(mContext, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }

    }

    @Override
    public void onFailureHandler() {

    }

    @Override
    public void onNetworkFailure(String message) {

    }

    String strPackageName = "";

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        Log.e(TAG, "onPurchasesUpdated: " + new Gson().toJson(purchases));
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                //region Data Log
                Log.e(TAG, "onPurchasesUpdated: getOrderId" + purchase.getOrderId());
                Log.e(TAG, "onPurchasesUpdated: getOriginalJson" + purchase.getOriginalJson());
                Log.e(TAG, "onPurchasesUpdated: getPackageName" + purchase.getPackageName());
                Log.e(TAG, "onPurchasesUpdated: getPurchaseToken" + purchase.getPurchaseToken());
                Log.e(TAG, "onPurchasesUpdated: getSignature" + purchase.getSignature());
                Log.e(TAG, "onPurchasesUpdated: getSku" + purchase.getSku());
                Log.e(TAG, "onPurchasesUpdated: getPurchaseTime" + purchase.getPurchaseTime());
                //endregion

                strPackageName = purchase.getSku();

                if (Constants.SIGNUPACTIVITY.equalsIgnoreCase(strIdentification)) {
                    presenter.gsonRegister(modelSignup.ProfilePath, modelSignup.FullName, modelSignup.Email,
                            modelSignup.CountryCode, modelSignup.Mobile, modelSignup.AccountType, modelSignup.Password, modelSignup.ReferralCode);
                } else {
                    if (purchase.getSku().equalsIgnoreCase("com.subscription.techamove.affiliate")) {
                        presenter.gsonpurchase(strEmail, purchase.getSku(), "affiliate");
                    } else {
                        presenter.gsonpurchase(strEmail, purchase.getSku(), "premium");
                    }
                }

            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.e(TAG, "onPurchasesUpdated: USER_CANCELED");
            //User Cancel the process
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_UNAVAILABLE) {
            //The item you requested is not available for purchase
            Log.e(TAG, "onPurchasesUpdated: ITEM_UNAVAILABLE");
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Log.e(TAG, "onPurchasesUpdated: ITEM_ALREADY_OWNED");
        }
        Log.e(TAG, "onPurchasesUpdated: " + billingResult.getResponseCode());
    }

}
