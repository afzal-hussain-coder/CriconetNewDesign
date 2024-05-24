package com.pb.criconetnewdesign.adapter.EcoachingAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconetnewdesign.databinding.SessionTimeChildviewBinding;
import com.pb.criconetnewdesign.databinding.UserBookingListItemBinding;

public class SessionTimeListAdapter extends RecyclerView.Adapter<SessionTimeListAdapter.MyViewHolder>{
    private Context mContext;
    coachItemClickListener coachItemClickListener;


    public SessionTimeListAdapter(Context mContext){
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(SessionTimeChildviewBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        holder.itemView.setOnClickListener(v -> {
//            coachItemClickListener.viewDetails(position);
//        });
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

        SessionTimeChildviewBinding UserBookingListItemBinding;

        public MyViewHolder(@NonNull SessionTimeChildviewBinding binding) {
            super(binding.getRoot());
            UserBookingListItemBinding = binding;

        }
    }

    public interface coachItemClickListener{
        void viewDetails(int id);
        void bookCoach(int id);
        void shareCoach();
    }
}
