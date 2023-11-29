package com.pb.criconetnewdesign.adapter.PavilionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.model.pavilionModel.SearchUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyViewHolder>{
     private Context mContext;
     private ArrayList<SearchUser>searchUserArrayList;
     private searchUserItemClick  searchUserItemClick;

     public SearchUserAdapter(Context mContext, ArrayList<SearchUser>searchUserArrayList, searchUserItemClick  searchUserItemClick){
         this.mContext = mContext;
         this.searchUserArrayList = searchUserArrayList;
         this.searchUserItemClick = searchUserItemClick;
     }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.search_user_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SearchUser searchUser = searchUserArrayList.get(position);

        holder.tv_name.setText(searchUser.getName());

        if(!searchUser.getProfile_picture().isEmpty()){

            Glide.with(mContext).load(searchUser.getProfile_picture())
                    .into(holder.iv_profile_image);
        }else{
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_default))
                    .into(holder.iv_profile_image);
        }

        holder.itemView.setOnClickListener(v -> {
            searchUserItemClick.getSearchUserName(searchUser.getName());
        });

    }

    @Override
    public int getItemCount() {
        return searchUserArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView iv_profile_image;
        private TextView tv_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile_image = itemView.findViewById(R.id.iv_profile_image);
            this.tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    public interface searchUserItemClick{
         public void getSearchUserName(String username);
    }
}
