package com.pb.criconet.adapter.EcoachingAdapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.databinding.CoachSpecilitiesItemBinding;
import com.pb.criconet.model.Coaching.CoachSpecialitiesModel;
import com.pb.criconet.model.Datum;

import java.util.ArrayList;
import java.util.List;

public class CoachSpecialitiesListAdapter extends RecyclerView.Adapter<CoachSpecialitiesListAdapter.MyViewHolder> {
    private Context mContext;
    List<CoachSpecialitiesModel.Datum> mValues;
    private ArrayList<Datum> editList_speclization;
    protected ItemListener mListener;
    ArrayList<String> checkedArray;

    public CoachSpecialitiesListAdapter(Context context, ArrayList<Datum> editList_speclizationn, List<CoachSpecialitiesModel.Datum> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        editList_speclization = editList_speclizationn;
        mListener = itemListener;

        checkedArray = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(CoachSpecilitiesItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CoachSpecialitiesModel.Datum coachLanguage = mValues.get(position);

        holder.coachSpecilitiesItemBinding.tvName.setText(coachLanguage.getTitle());


        for (int j = 0; j < editList_speclization.size(); j++) {
            if (editList_speclization.get(j).getTitle().equalsIgnoreCase(coachLanguage.getTitle())) {
                coachLanguage.setCheck(true);
                checkedArray.add(coachLanguage.getId());
                mListener.onItemClick(checkedArray);
                break;
            }
        }

        if (coachLanguage.isCheck()) {
            holder.coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
            holder.coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                    mContext.getResources(), R.color.purple_700, null)));
            holder.coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.purple_700));
        } else {
            holder.coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                    mContext.getResources(), R.color.gray, null)));
            holder.coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
            holder.coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.gray));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoachSpecialitiesModel.Datum coachLanguage = mValues.get(position);

                if (coachLanguage.isCheck() == true) {
                    coachLanguage.setCheck(false);

                    holder.coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                            mContext.getResources(), R.color.gray, null)));
                    holder.coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
                    holder.coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.gray));
                    checkedArray.remove(coachLanguage.getId());
                } else {
                    coachLanguage.setCheck(true);
                    holder.coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
                    holder.coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                            mContext.getResources(), R.color.purple_700, null)));
                    holder.coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.purple_700));
                    checkedArray.add(coachLanguage.getId());
                }

                mListener.onItemClick(checkedArray);
            }
        });


        //holder.setData(mValues.get(position));


//        for(int j= 0; j<editList_speclization.size();j++){
//            if(editList_speclization.get(j).getTitle().equalsIgnoreCase(mValues.get(position).getTitle())){
//                datum.setCheck(false);
//                mValues.add(datum);
//                mListener.onItemClick(mValues);
//                break;
//            }
//        }

//        if(datum.isCheck()){
//            holder.coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
//            holder.coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
//                    mContext.getResources(), R.color.purple_700, null)));
//            holder.coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.purple_700));
//        }else {
//            holder.coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
//                    mContext.getResources(), R.color.gray, null)));
//            holder.coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
//            holder.coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.gray));
//        }

//        holder.itemView.setOnClickListener(v -> {
//            if (mListener != null) {
//
//                if (mValues.get(position).isCheck()) {
//                    mValues.get(position).setCheck(false);
//
//                    notifyDataSetChanged();
//                } else {
//                    mValues.get(position).setCheck(true);
//                    notifyDataSetChanged();
//                }
//                mListener.onItemClick(mValues);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CoachSpecilitiesItemBinding coachSpecilitiesItemBinding;
        CoachSpecialitiesModel.Datum items;

        public MyViewHolder(@NonNull CoachSpecilitiesItemBinding binding) {
            super(binding.getRoot());
            coachSpecilitiesItemBinding = binding;

        }

        public void setData(CoachSpecialitiesModel.Datum item) {
            this.items = item;
            coachSpecilitiesItemBinding.tvName.setText(items.getTitle());


            if (item.isCheck()) {
                coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
                coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                        mContext.getResources(), R.color.purple_700, null)));
                coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.purple_700));
            } else {
                coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                        mContext.getResources(), R.color.gray, null)));
                coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
                coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.gray));
            }
        }
    }

    public interface ItemListener {
        void onItemClick(List<String> item);
    }
}
