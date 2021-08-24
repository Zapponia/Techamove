package com.techamove.view.ViewVideo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.techamove.R;
import com.techamove.Utils.Constants;
import com.techamove.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends BaseActivity {
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.imgBell)
    ImageView imgBell;
    @BindView(R.id.rlMain)
    RelativeLayout rlMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video);
        ButterKnife.bind(this);
        iniview();
    }

    private void iniview() {
        imgDrawer.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_white_left));
        txtTitle.setText(getApplicationContext().getResources().getString(R.string.lable_video));
        if (getIntent().getExtras() != null) {
            String url = getIntent().getExtras().getString(Constants.URL);
            videoView.setVideoPath(String.valueOf(url));
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        if (videoView != null) {
            videoView.stopPlayback();
            videoView = null;
        }
        super.onDestroy();
    }

    @OnClick(R.id.imgDrawer)
    void onclick() {
        onBackPressed();
    }
}
