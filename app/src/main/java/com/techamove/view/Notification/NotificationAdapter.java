package com.techamove.view.Notification;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;
import com.techamove.Utils.Utility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context mContext;
    private List<NotificationModel.DataEntity> data = new ArrayList<>();

    public NotificationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        NotificationModel.DataEntity d = data.get(position);

        holder.txtTital.setText(d.title);
        holder.txtDisc.setText(d.description);
        try {
            holder.txtTime.setText(Utility.formatTime(d.createdAt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgUserProfile)
        CircleImageView imgUserProfile;
        @BindView(R.id.txtTital)
        TextView txtTital;
        @BindView(R.id.txtDisc)
        TextView txtDisc;
        @BindView(R.id.txtTime)
        TextView txtTime;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    EventListener eventListener;

    public interface EventListener {

    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void addData(List<NotificationModel.DataEntity> d) {
        this.data.addAll(d);
        notifyDataSetChanged();
    }

}
