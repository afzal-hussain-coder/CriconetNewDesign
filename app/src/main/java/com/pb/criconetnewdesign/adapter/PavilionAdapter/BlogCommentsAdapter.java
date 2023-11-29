package com.pb.criconetnewdesign.adapter.PavilionAdapter;

import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.util.Global;

public class BlogCommentsAdapter extends RecyclerView.Adapter<BlogCommentsAdapter.MyViewHolder>{

    Context mContext;
    public BlogCommentsAdapter( Context mContext){
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.blog_comments_child,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvComments;
        TextView tvCommentedName;
        TextView tvLikeCount;
        TextView tvDisLikeCount;
        TextView tvViewerCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvComments = itemView.findViewById(R.id.tvComments);
            this.tvCommentedName = itemView.findViewById(R.id.tvCommentedName);
            this.tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            this.tvDisLikeCount = itemView.findViewById(R.id.tvDisLikeCount);
            this.tvViewerCount = itemView.findViewById(R.id.tvViewerCount);

            Global.addReadMore(tvComments.getText().toString(),tvComments);

        }
    }


}
