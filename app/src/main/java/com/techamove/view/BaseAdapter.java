package com.techamove.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<BaseModel> arrPhotoVideolist = new ArrayList<>();
    EventListener mEventListener;
    int selectedPos;


    public BaseAdapter(Context mContext, ArrayList<BaseModel> arrPhotoVideolist, int selectedPos) {
        this.mContext = mContext;
        this.arrPhotoVideolist = arrPhotoVideolist;
        this.selectedPos = selectedPos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        BaseModel model = arrPhotoVideolist.get(position);
            holder.imgIcon.setImageDrawable(model.getIcon());
            holder.txtCategory.setText(model.getName());

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventListener.onItemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrPhotoVideolist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtName)
        TextView txtCategory;
        @BindView(R.id.llMain)
        LinearLayoutCompat llMain;
        @BindView(R.id.imgIcon)
        ImageView imgIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface EventListener {
        void onItemClick(int position);
    }

    public void setEventListener(EventListener mEventListener) {
        this.mEventListener = mEventListener;
    }

    public void getPos(int value) {
        selectedPos = value;
        notifyDataSetChanged();
    }


}
