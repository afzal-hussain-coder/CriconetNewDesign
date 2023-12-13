package com.pb.criconetnewdesign.adapter.EcoachingAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconetnewdesign.databinding.UserBookingListItemBinding;

public class UserBookingListAdapter extends RecyclerView.Adapter<UserBookingListAdapter.MyViewHolder>{
    private Context mContext;
    coachItemClickListener coachItemClickListener;
    

    public UserBookingListAdapter(Context mContext, coachItemClickListener coachItemClickListener){
        this.mContext=mContext;
        this.coachItemClickListener = coachItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(UserBookingListItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.itemView.setOnClickListener(v -> {
            coachItemClickListener.viewDetails(position);
        });
       // holder.UserBookingListItemBinding.flViewDetails.setOnClickListener(v -> coachItemClickListener.viewDetails(position));
//
//        holder.UserBookingListItemBinding.getRoot().setOnClickListener(v -> coachItemClickListener.viewDetails(position));
//
//        holder.UserBookingListItemBinding.ibShare.setOnClickListener(v -> coachItemClickListener.shareCoach());

    }

    @Override
    public int getItemCount() {
        return 10;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        UserBookingListItemBinding UserBookingListItemBinding;

        public MyViewHolder(@NonNull UserBookingListItemBinding binding) {
            super(binding.getRoot());
            UserBookingListItemBinding = binding;
            UserBookingListItemBinding.tvViewDetails.setPaintFlags(UserBookingListItemBinding.tvViewDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public interface coachItemClickListener{
        void viewDetails(int id);
        void bookCoach(int id);
        void shareCoach();
    }
}
