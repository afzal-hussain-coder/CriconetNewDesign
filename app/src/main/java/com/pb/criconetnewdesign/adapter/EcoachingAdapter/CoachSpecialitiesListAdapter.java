package com.pb.criconetnewdesign.adapter.EcoachingAdapter;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.internal.v;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.databinding.CoachSpecilitiesItemBinding;
import com.pb.criconetnewdesign.model.Coaching.CoachSpecialitiesModel;
import com.pb.criconetnewdesign.util.Toaster;

import java.util.List;

public class CoachSpecialitiesListAdapter extends RecyclerView.Adapter<CoachSpecialitiesListAdapter.MyViewHolder>{
    private Context mContext;
    List<CoachSpecialitiesModel.Datum> mValues;
    protected ItemListener mListener;

    public CoachSpecialitiesListAdapter(Context context,List<CoachSpecialitiesModel.Datum> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(CoachSpecilitiesItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     holder.setData(mValues.get(position));

     holder.itemView.setOnClickListener(v -> {
         if (mListener != null) {

             if(mValues.get(position).isCheck()) {
                 mValues.get(position).setCheck(false);

                 notifyDataSetChanged();
             }else {
                 mValues.get(position).setCheck(true);
                 notifyDataSetChanged();
             }
             mListener.onItemClick(mValues);
         }
     });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        CoachSpecilitiesItemBinding coachSpecilitiesItemBinding;
        CoachSpecialitiesModel.Datum items;

        public MyViewHolder(@NonNull CoachSpecilitiesItemBinding binding) {
            super(binding.getRoot());
            coachSpecilitiesItemBinding = binding;

        }

        public void setData(CoachSpecialitiesModel.Datum item) {
            this.items = item;
            coachSpecilitiesItemBinding.tvName.setText(items.getTitle());
            if(item.isCheck()){
                coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
                coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                        mContext.getResources(), R.color.purple_700, null)));
                coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.purple_700));
            }else {
                coachSpecilitiesItemBinding.tvName.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                        mContext.getResources(), R.color.gray, null)));
                coachSpecilitiesItemBinding.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.chip_bg));
                coachSpecilitiesItemBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.gray));
            }
        }
    }

    public interface ItemListener {
        void onItemClick(List<CoachSpecialitiesModel.Datum> item);
    }
}
