package com.pb.criconet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
