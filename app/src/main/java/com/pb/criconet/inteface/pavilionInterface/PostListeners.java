package com.pb.criconet.inteface.pavilionInterface;


import com.pb.criconet.model.pavilionModel.NewPostModel;

public interface PostListeners {

    void onLikeClickListener(NewPostModel post);

    void onDislikeClickListener(NewPostModel post);

    void onCommentClickListener(NewPostModel post);

    void onShareClickListener(NewPostModel post);

    void onProfileClickListener(NewPostModel post);

    void onReportFeedListener(String id,String message);

    void onDeleteFeedListener(String id);

    void onPostDeleteFeedListener(String id,int pos);

    void onEditFeedListener(String id,String text);
}
