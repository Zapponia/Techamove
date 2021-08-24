package com.techamove.view.BusinessCardVideo;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;
import com.techamove.Utils.Constants;
import com.techamove.Utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    Context mContext;
    String strIdentification;
    List<VideoModel.Datum> data = new ArrayList<>();

    public VideoAdapter(Context mContext, String strIdentification) {
        this.mContext = mContext;
        this.strIdentification = strIdentification;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (strIdentification.equals(Constants.SHAREVIDEO)) {
            holder.imgShare.setVisibility(View.GONE);
        }
//        try {
//            holder.imgVideo.setImageBitmap(retriveVideoFrameFromVideo(data.get(position).videos_url));
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
        Utility.loadImageFromUrl(mContext, holder.imgVideo, data.get(position).thumbImages, R.drawable.video_placeholder);

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventListener != null) {
                    eventListener.onShareItem(data.get(position).id);
                }
            }
        });
        holder.imgDalete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventListener != null) {
                    eventListener.onDeleteItem(data.get(position).id, position);
                }
            }
        });

        holder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventListener != null) {
                    eventListener.onPlayItem(data.get(position).videos_url);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<VideoModel.Datum> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgVideo)
        ImageView imgVideo;
        @BindView(R.id.imgShare)
        ImageView imgShare;
        @BindView(R.id.imgPlay)
        ImageView imgPlay;
        @BindView(R.id.imgDalete)
        ImageView imgDalete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    EventListener eventListener;

    public interface EventListener {
        void onShareItem(int id);

        void onDeleteItem(int id, int position);

        void onPlayItem(String videos_url);

    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
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
            //   mediaMetadataRetriever.setDataSource(videoPath);
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


}
