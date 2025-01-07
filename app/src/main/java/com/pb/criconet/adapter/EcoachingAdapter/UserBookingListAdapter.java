package com.pb.criconet.adapter.EcoachingAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.pb.criconet.R;
import com.pb.criconet.databinding.UserBookingListItemBinding;
import com.pb.criconet.model.Coaching.BookingHistory;
import com.pb.criconet.util.Global;

import java.util.List;

public class UserBookingListAdapter extends RecyclerView.Adapter<UserBookingListAdapter.MyViewHolder> {
    private Context mContext;
    coachItemClickListener coachItemClickListener;
    private List<BookingHistory.Datum> data;


    public UserBookingListAdapter(Context mContext, List<BookingHistory.Datum> data, coachItemClickListener coachItemClickListener) {
        this.mContext = mContext;
        this.data = data;
        this.coachItemClickListener = coachItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(UserBookingListItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // Define the radius for rounded corners
        int cornerRadius = 20; // Adjust this value as needed

// Apply the transformation when loading the image
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .transform(new RoundedCorners(cornerRadius));

        Glide.with(mContext).load(data.get(position).getAvatar()).error(mContext.getResources().getDrawable(R.drawable.image_placeholder)).placeholder(mContext.getResources().getDrawable(R.drawable.image_placeholder)).into(holder.UserBookingListItemBinding.ivProfile);
        //.apply(requestOptions)


        holder.itemView.setOnClickListener(v -> {
            coachItemClickListener.viewDetails(data.get(position).getId());
        });

        holder.UserBookingListItemBinding.flSendFeedback.setOnClickListener(v -> {
            coachItemClickListener.sendFeedback(position);
        });

        holder.UserBookingListItemBinding.tvBookingDatee.setText(data.get(position).getBooking_date());

        holder.UserBookingListItemBinding.tvBookingDate.setText(
                Global.convertUTCDateToLocalDate(data.get(position).getOnlineSessionStartTime()) + " , " + data.get(position).getBookingSlotTxt());

        holder.UserBookingListItemBinding.tvBookingDuration.setText(mContext.getResources().getString(R.string.slot_duration) + " " + data.get(position).getSlotDuration()
                + mContext.getString(R.string.minute));
        holder.UserBookingListItemBinding.tvName.setText(data.get(position).getName());


        if (data.get(position).getBtn1().equalsIgnoreCase("Confirmed")) {
            holder.UserBookingListItemBinding.tvBookingStatus.setVisibility(View.VISIBLE);
            holder.UserBookingListItemBinding.tvBookingStatusCancelled.setVisibility(View.GONE);
        } else if (data.get(position).getBtn1().equalsIgnoreCase("Cancelled")) {
            holder.UserBookingListItemBinding.tvBookingStatus.setVisibility(View.GONE);
            holder.UserBookingListItemBinding.tvBookingStatusCancelled.setVisibility(View.VISIBLE);
        } else {
            holder.UserBookingListItemBinding.tvBookingStatus.setVisibility(View.GONE);
            holder.UserBookingListItemBinding.tvBookingStatusCancelled.setVisibility(View.GONE);
        }

        if (data.get(position).getBtn2() != null && !data.get(position).getBtn2().equalsIgnoreCase("")) {

            try {
                if (data.get(position).getChanel_id().equalsIgnoreCase("")) {
                    holder.UserBookingListItemBinding.flJoinSession.setVisibility(View.GONE);
                    holder.UserBookingListItemBinding.viewLine.setVisibility(View.GONE);
                } else {
                    holder.UserBookingListItemBinding.viewLine.setVisibility(View.VISIBLE);
                    holder.UserBookingListItemBinding.flJoinSession.setVisibility(View.VISIBLE);
                    holder.UserBookingListItemBinding.tvJoin.setText(data.get(position).getBtn2() + " " + mContext.getResources().getString(R.string.session));
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        } else {
            holder.UserBookingListItemBinding.flJoinSession.setVisibility(View.GONE);
        }


        holder.UserBookingListItemBinding.flJoinSession.setOnClickListener(v -> {
            coachItemClickListener.buttonJoin(data.get(position).getId(), data.get(position).getDuration_in_milisecond(), data.get(position).getBtn2(), data.get(position).getChanel_id(), data.get(position).getBookingId(),
                    data.get(position).getUserId(), data.get(position).getCoachUserId(), data.get(position).getName());
        });


        // holder.UserBookingListItemBinding.flViewDetails.setOnClickListener(v -> coatv_booking_statuschItemClickListener.viewDetails(position));
//
//        holder.UserBookingListItemBinding.getRoot().setOnClickListener(v -> coachItemClickListener.viewDetails(position));
//
//        holder.UserBookingListItemBinding.ibShare.setOnClickListener(v -> coachItemClickListener.shareCoach());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        UserBookingListItemBinding UserBookingListItemBinding;

        public MyViewHolder(@NonNull UserBookingListItemBinding binding) {
            super(binding.getRoot());
            UserBookingListItemBinding = binding;
            UserBookingListItemBinding.tvViewDetails.setPaintFlags(UserBookingListItemBinding.tvViewDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public interface coachItemClickListener {
        void viewDetails(String bookingId);

        void bookCoach(int id);

        void shareCoach();

        void sendFeedback(int id);

        void buttonJoin(String id, long timeDuration, String action, String channel_id, String booking_id, String userid, String coachid, String coachName);
    }
}
