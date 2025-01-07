package com.pb.criconet.adapter.PavilionAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.adapter.BlockUserAdapter;
import com.pb.criconet.databinding.ItemBlockUserBinding;
import com.pb.criconet.databinding.MynoticeboardChildBinding;
import com.pb.criconet.model.NoticeBoardModel;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.MyViewHolder> {

    Context mContext;
    noticeBoardItemClickListener noticeBoardItemClickListener;
    ArrayList<NoticeBoardModel> noticeBoardModelsList;
    SharedPreferences preferences;

    public NoticeBoardAdapter(Context mContext, ArrayList<NoticeBoardModel> noticeBoardModelsList, noticeBoardItemClickListener noticeBoardItemClickListener) {
        this.mContext = mContext;
        this.noticeBoardModelsList = noticeBoardModelsList;
        this.noticeBoardItemClickListener = noticeBoardItemClickListener;

        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(MynoticeboardChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final NoticeBoardModel noticeBoardModel = noticeBoardModelsList.get(position);
        holder.mynoticeboardChildBinding.eCoaching.setText(noticeBoardModel.getTitle());
        holder.mynoticeboardChildBinding.tvDetails.setText(noticeBoardModel.getDetails());
        holder.mynoticeboardChildBinding.tvpostedByA.setText(noticeBoardModel.getName());
        holder.mynoticeboardChildBinding.hrsAgo.setText(Global.getFrOmDate(noticeBoardModel.getCreated_at()));
        holder.mynoticeboardChildBinding.tvType.setText(noticeBoardModel.getNotic_type());

        if(noticeBoardModel.getAvatar().isEmpty()){
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.placeholder_user)).into(holder.mynoticeboardChildBinding.cImgAvatar);
        }else{
            Glide.with(mContext).load(noticeBoardModel.getAvatar()).error(mContext.getResources().getDrawable(R.drawable.placeholder_user)).into(holder.mynoticeboardChildBinding.cImgAvatar);
        }


        if(noticeBoardModel.getUser_id().equalsIgnoreCase(SessionManager.get_user_id(preferences))){
            holder.mynoticeboardChildBinding.ivDelete.setVisibility(View.VISIBLE);
        }else{
            holder.mynoticeboardChildBinding.ivDelete.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            noticeBoardItemClickListener.noticeItemClickEvent(noticeBoardModel);
        });

        holder.mynoticeboardChildBinding.ivDelete.setOnClickListener(view -> {
            noticeBoardItemClickListener.deleteItem(position,noticeBoardModel.getId());

        });
    }

    @Override
    public int getItemCount() {
        return noticeBoardModelsList.size();
    }

    public void addItem(NoticeBoardModel newItem) {
        noticeBoardModelsList.add(newItem);
        notifyItemInserted(noticeBoardModelsList.size() - 1); // Notify adapter about the new item
    }

    public void deleteItem(int position) {
        noticeBoardModelsList.remove(position); // Remove item from the list
        notifyItemRemoved(position); // Notify adapter about the removed item
    }
    public void updateData(ArrayList<NoticeBoardModel> newList){
        noticeBoardModelsList.clear();
        noticeBoardModelsList.addAll(newList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MynoticeboardChildBinding mynoticeboardChildBinding;

        public MyViewHolder(@NonNull MynoticeboardChildBinding mynoticeboardChildBindingg) {
            super(mynoticeboardChildBindingg.getRoot());
            mynoticeboardChildBinding =mynoticeboardChildBindingg;
        }
    }

    public interface noticeBoardItemClickListener {
        void noticeItemClickEvent(NoticeBoardModel noticeBoardModel);
        void deleteItem(int pos,String id);
    }
}
