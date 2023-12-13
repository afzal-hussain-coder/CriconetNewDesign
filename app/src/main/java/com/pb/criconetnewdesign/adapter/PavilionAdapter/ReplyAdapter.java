package com.pb.criconetnewdesign.adapter.PavilionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.model.pavilionModel.SearchUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.MyViewHolder>{
     private Context mContext;
     private ArrayList<SearchUser>searchUserArrayList;
     private searchUserItemClick  searchUserItemClick;

     public ReplyAdapter(Context mContext){
         this.mContext = mContext;
         this.searchUserArrayList = searchUserArrayList;
         this.searchUserItemClick = searchUserItemClick;
     }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.replay_comment_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        SearchUser searchUser = searchUserArrayList.get(position);
//
//        holder.tv_name.setText(searchUser.getName());
//
//        if(!searchUser.getProfile_picture().isEmpty()){
//
//            Glide.with(mContext).load(searchUser.getProfile_picture())
//                    .into(holder.iv_profile_image);
//        }else{
//            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_default))
//                    .into(holder.iv_profile_image);
//        }
//
//        holder.itemView.setOnClickListener(v -> {
//            searchUserItemClick.getSearchUserName(searchUser.getName());
//        });

//        ChildAdapter childAdapter;
//        childAdapter = new ChildAdapter(parentModelClassArrayList.get(position).childModelClassList, context);
//        ListDecorator listDecorator = new ListDecorator(0,15,15,0);
//        holder.child_rv.addItemDecoration(listDecorator);
//        holder.child_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        holder.child_rv.setAdapter(childAdapter);
//        holder.child_rv.setHasFixedSize(true);
//        childAdapter.notifyDataSetChanged();

        holder.ib_setting_reply.setOnClickListener(v -> {
            BottomSheetDialog();
        });

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_replay;
        private RecyclerView rv_replaycomment;
        private ImageButton ib_setting_reply;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_replay = itemView.findViewById(R.id.tv_replay);
            this.rv_replaycomment = itemView.findViewById(R.id.rv_replaycomment);
            this.ib_setting_reply = itemView.findViewById(R.id.ib_setting_reply);
        }
    }

    public interface searchUserItemClick{
         public void getSearchUserName(String username);
    }

    private void BottomSheetDialog(){
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext,R.style.BottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottom_setting_layout);
        dialog.show();
    }
}
