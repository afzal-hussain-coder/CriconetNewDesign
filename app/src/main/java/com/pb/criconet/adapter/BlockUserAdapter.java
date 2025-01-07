package com.pb.criconet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.databinding.ItemBlockUserBinding;
import com.pb.criconet.model.pavilionModel.NewUserModel;

import java.util.ArrayList;

public class BlockUserAdapter extends RecyclerView.Adapter<BlockUserAdapter.MyViewHolder>{

    Context mContext;
    ArrayList<NewUserModel> blockUserList;
    UnBlockInterface unBlockInterface;

    public  BlockUserAdapter(Context mContext,ArrayList<NewUserModel> blockUserList,UnBlockInterface unBlockInterface){
        this.mContext = mContext;
        this.blockUserList = blockUserList;
        this.unBlockInterface = unBlockInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemBlockUserBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NewUserModel newUserModel = blockUserList.get(position);

        if(!newUserModel.getAvatar().isEmpty()){
            Glide.with(mContext).load(newUserModel.getAvatar()).into(holder.itemBlockUserBinding.ivProfile);
        }else{
            Glide.with(mContext).load(R.drawable.placeholder_user).into(holder.itemBlockUserBinding.ivProfile);
        }

        holder.itemBlockUserBinding.tvName.setText(newUserModel.getName());

        if (blockUserList.get(position).getVerified().equalsIgnoreCase("1")) {
            holder.itemBlockUserBinding.ivVerified.setVisibility(View.VISIBLE);
            if(blockUserList.get(position).getVerified().equalsIgnoreCase("1") &&
                    blockUserList.get(position).getCriconet_verified().equalsIgnoreCase("1")){
                holder.itemBlockUserBinding.ivVerified.setColorFilter(ContextCompat.getColor(mContext, R.color.purple_200));
            }else{
                holder.itemBlockUserBinding.ivVerified.setColorFilter(ContextCompat.getColor(mContext, R.color.verified_user_color));
            }
        } else {
            holder.itemBlockUserBinding.ivVerified.setVisibility(View.GONE);
            holder.itemBlockUserBinding.ivVerified.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.bckground_light));
        }

        holder.itemBlockUserBinding.flUserBlock.setOnClickListener(v -> {
            unBlockInterface.unBlockUser(newUserModel.getUser_id(),position);
        });

    }

    public void removeItem(int pos) {
        blockUserList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, blockUserList.size());
    }

    @Override
    public int getItemCount() {
        return blockUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ItemBlockUserBinding itemBlockUserBinding;

        public MyViewHolder(@NonNull ItemBlockUserBinding itemView) {
            super(itemView.getRoot());
            itemBlockUserBinding =itemView;
        }
    }

    public interface UnBlockInterface{
        void unBlockUser(String userId,int pos);
    }




}
