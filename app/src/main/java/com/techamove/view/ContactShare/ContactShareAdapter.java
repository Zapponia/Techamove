package com.techamove.view.ContactShare;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;
import com.techamove.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactShareAdapter extends RecyclerView.Adapter<ContactShareAdapter.ViewHolder> {
    Context mContext;

    ArrayList<ContectModel.Datum> arrayList = new ArrayList<>();

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
    }

    public void changeSelection(int position, boolean isMultiSel) {
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgUserProfile)
        CircleImageView imgUserProfile;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.imgUncheck)
        ImageView imgUncheck;

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
