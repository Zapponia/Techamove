package com.techamove.view.BusinessCardVideo;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.ContactShare.ContactShareActivity;
import com.techamove.view.ViewVideo.VideoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OwnVideoFragment extends Fragment implements ResponseListener {
    @BindView(R.id.rvVideo)
    RecyclerView rvVideo;
    @BindView(R.id.txtError)
    TextView txtError;
    @BindView(R.id.imgTryAgain)
    ImageView imgTryAgain;
    @BindView(R.id.llError)
    LinearLayout llError;

    LinearLayout llErrorOwn;
    RecyclerView rvOwn;

    String strIdentification = "";
    ResponsePresenter presenter;
    VideoAdapter adapter;
    int position;

    public OwnVideoFragment newInstance(String type, int position) {
        Bundle args = new Bundle();
        args.putString(Constants.IDENTIFICATION, type);
        OwnVideoFragment f = new OwnVideoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strIdentification = getArguments().getString(Constants.IDENTIFICATION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, rootView);
        presenter = new ResponsePresenter(getActivity(), this);
        llErrorOwn = rootView.findViewById(R.id.llError);
        rvOwn = rootView.findViewById(R.id.rvVideo);
        iniview();
        return rootView;
    }

    private void iniview() {
        rvVideo.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        adapter = new VideoAdapter(getActivity(), strIdentification);
        rvVideo.setHasFixedSize(true);
        rvVideo.setAdapter(adapter);
        adapter.setEventListener(new VideoAdapter.EventListener() {
            @Override
            public void onShareItem(int id) {
                Intent i = new Intent(getActivity(), ContactShareActivity.class);
                i.putExtra(Constants.IDENTIFICATION, Constants.VIDEO_RECORDER_ACTIVITY);
                i.putExtra(Constants.VIDEOID, String.valueOf(id));
                startActivity(i);
            }

            @Override
            public void onDeleteItem(int id, int i) {
                position = i;
                openDeleteDialog(id);
            }

            @Override
            public void onPlayItem(String videos_url) {
                Intent i = new Intent(getActivity(), VideoActivity.class);
                i.putExtra(Constants.URL, videos_url);
                startActivity(i);
            }
        });

        if (strIdentification.equals(Constants.OWNVIDEO)) {
            presenter.gsonOwnVideoList();
        } else {
            presenter.gsonShareVideoList();
        }
    }

    private void openDeleteDialog(int id) {
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete_video, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                if (strIdentification.equals(Constants.OWNVIDEO)) {
                    presenter.gsonOwnVideoDelete(id);
                } else {
                    presenter.gsonShareVideoDelete(id);
                }
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
    public void onSuccessHandler(String response, String apiTag) {
        if (apiTag.equals(Constants.API_OWNVIDEO)) {
            rvOwn.setVisibility(View.VISIBLE);
            llErrorOwn.setVisibility(View.GONE);

            VideoModel model = Utility.getModelData(response, VideoModel.class);
            adapter.addData(model.data);
        } else if (apiTag.equals(Constants.API_SHARE_VIDEO_LIST)) {
            rvVideo.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);

            VideoModel model = Utility.getModelData(response, VideoModel.class);
            adapter.addData(model.data);
        } else if (apiTag.equals(Constants.API_OWNVIDEODELETE)) {
            adapter.removeData(position);
        } else if (apiTag.equals(Constants.API_SHAREVIDEODELETE)) {
            adapter.removeData(position);
        }
    }

    @Override
    public void onFailureHandler() {
        if (strIdentification.equals(Constants.OWNVIDEO)) {
            rvOwn.setVisibility(View.GONE);
            llErrorOwn.setVisibility(View.VISIBLE);
            txtError.setText(getActivity().getResources().getString(R.string.empty_list_msg));
            imgTryAgain.setVisibility(View.GONE);
        } else {
            rvVideo.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
            txtError.setText(getActivity().getResources().getString(R.string.empty_list_msg));
            imgTryAgain.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNetworkFailure(String message) {
        if (strIdentification.equals(Constants.OWNVIDEO)) {
            llErrorOwn.setVisibility(View.VISIBLE);
            txtError.setText(message);
            imgTryAgain.setVisibility(View.VISIBLE);
        } else {
            llError.setVisibility(View.VISIBLE);
            txtError.setText(message);
            imgTryAgain.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.imgTryAgain)
    public void tryAgain() {
        if (strIdentification.equals(Constants.OWNVIDEO)) {
            presenter.gsonOwnVideoList();
        } else {
            presenter.gsonShareVideoList();
        }
    }

}
