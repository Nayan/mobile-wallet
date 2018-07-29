package org.mifos.mobilewallet.mifospay.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilewallet.core.data.fineract.entity.accounts.savings.SavingsWithAssociations;
import org.mifos.mobilewallet.mifospay.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ankur on 11/July/2018
 */

public class MerchantsAdapter extends RecyclerView.Adapter<MerchantsAdapter.ViewHolder> {

    private List<SavingsWithAssociations> mMerchantsList;

    @Inject
    public MerchantsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merchant,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SavingsWithAssociations mMerchant = mMerchantsList.get(position);
        holder.mTvMerchantName.setText(mMerchant.getClientName());
        holder.mTvMerchantExternalId.setText(mMerchant.getExternalId());
    }

    @Override
    public int getItemCount() {
        if (mMerchantsList != null) {
            return mMerchantsList.size();
        } else {
            return 0;
        }
    }

    public void setData(List<SavingsWithAssociations> mMerchantsList) {
        this.mMerchantsList = mMerchantsList;
        notifyDataSetChanged();
    }

    public List<SavingsWithAssociations> getMerchantsList() {
        return mMerchantsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_merchant_name)
        TextView mTvMerchantName;
        @BindView(R.id.tv_merchant_external_id)
        TextView mTvMerchantExternalId;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}