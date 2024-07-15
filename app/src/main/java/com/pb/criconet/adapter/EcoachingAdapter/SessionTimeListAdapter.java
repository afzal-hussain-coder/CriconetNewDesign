package com.pb.criconet.adapter.EcoachingAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.databinding.SessionTimeChildviewBinding;
import com.pb.criconet.model.Coaching.TimeSlot;


import java.util.List;

public class SessionTimeListAdapter extends RecyclerView.Adapter<SessionTimeListAdapter.MyViewHolder> {
    private Context mContext;
    private List<TimeSlot.Datum> data;
    getSlotIdInterface getSlotIdInterface;
    private int selectedItem = -1;


    public SessionTimeListAdapter(Context mContext, List<TimeSlot.Datum> data, getSlotIdInterface getSlotIdInterface) {
        this.mContext = mContext;
        this.data = data;
        this.getSlotIdInterface = getSlotIdInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(SessionTimeChildviewBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.UserBookingListItemBinding.tvSessionTime.setText(data.get(position).getSlotValue());

        holder.UserBookingListItemBinding.tvSessionTime.setOnClickListener(v -> {
            selectedItem = holder.getAdapterPosition();

            getSlotIdInterface.getSlotId(data.get(position).getSlotId());
            holder.UserBookingListItemBinding.tvSessionTime.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.selected_session_time_background));
            holder.UserBookingListItemBinding.tvSessionTime.setTextColor(mContext.getResources().getColor(R.color.white));
            notifyDataSetChanged();

        });

        if (position == selectedItem) {
            holder.UserBookingListItemBinding.tvSessionTime.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.UserBookingListItemBinding.tvSessionTime.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.selected_session_time_background));
        } else {
            holder.UserBookingListItemBinding.tvSessionTime.setTextColor(mContext.getResources().getColor(R.color.indicator_selector));
            holder.UserBookingListItemBinding.tvSessionTime.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.session_time_background));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        SessionTimeChildviewBinding UserBookingListItemBinding;

        public MyViewHolder(@NonNull SessionTimeChildviewBinding binding) {
            super(binding.getRoot());
            UserBookingListItemBinding = binding;



        }
    }

    public interface getSlotIdInterface {
        void getSlotId(String slotId);
    }
}
