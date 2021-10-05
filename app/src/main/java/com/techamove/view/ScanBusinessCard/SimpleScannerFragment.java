package com.techamove.view.ScanBusinessCard;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;
import com.techamove.view.BusinessCardSave.SaveCardActivity;
import com.techamove.view.BusinessCardView.ViewBusinessCardActivity;
import com.techamove.view.Home.CardListModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by Android -pc 15 on 08/03/2019.
 */

public class SimpleScannerFragment extends Fragment implements ZBarScannerView.ResultHandler, ResponseListener {

    private ZBarScannerView mScannerView;
    ResponsePresenter presenter;
    String strIdentification = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZBarScannerView(getActivity());
        Log.e("Fragment", "Fragment");
        presenter = new ResponsePresenter(getActivity(), this);
        strIdentification = getActivity().getIntent().getStringExtra(Constants.IDENTIFICATION);
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }


    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result rawResult) {
//        Toast.makeText(getActivity(), "Fragment = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_LONG).show();

//        Toast.makeText(getActivity(), "Contents = " + rawResult.getContents() +
//                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(SimpleScannerFragment.this);
            }
        }, 2000);

        Log.e("ScanResult=====>>", "Fragment  " + rawResult.getContents());
        presenter.gsonCardDetails(rawResult.getContents());

//        Log.e("ScanResult=====>>", "" + rawResult.getContents());


    }

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            CardListModel.Datum model = Utility.getModelData(jsonObject.getJSONObject("data").toString(), CardListModel.Datum.class);

            if (Constants.SIGNUPACTIVITY.equals(strIdentification)) {
                Intent i = new Intent();;
                getActivity().setResult(getActivity().RESULT_OK, i);
                i.putExtra(Constants.USERID, model.users_id);
            } else {
                Intent i = new Intent(getActivity(), ViewBusinessCardActivity.class);
                i.putExtra(Constants.IDENTIFICATION, Constants.SCANCARDACTIVITY);
                i.putExtra(Constants.CARDLISTMODEL, new Gson().toJson(model));
                startActivity(i);
            }

//            getActivity().finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureHandler() {
        mScannerView.setAutoFocus(false);
    }

    @Override
    public void onNetworkFailure(String message) {

    }
}
