package com.pb.criconetnewdesign.adapter.EcoachingAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pb.criconetnewdesign.databinding.EcoachingListItemBinding;

public class EcoachingListAdapter extends RecyclerView.Adapter<EcoachingListAdapter.MyViewHolder>{
    private Context mContext;
    coachItemClickListener coachItemClickListener;

    public EcoachingListAdapter(Context mContext,coachItemClickListener coachItemClickListener){
        this.mContext=mContext;
        this.coachItemClickListener = coachItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(EcoachingListItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.ecoachingListItemBinding.flViewDetails.setOnClickListener(v -> coachItemClickListener.viewDetails(position));

        holder.ecoachingListItemBinding.getRoot().setOnClickListener(v -> coachItemClickListener.viewDetails(position));

        holder.ecoachingListItemBinding.ibShare.setOnClickListener(v -> coachItemClickListener.shareCoach());

    }

    @Override
    public int getItemCount() {
        return 10;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        EcoachingListItemBinding ecoachingListItemBinding;

        public MyViewHolder(@NonNull EcoachingListItemBinding binding) {
            super(binding.getRoot());
            ecoachingListItemBinding = binding;
        }
    }

    public interface coachItemClickListener{
        void viewDetails(int id);
        void bookCoach(int id);
        void shareCoach();
    }
}
