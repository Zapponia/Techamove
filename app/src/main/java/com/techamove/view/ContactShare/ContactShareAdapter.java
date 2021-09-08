package com.techamove.view.ContactShare;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.techamove.R;
import com.techamove.Utils.Utility;
import com.techamove.view.Home.CardListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactShareAdapter extends RecyclerView.Adapter<ContactShareAdapter.ViewHolder> {
    Context mContext;

    //ArrayList<ContectModel.Datum> arrayList = new ArrayList<>();
    List<CardListModel.Datum> data = new ArrayList<>();

    public ContactShareAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CardListModel.Datum model = data.get(position);

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

        /*
                if (arrayList.get(position).isSelected) {
            holder.imgUncheck.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check));
        } else {
            holder.imgUncheck.setImageDrawable(mContext.getResources().getDrawable(R.drawable.uncheck));
        }
        holder.txtName.setText(arrayList.get(position).full_name);
        Utility.loadImageFromUrl(mContext, holder.imgUserProfile, arrayList.get(position).avatar, R.drawable.ic_defualt);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventListener != null) {
                    eventListener.onItemClick(true, position);
                }

            }
        });
         */
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

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

/*    public void changeSelection(int position, boolean isMultiSel) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (position == i) {
                arrayList.get(i).isSelected = !arrayList.get(i).isSelected;
            } else if (!isMultiSel) {
                arrayList.get(i).isSelected = false;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addData(List<ContectModel.Datum> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public String getSelectedIds() {
        String str = "";

        for (ContectModel.Datum spinner : arrayList) {
            if (spinner.isSelected) {
                str = str + spinner.id + ",";
            }
        }

        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    public ArrayList<ContectModel.Datum> getSelectedUser() {
        ArrayList<ContectModel.Datum> arrayUser = new ArrayList<>();

        for (ContectModel.Datum spinner : arrayList) {
            if (spinner.isSelected) {
                ContectModel.Datum model = new ContectModel.Datum();
                model.id = spinner.id;
                model.email = spinner.email;
                arrayUser.add(model);
            }
        }

        return arrayUser;
    }

    public boolean getSelectedOne() {
        for (ContectModel.Datum spinner : arrayList) {
            if (spinner.isSelected) {
                return true;
            }
        }

        return false;
    }
*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgQrCode)
        ImageView imgQrCode;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    EventListener eventListener;

    public interface EventListener {
        void onItemClick(boolean isSelected, int position);

    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }


}
