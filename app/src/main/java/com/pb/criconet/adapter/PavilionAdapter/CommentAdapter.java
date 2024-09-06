package com.pb.criconet.adapter.PavilionAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.luseen.autolinklibrary.AutoLinkMode;
import com.pb.criconet.Activity.UserDetails;
import com.pb.criconet.R;
import com.pb.criconet.adapter.AcademyAdapter.ManageStudentsAdapter;
import com.pb.criconet.databinding.CommentItemBinding;
import com.pb.criconet.databinding.ManageStudentsChildBinding;
import com.pb.criconet.model.pavilionModel.CommentModel;
import com.pb.criconet.model.pavilionModel.SearchUser;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.Toaster;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    ArrayList<CommentModel> arrayList_image;
    Context mContext;
    String publisherType, pageId;
    itemDeleteInterface itemDeleteInterface;
    boolean likeUnLIkeStatus;

    public CommentAdapter(Context context, String publisherType, String pageId, ArrayList<CommentModel> arrayList_imagee, itemDeleteInterface itemDeleteInterface) {
        arrayList_image = arrayList_imagee;
        mContext = context;
        this.publisherType = publisherType;
        this.pageId = pageId;
        this.itemDeleteInterface = itemDeleteInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(CommentItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CommentModel commentModel = arrayList_image.get(position);

        holder.commentItemBinding.tvName.setText(commentModel.getName());
        holder.commentItemBinding.tvCommented.setText(commentModel.getComment());
        holder.commentItemBinding.tvReplayCount.setText(commentModel.getReplayCommentsList().size() + "");


        if (!commentModel.getAvatar().isEmpty()) {

            Glide.with(mContext).load(commentModel.getAvatar())
                    .into(holder.commentItemBinding.ivProfileImage);
        } else {
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_default))
                    .into(holder.commentItemBinding.ivProfileImage);
        }

        holder.commentItemBinding.tvCommentTime.setText(Global.getTimeAgo(Long.parseLong(commentModel.getTime())));

        if (commentModel.getTagsusers().isEmpty()) {
            holder.commentItemBinding.tvCommented.setVisibility(View.VISIBLE);
            holder.commentItemBinding.postTextAutolink.setVisibility(View.GONE);
            holder.commentItemBinding.tvCommented.setText(commentModel.getComment());
        } else {
            holder.commentItemBinding.tvCommented.setVisibility(View.GONE);
            holder.commentItemBinding.postTextAutolink.setVisibility(View.VISIBLE);
            holder.commentItemBinding.postTextAutolink.addAutoLinkMode(
                    AutoLinkMode.MODE_HASHTAG,
                    AutoLinkMode.MODE_PHONE,
                    AutoLinkMode.MODE_URL,
                    AutoLinkMode.MODE_EMAIL,
                    AutoLinkMode.MODE_CUSTOM,
                    AutoLinkMode.MODE_MENTION);

            holder.commentItemBinding.postTextAutolink.setAutoLinkText(Global.capitalizeFirstLatterOfString(commentModel.getComment()));
            holder.commentItemBinding.postTextAutolink.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.commentItemBinding.postTextAutolink.setPhoneModeColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.commentItemBinding.postTextAutolink.setCustomModeColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.commentItemBinding.postTextAutolink.setMentionModeColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.commentItemBinding.postTextAutolink.setEmailModeColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.commentItemBinding.postTextAutolink.setUrlModeColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.commentItemBinding.postTextAutolink.setSelectedStateColor(ContextCompat.getColor(mContext, R.color.blue));


            holder.commentItemBinding.postTextAutolink.setAutoLinkOnClickListener((autoLinkMode, matchedText) -> {
                String match = matchedText.trim();
                if (autoLinkMode == AutoLinkMode.MODE_URL) {
//                                Intent i = new Intent(context, WebView_Activity.class);
//                                i.putExtra(WebView_Activity.WebURL, matchedText.trim());
//                                context.startActivity(i);
                }
//                            else if (autoLinkMode == AutoLinkMode.MODE_PHONE) {
//
//                                Toaster.customToast(matchedText.trim()+"");
//                                context.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" +"+91-"+ matchedText.trim())));
//                            }
                else if (autoLinkMode == AutoLinkMode.MODE_EMAIL) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{matchedText.trim()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    mContext.startActivity(Intent.createChooser(intent, ""));
                } else {
                    for (int i = 0; i < commentModel.getTagsusers().size(); i++) {
                        Intent intent = new Intent(mContext, UserDetails.class);
                        intent.putExtra("user_id", commentModel.getTagsusers().get(i).getUser_id());
                        intent.putExtra("FROM", "3");
                        mContext.startActivity(intent);
                    }
                }


            });

        }


        holder.commentItemBinding.tvLikeCount.setText(commentModel.getComment_likes());
        holder.commentItemBinding.tvLike.setOnClickListener(v -> {
            likeUnLIkeStatus = !likeUnLIkeStatus;

            itemDeleteInterface.likeComments(commentModel.getId(), likeUnLIkeStatus);
        });

//        holder.itemView.setOnClickListener(v -> {
//            searchUserItemClick.getSearchUserName(searchUser.getName());
//        });

        ReplyAdapter childAdapter;
        childAdapter = new ReplyAdapter(mContext, commentModel.getReplayCommentsList(), new ReplyAdapter.replayInterface() {
            @Override
            public void deleteReply(String userid, String replyId, String text) {
                itemDeleteInterface.deleteReply(userid, replyId, text, commentModel.getName(), commentModel.getId());
            }

            @Override
            public void likeUnLikeReply(String replyId, boolean status) {
                itemDeleteInterface.likeReply(replyId, status);
            }
        });
        holder.commentItemBinding.rvReplaycomment.setLayoutManager(new LinearLayoutManager(mContext));
        holder.commentItemBinding.rvReplaycomment.setAdapter(childAdapter);
        holder.commentItemBinding.rvReplaycomment.setHasFixedSize(true);
        childAdapter.notifyDataSetChanged();

        holder.commentItemBinding.tvReplay.setOnClickListener(v -> {
            itemDeleteInterface.addReplayComment(commentModel.getId(), commentModel.getUser_name(), "Reply");


        });

        holder.commentItemBinding.ibSettingComment.setOnClickListener(v -> {
            itemDeleteInterface.commentDelete(commentModel.getUser_id(),commentModel.getId(), commentModel.getComment_text());
        });

    }

    @Override
    public int getItemCount() {
        return arrayList_image.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CommentItemBinding commentItemBinding;

        public MyViewHolder(@NonNull CommentItemBinding binding) {
            super(binding.getRoot());
            commentItemBinding = binding;
        }
    }

    public interface searchUserItemClick {
        public void getSearchUserName(String username);
    }


    public interface itemDeleteInterface {
        void commentDelete(String userId,String commentId, String commentText);

        void addReplayComment(String userId, String username, String type);

        void deleteReply(String userId, String replyId, String commentText, String Username, String commentId);

        void likeComments(String commentId, boolean status);

        void likeReply(String replyId, boolean status);
    }

}
