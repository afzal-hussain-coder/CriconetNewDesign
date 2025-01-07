package com.pb.criconet.adapter.PavilionAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ItemBlockUserBinding;
import com.pb.criconet.databinding.ItemLikeddislikedUserBinding;
import com.pb.criconet.model.pavilionModel.NewUserModel;

import java.util.ArrayList;

public class LikedDislikedPostAdapter extends RecyclerView.Adapter<LikedDislikedPostAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<NewUserModel> likedDisLikedList;
    UnFollowInterface userFlowInterface;

    public LikedDislikedPostAdapter(Context mContext, ArrayList<NewUserModel> likedDisLikedList, UnFollowInterface userFlowInterface) {
        this.mContext = mContext;
        this.likedDisLikedList = likedDisLikedList;
        this.userFlowInterface = userFlowInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemLikeddislikedUserBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NewUserModel newUserModel = likedDisLikedList.get(position);

        if (!newUserModel.getAvatar().isEmpty()) {
            Glide.with(mContext).load(newUserModel.getAvatar()).into(holder.itemBlockUserBinding.ivProfile);
        } else {
            Glide.with(mContext).load(R.drawable.placeholder_user).into(holder.itemBlockUserBinding.ivProfile);
        }

        holder.itemBlockUserBinding.tvName.setText(newUserModel.getName());


        if (likedDisLikedList.get(position).getVerified().equalsIgnoreCase("1")) {
            holder.itemBlockUserBinding.ivVerified.setVisibility(View.VISIBLE);
            if (likedDisLikedList.get(position).getVerified().equalsIgnoreCase("1") &&
                    likedDisLikedList.get(position).getCriconet_verified().equalsIgnoreCase("1")) {
                holder.itemBlockUserBinding.ivVerified.setColorFilter(ContextCompat.getColor(mContext, R.color.purple_200));
            } else {
                holder.itemBlockUserBinding.ivVerified.setColorFilter(ContextCompat.getColor(mContext, R.color.verified_user_color));
            }
        } else {
            holder.itemBlockUserBinding.ivVerified.setVisibility(View.GONE);
            holder.itemBlockUserBinding.ivVerified.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.bckground_light));
        }

        holder.itemBlockUserBinding.flUserBlock.setOnClickListener(v -> {
            userFlowInterface.follow(newUserModel.getUser_id(), position);
        });

        if (newUserModel.getWo_IsFollowing().equalsIgnoreCase("1")) {
            holder.itemBlockUserBinding.tvText.setText(mContext.getResources().getString(R.string.following));
            // Get the drawable resource
            Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_done);

            // Set the drawable to the TextView
            holder.itemBlockUserBinding.tvText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else {
            // Get the drawable resource
            Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.plus);

            // Set the drawable to the TextView
            holder.itemBlockUserBinding.tvText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            holder.itemBlockUserBinding.tvText.setText(mContext.getResources().getString(R.string.follow));
        }


    }

    public void updateItem(int pos, String isLiked) {
        if (pos >= 0 && pos < likedDisLikedList.size()) {
            likedDisLikedList.get(pos).setWo_IsFollowing(isLiked);
            notifyItemChanged(pos);
        }
    }

    @Override
    public int getItemCount() {
        return likedDisLikedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemLikeddislikedUserBinding itemBlockUserBinding;

        public MyViewHolder(@NonNull ItemLikeddislikedUserBinding itemView) {
            super(itemView.getRoot());
            itemBlockUserBinding = itemView;
        }
    }

    public interface UnFollowInterface {
        void follow(String userId, int pos);
    }


}
