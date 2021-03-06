package com.techamove.view.ContactShare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.AddMember.AddMemberActivity;
import com.techamove.view.BusinessCardVideo.BusinessCardVideoActivity;
import com.techamove.view.Home.HomeActivity;
import com.techamove.view.Login.CustomerDataModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.techamove.Utils.Utility.setStatusBarGradiant;

public class ContactShareActivity extends AppCompatActivity implements ResponseListener {
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtAddMember)
    TextView txtAddMember;
    @BindView(R.id.rvGroup)
    RecyclerView rvGroup;
    @BindView(R.id.btnShare)
    Button btnShare;
    @BindView(R.id.rlmain)
    RelativeLayout rlmain;
    @BindView(R.id.txtError)
    TextView txtError;
    @BindView(R.id.imgTryAgain)
    ImageView imgTryAgain;
    @BindView(R.id.llError)
    LinearLayout llError;

    Context mContext;
    ContactShareAdapter shareAdapter;
    String strIdentification = "", strCardId = "", strVideoId = "";
    ResponsePresenter presenter;
    CustomerDataModel modelCustomerData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        mContext = ContactShareActivity.this;
        presenter = new ResponsePresenter(mContext, this);
        strIdentification = getIntent().getStringExtra(Constants.IDENTIFICATION);
        intview();
    }

    private void intview() {
        imgDrawer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_white_left));
        txtTitle.setText(mContext.getResources().getString(R.string.lable_share));

        shareAdapter = new ContactShareAdapter(mContext);
        rvGroup.setHasFixedSize(true);
        rvGroup.setLayoutManager(new LinearLayoutManager(this));
        rvGroup.setAdapter(shareAdapter);
        shareAdapter.setEventListener(new ContactShareAdapter.EventListener() {
            @Override
            public void onItemClick(boolean isSelected, int position) {
                shareAdapter.changeSelection(position, true);
            }
        });
        if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.HOMEPAGE)) {
            strCardId = getIntent().getStringExtra(Constants.CARDID);
            txtAddMember.setVisibility(View.VISIBLE);
            presenter.gsonUserList(strCardId);

        } else if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.VIDEO_RECORDER_ACTIVITY)) {
            strVideoId = getIntent().getStringExtra(Constants.VIDEOID);
            txtAddMember.setVisibility(View.VISIBLE);
            presenter.gsonVideoUserList(strVideoId);
        }
    }

    @OnClick({R.id.imgDrawer, R.id.btnShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgDrawer:
                if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.VIDEO_RECORDER_ACTIVITY)) {
                    Intent i = new Intent(mContext, BusinessCardVideoActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    onBackPressed();
                }

                break;
            case R.id.btnShare:
                if (!shareAdapter.getSelectedOne()) {
                    Utility.customToast(mContext, mContext.getResources().getString(R.string.err_msg_select_one));
                } else {
                    if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.HOMEPAGE)) {
                        presenter.gsonShareCard(strCardId, shareAdapter.getSelectedIds());
                    } else if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.VIDEO_RECORDER_ACTIVITY)) {
                        presenter.gsonShareVideo(strVideoId, shareAdapter.getSelectedIds());
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        if (apiTag.equals(Constants.API_PREMIUM)) {
            rlmain.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
            ContectModel model = Utility.getModelData(response, ContectModel.class);
            shareAdapter.addData(model.data);
            presenter.gsonAllRecord();
        } else if (apiTag.equals(Constants.API_VIDEO_PREMIUM)) {
            rlmain.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
            ContectModel model = Utility.getModelData(response, ContectModel.class);
            shareAdapter.addData(model.data);
        } else if (apiTag.equals(Constants.API_SHARECARD)) {
            String strSendSms = "";
            boolean isMatches = false;

            /*--------------- SEND SMS ------------------*/
            ArrayList<ContectModel.Datum> arrayUser = shareAdapter.getSelectedUser();
            for (int i = 0; i < arrayUser.size(); i++) {
                for (int j = 0; j < modelCustomerData.data.size(); j++) {
                    if (arrayUser.get(i).email.equals(modelCustomerData.data.get(j).email)) {
                        isMatches = true;
                    }
                }
                if (!isMatches) {
                    strSendSms = strSendSms + arrayUser.get(i).id + ",";
                } else {
                    isMatches = false;
                }
            }
            if (!TextUtils.isEmpty(strSendSms)) {
                if (strSendSms.length() > 0) {
                    strSendSms = strSendSms.substring(0, strSendSms.length() - 1);
                }
                presenter.gsonSendSms(strCardId, strSendSms);
            } else {
                Intent i = new Intent(mContext, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(i);
                ((Activity) mContext).finish();
            }

        } else if (apiTag.equals(Constants.API_SHARE_VIDEO)) {
            Intent i = new Intent(mContext, BusinessCardVideoActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(i);
            ((Activity) mContext).finish();
        } else if (apiTag.equals(Constants.API_CUSTOMERS)) {
            modelCustomerData = Utility.getModelData(response, CustomerDataModel.class);

        } else if (apiTag.equalsIgnoreCase(Constants.API_SHENDSMS)) {
            Intent i = new Intent(mContext, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(i);
            ((Activity) mContext).finish();
        }

    }

    @Override
    public void onFailureHandler() {
        rlmain.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        txtError.setText(mContext.getResources().getString(R.string.empty_list_msg));
        imgTryAgain.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetworkFailure(String message) {
        rlmain.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        txtError.setText(message);
        imgTryAgain.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.imgTryAgain)
    public void tryAgain() {
        if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.HOMEPAGE)) {
            presenter.gsonUserList(strCardId);
        } else if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.VIDEO_RECORDER_ACTIVITY)) {
            presenter.gsonVideoUserList(strVideoId);
        }
    }

    @OnClick(R.id.txtAddMember)
    void onAddMember() {
        Intent i = new Intent(mContext, AddMemberActivity.class);
        startActivityForResult(i, 123);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.HOMEPAGE)) {
                    presenter.gsonUserList(strCardId);
                } else if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.VIDEO_RECORDER_ACTIVITY)) {
                    presenter.gsonVideoUserList(strVideoId);
                }
            }
        }
    }
}
