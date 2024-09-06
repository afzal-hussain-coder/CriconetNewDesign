package com.pb.criconet.adapter.EcoachingAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.pb.criconet.R;
import com.pb.criconet.databinding.EcoachingListItemBinding;
import com.pb.criconet.model.Coaching.CoachList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EcoachingListAdapter extends RecyclerView.Adapter<EcoachingListAdapter.MyViewHolder> {
    private Context mContext;
    coachItemClickListener coachItemClickListener;
    private List<CoachList.Datum> mdata;
    List<String> categoriesList;

    public EcoachingListAdapter(Context mContext, List<CoachList.Datum> data, coachItemClickListener coachItemClickListener) {
        this.mContext = mContext;
        this.coachItemClickListener = coachItemClickListener;
        this.mdata = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(EcoachingListItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CoachList.Datum datum  = mdata.get(position);

        holder.ecoachingListItemBinding.tvCoachName.setText(datum.getName());
        holder.ecoachingListItemBinding.yearsExper.setText(datum.getExps());

        if(datum.getAvatar().isEmpty()){
            Glide.with(mContext).load(mContext.getDrawable(R.drawable.placeholder_user)).into(holder.ecoachingListItemBinding.roundedImagePic);
        }else{
            Glide.with(mContext).load(datum.getAvatar()).error(mContext.getDrawable(R.drawable.placeholder_user)).into(holder.ecoachingListItemBinding.roundedImagePic);
        }



        holder.ecoachingListItemBinding.chipSpecializationGroupList.removeAllViews();
        String[] categoriesArray = datum.getCat_title().split(", ");

        categoriesList = new ArrayList<>(Arrays.asList(categoriesArray));

        for (String chipText : categoriesList) {
            Chip chip = new Chip(mContext);
            chip.setText(chipText);
            chip.setChipBackgroundColorResource(R.color.blue_intro_color);
            chip.setChipStrokeColorResource(R.color.indicator_selector);
            chip.setChipStrokeWidth(2.0f);
            chip.setTextAppearance(R.style.MyChipTextAppearanceList);
            holder.ecoachingListItemBinding.chipSpecializationGroupList.addView(chip);
        }

        holder.ecoachingListItemBinding.tvPrice.setText("Price - " + datum.getPrice().getCoachPrice() + " \u20B9");

        holder.ecoachingListItemBinding.flViewDetails.setOnClickListener(v -> coachItemClickListener.viewDetails(position, datum.getUserId()));

        holder.ecoachingListItemBinding.getRoot().setOnClickListener(v -> coachItemClickListener.viewDetails(position, datum.getUserId()));

        holder.ecoachingListItemBinding.flBook.setOnClickListener(v -> {
            coachItemClickListener.bookCoach(position, datum.getUserId());
        });

        if (mdata.get(position).getRating() == 0) {
            holder.ecoachingListItemBinding.ratingBar.setVisibility(View.GONE);
        } else {
            holder.ecoachingListItemBinding.ratingBar.setVisibility(View.VISIBLE);
            holder.ecoachingListItemBinding.ratingBar.setRating((float) datum.getRating());
        }


        if(mdata.get(position).getPrice().getOfferId().equalsIgnoreCase("0")){
            holder.ecoachingListItemBinding.rlOffer.setVisibility(View.GONE);
        }else{
            if(mdata.get(position).getPrice().getOfferPercentage().equalsIgnoreCase("0")){
                //holder.tvoffer.setText(mdata.get(position).getPrice().getOfferPercentage());
                //holder.tvOfferl.setVisibility(View.GONE);
            }else{
                //holder.tvOfferl.setVisibility(View.VISIBLE);
                holder.ecoachingListItemBinding.tvoffer.setText(mdata.get(position).getPrice().getOfferPercentage() +"% "+mContext.getResources().getString(R.string.OFF));
                holder.ecoachingListItemBinding.rlOffer.setVisibility(View.VISIBLE);
            }

        }

        if(mdata.get(position).getPrice().getOfferPercentage().equalsIgnoreCase("0")){
            holder.ecoachingListItemBinding.tvOfferP.setVisibility(View.GONE);
        }else{
            holder.ecoachingListItemBinding.tvOfferP.setVisibility(View.VISIBLE);
            holder.ecoachingListItemBinding.tvPrice.setPaintFlags(holder.ecoachingListItemBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.ecoachingListItemBinding.tvOfferP.setText(mContext.getString(R.string.offer_price)+" "+"\u20B9" +mdata.get(position).getPrice().getPaymentPrice() +" "+"/"+mContext.getString(R.string.session));
        }

        holder.ecoachingListItemBinding.ibShare.setOnClickListener(v -> coachItemClickListener.shareCoach());

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        EcoachingListItemBinding ecoachingListItemBinding;

        public MyViewHolder(@NonNull EcoachingListItemBinding binding) {
            super(binding.getRoot());
            ecoachingListItemBinding = binding;
        }
    }

    public interface coachItemClickListener {
        void viewDetails(int id, String coachId);

        void bookCoach(int id, String coachId);

        void shareCoach();
    }
}
