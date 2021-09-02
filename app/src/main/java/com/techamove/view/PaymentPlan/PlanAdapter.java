package com.techamove.view.PaymentPlan;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techamove.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    Context mContext;

    ArrayList<SkuDetailsModel> arraySkuDetails = new ArrayList<>();

    public PlanAdapter(Context mContext) {
        this.mContext = mContext;
        Log.e("Here", "PlanAdapter Function");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("Here", "onCreateViewHolder - Start");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        Log.e("Here", "onCreateViewHolder - End");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e("Here", "onBindViewHolder - Start");
        SkuDetailsModel model = arraySkuDetails.get(position);
        holder.txtPrice.setText(model.mPrice);
        Log.e("Txt", "---> " + holder.txtPrice);
        holder.txtCurrencySys.setText(model.mPriceCurrencyCode);
        holder.txtPlanDisc.setText(model.mDescription);
        if ("com.subscription.techamove.affiliate".equals(model.mSku)) {
            holder.txtPlanName.setText("Affiliate " + mContext.getString(R.string.lbl_yearly));
        } else if ("com.subscription.techamove.premium".equals(model.mSku)) {
            holder.txtPlanName.setText("Premium " + mContext.getString(R.string.lbl_yearly));
        }

        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventListener != null) {
                    eventListener.onItemClick(model, position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arraySkuDetails.size();
    }

    public void addData(ArrayList<SkuDetailsModel> arraySkuDetails) {
        this.arraySkuDetails.addAll(arraySkuDetails);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgUserProfile)
        ImageView imgUserProfile;
        @BindView(R.id.txtPlanName)
        TextView txtPlanName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.txtCurrencySys)
        TextView txtCurrencySys;
        @BindView(R.id.txtPlanDisc)
        TextView txtPlanDisc;
        @BindView(R.id.btnSubmit)
        Button btnSubmit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    EventListener eventListener;

    public interface EventListener {
        void onItemClick(SkuDetailsModel model, int position);
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }


}
