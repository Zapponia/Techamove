package com.techamove.view.Notification;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends BaseActivity implements ResponseListener {
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.rvCard)
    RecyclerView rvCard;
    @BindView(R.id.framBottom)
    FrameLayout framBottom;
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

    Context mContext;
    NotificationAdapter adapter;
    ResponsePresenter presenter;

//    EndlessList endlessList;

    private int currentPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mContext = NotificationActivity.this;

        presenter = new ResponsePresenter(mContext, this);
        iniview();
    }

    private void iniview() {
        framBottom.setVisibility(View.GONE);
        imgDrawer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_white_left));
        txtTitle.setText(mContext.getResources().getString(R.string.title_notification));

        rvCard.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(mContext);

//        endlessList = new EndlessList(rvCard, new LinearLayoutManager(this));
//        endlessList.lockMoreLoading();


        rvCard.setHasFixedSize(true);
        rvCard.setAdapter(adapter);

        presenter.gsonNotificationList(currentPage + "");

//        endlessList.setOnLoadMoreListener(new EndlessList.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                endlessList.lockMoreLoading();
//                if (currentPage != 1) {
//                    // progressBar.setVisibility(View.VISIBLE);
//                } else {
//                    // progressBar.setVisibility(View.GONE);
//                }
//                presenter.gsonNotificationList(currentPage + "");
//            }
//        });


    }

    @OnClick({R.id.imgDrawer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgDrawer:
                finish();
                break;
        }
    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        if (apiTag.equalsIgnoreCase(Constants.API_NOTIFICATION)) {
            NotificationModel cardListModel = Utility.getModelData(response, NotificationModel.class);
            if (cardListModel.data.size() > 0) {
                llError.setVisibility(View.GONE);
                rvCard.setVisibility(View.VISIBLE);
                if (adapter != null && cardListModel.data.size() != 0) {
//                    endlessList.releaseLock();
//                    currentPage++;
                    adapter.addData(cardListModel.data);
                }
//                if (adapter.getItemCount() < 10) {
//                    endlessList.disableLoadMore();
//                }
            } else if (currentPage == 1 && cardListModel.data.size() == 0) {
                llError.setVisibility(View.VISIBLE);
                rvCard.setVisibility(View.GONE);
            }
            framBottom.setVisibility(View.GONE);
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
        presenter.gsonNotificationList(currentPage + "");
    }


}
