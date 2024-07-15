package com.pb.criconet.adapter.Streaming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.model.EmojiModel;
import java.util.ArrayList;

public class LiveMatchEmojiAdapter extends RecyclerView.Adapter<LiveMatchEmojiAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<EmojiModel>emojiList;
    private itemClickInterface itemClickInterface;
    public LiveMatchEmojiAdapter(Context mContext, ArrayList<EmojiModel>emojiList, itemClickInterface itemClickInterface){
      this.mContext = mContext;
      this.emojiList = emojiList;
      this.itemClickInterface = itemClickInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.emoji_child_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(mContext).load(emojiList.get(position).getUrl()).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            itemClickInterface.getClickItem(emojiList.get(position).getUrl(),emojiList.get(position).getName());
        });
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_emoji_six);
        }
    }

    public interface itemClickInterface{
        void getClickItem(String image,String name);
    }
}
