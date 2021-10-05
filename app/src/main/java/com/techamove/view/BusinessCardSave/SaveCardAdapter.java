package com.techamove.view.BusinessCardSave;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;
import com.techamove.view.BusinessCardVideo.VideoModel;
import com.techamove.view.Home.CardListModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaveCardAdapter extends RecyclerView.Adapter<SaveCardAdapter.ViewHolder> {
    Context mContext;
    List<CardListModel.Datum> data = new ArrayList<>();

    public SaveCardAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imgEdit.setVisibility(View.GONE);
        holder.imgShare.setVisibility(View.GONE);

        CardListModel.Datum model = data.get(position);
        holder.txtName.setText(model.full_name);
        holder.txtCompanyName.setText(model.business_name);
        holder.txtService.setText(model.business_service);
        holder.txtEmail.setText(model.email);
        holder.txtPhone.setText(model.mobile_number);
        holder.txtWebsite.setText(model.website_link);

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(model.id, BarcodeFormat.QR_CODE, 120, 120);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            holder.imgQrCode.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventListener != null) {
                    eventListener.onItemClick(model);
                }
            }
        });


        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventListener != null) {
                    eventListener.onDelete(model.id, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<CardListModel.Datum> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void itemRemoved(int cardPosition) {
        data.remove(cardPosition);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.imgEdit)
        ImageView imgEdit;
        @BindView(R.id.imgDelete)
        ImageView imgDelete;
        @BindView(R.id.imgShare)
        ImageView imgShare;
        @BindView(R.id.txtCompanyName)
        TextView txtCompanyName;
        @BindView(R.id.txtService)
        TextView txtService;
        @BindView(R.id.imgQrCode)
        ImageView imgQrCode;
        @BindView(R.id.txtEmail)
        TextView txtEmail;
        @BindView(R.id.txtPhone)
        TextView txtPhone;
        @BindView(R.id.txtWebsite)
        TextView txtWebsite;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    EventListener eventListener;

    public interface EventListener {
        void onItemClick(CardListModel.Datum model);

        void onDelete(String id, int position);
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }


}
