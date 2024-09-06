package com.pb.criconet.adapter.PavilionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pb.criconet.R;
import com.pb.criconet.databinding.CommentItemBinding;
import com.pb.criconet.databinding.ReplayCommentItemBinding;
import com.pb.criconet.model.pavilionModel.CommentModel;
import com.pb.criconet.model.pavilionModel.ReplyComments;
import com.pb.criconet.model.pavilionModel.SearchUser;
import com.pb.criconet.util.Global;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.MyViewHolder>{
     private Context mContext;
     private ArrayList<ReplyComments>replyCommentsArrayList;
    replayInterface replayInterface;
    boolean likeUnLikeStatus;

     public ReplyAdapter(Context mContext,ArrayList<ReplyComments>replyCommentsArrayList,replayInterface replayInterface){
         this.mContext = mContext;
         this.replyCommentsArrayList = replyCommentsArrayList;
         this.replayInterface = replayInterface;
     }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(ReplayCommentItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ReplyComments commentModel = replyCommentsArrayList.get(position);

        holder.commentItemBinding.tvName.setText(commentModel.getName());
        holder.commentItemBinding.tvCommented.setText(commentModel.getComment_text());


        if(!commentModel.getAvatar().isEmpty()){

            Glide.with(mContext).load(commentModel.getAvatar())
                    .into(holder.commentItemBinding.ivProfileImage);
        }else{
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_default))
                    .into(holder.commentItemBinding.ivProfileImage);
        }

        holder.commentItemBinding.tvSessionTime.setText(Global.getTimeAgo(Long.parseLong(commentModel.getTime())));





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

        holder.commentItemBinding.ibSettingReply.setOnClickListener(v -> {
          replayInterface.deleteReply(commentModel.getUser_id(),commentModel.getReply_id(),commentModel.getComment_text());
        });

        holder.commentItemBinding.tvLike.setOnClickListener(v -> {
            likeUnLikeStatus = !likeUnLikeStatus;
            replayInterface.likeUnLikeReply(commentModel.getReply_id(),likeUnLikeStatus);
        });
        holder.commentItemBinding.tvLikeCount.setText(commentModel.getComment_likes());

    }

    @Override
    public int getItemCount() {
        return replyCommentsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ReplayCommentItemBinding commentItemBinding;
        public MyViewHolder(@NonNull ReplayCommentItemBinding binding) {
            super(binding.getRoot());
            commentItemBinding = binding;
        }
    }

    public interface searchUserItemClick{
         public void getSearchUserName(String username);
    }

    public interface replayInterface{
         void deleteReply(String userid,String replyId,String text);
         void likeUnLikeReply(String replyId,boolean status);
    }

}
