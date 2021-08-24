package com.techamove.view.VideoRecod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.techamove.R;
import com.techamove.Response.ResponseListener;
import com.techamove.Response.ResponsePresenter;
import com.techamove.Utils.Constants;
import com.techamove.Utils.UriHelper;
import com.techamove.view.ContactShare.ContactShareActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoRecoderActivity extends Activity implements ResponseListener {

    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.imgBell)
    ImageView imgBell;
    private Uri fileUri;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int GALLERY_VIDEO_ACTIVITY_REQUEST_CODE = 100;
    public static VideoRecoderActivity ActivityContext = null;
    ResponsePresenter presenter;
    String strFileUrl = "";
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recod);
        ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ActivityContext = this;
        presenter = new ResponsePresenter(ActivityContext, this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(ActivityContext.getResources().getString(R.string.lable_video_upload_progress));
        progressDialog.setMessage(ActivityContext.getResources().getString(R.string.lable_loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);


        iniview();
    }

    private void iniview() {
        imgDrawer.setImageDrawable(ActivityContext.getResources().getDrawable(R.drawable.ic_white_left));
        txtTitle.setText(ActivityContext.getResources().getString(R.string.lable_video));
        imgBell.setVisibility(View.VISIBLE);
        imgBell.setImageDrawable(ActivityContext.getResources().getDrawable(R.drawable.ic_white_camera));
        openDialogPicker();
    }

    private void openDialogPicker() {
        final CharSequence[] options = {ActivityContext.getResources().getString(R.string.lable_camera),
                ActivityContext.getResources().getString(R.string.lable_gallery), ActivityContext.getResources().getString(R.string.lable_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoRecoderActivity.this);
        builder.setTitle(ActivityContext.getResources().getString(R.string.title_dialog_video_picker));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(ActivityContext.getResources().getString(R.string.lable_camera))) {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

                } else if (options[item].equals(ActivityContext.getResources().getString(R.string.lable_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, GALLERY_VIDEO_ACTIVITY_REQUEST_CODE);
                } else if (options[item].equals(ActivityContext.getResources().getString(R.string.lable_cancel))) {
                    dialog.dismiss();
                    finish();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraVideo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }

        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                String selectedImagePath = UriHelper.getPath(this, resultUri);
                strFileUrl = selectedImagePath;
                videoView.setVideoPath(String.valueOf(data.getData()));
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                mediaController.isShowing();
                videoView.start();
            } else if (resultCode == RESULT_CANCELED) {
//                // User cancelled the video capture
//                Toast.makeText(this, "User cancelled the video capture.",
//                        Toast.LENGTH_LONG).show();
                onBackPressed();
            } else {
                onBackPressed();
//                // Video capture failed, advise user
//                Toast.makeText(this, "Video capture failed.",
//                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == GALLERY_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                String selectedImagePath = UriHelper.getPath(this, resultUri);
                strFileUrl = selectedImagePath;
                videoView.setVideoPath(String.valueOf(data.getData()));
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                mediaController.isShowing();
                videoView.start();
            } else if (resultCode == RESULT_CANCELED) {

                onBackPressed();
            } else {
                onBackPressed();
            }
        }
    }


    @OnClick({R.id.imgDrawer, R.id.btnSubmit, R.id.imgBell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgDrawer:
                onBackPressed();
                break;
            case R.id.imgBell:
                openDialogPicker();
                break;
            case R.id.btnSubmit:
                videoView.pause();
                if (!strFileUrl.equals("")) {
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(strFileUrl, MediaStore.Video.Thumbnails.MINI_KIND);
                    presenter.gsonVideoUpload(strFileUrl, new File(isSaveBitmap(bitmap)));

                }
                break;
        }
    }

    private String isSaveBitmap(Bitmap thumbnail) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraVideo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
//                return null;
            }
        }

//            Date date = new Date();
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//                    .format(date.getTime());
//
//            File  file = new File(mediaStorageDir.getPath() + File.separator +
//                    "VID_" + timeStamp + ".jpg");

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(mediaStorageDir, fname);
        Log.i(TAG, "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsoluteFile().toString();
    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {

        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    public static final String TAG = VideoRecoderActivity.class.getName();

    @Override
    public void onSuccessHandler(String response, String apiTag) {
        try {
            if (apiTag.equalsIgnoreCase(Constants.VIDEO_PROGRESS)) {
                Log.e(TAG, "onSuccessHandler: " + response);
                if (ActivityContext instanceof Activity && progressDialog != null && !progressDialog.isShowing() && !((Activity) ActivityContext).isFinishing()) {
                    progressDialog.show();
                }
                progressDialog.setProgress(Integer.parseInt(response));
                if (response.equals("100")) {
                    progressDialog.dismiss();
                }
            } else {
                JSONObject jsonObject = new JSONObject(response);
                Intent i = new Intent(ActivityContext, ContactShareActivity.class);
                i.putExtra(Constants.IDENTIFICATION, Constants.VIDEO_RECORDER_ACTIVITY);
                i.putExtra(Constants.VIDEOID, jsonObject.getJSONObject("data").getString("id"));
                startActivity(i);
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailureHandler() {

    }

    @Override
    public void onNetworkFailure(String message) {

    }
}