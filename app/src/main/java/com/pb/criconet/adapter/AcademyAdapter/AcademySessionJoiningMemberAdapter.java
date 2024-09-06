package com.pb.criconet.adapter.AcademyAdapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.Activity.Coach.EndSessionFeedbackFormActivity;
import com.pb.criconet.R;
import com.pb.criconet.databinding.AcademySessionJoiningMemberItemBinding;
import com.pb.criconet.model.AcademyModel.AcademySessionCoach;
import com.pb.criconet.model.AcademyModel.AcademySessionJoiningMemberCommon;

import java.util.ArrayList;
import java.util.Objects;

public class AcademySessionJoiningMemberAdapter extends RecyclerView.Adapter<AcademySessionJoiningMemberAdapter.MyViewHolder>{
    Context mContext;
    private ArrayList<AcademySessionJoiningMemberCommon>academySessionCoaches;

    public AcademySessionJoiningMemberAdapter(Context mContext,ArrayList<AcademySessionJoiningMemberCommon>academySessionCoaches){
        this.mContext = mContext;
        this.academySessionCoaches = academySessionCoaches;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AcademySessionJoiningMemberItemBinding.inflate(LayoutInflater.from(mContext),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AcademySessionJoiningMemberCommon  academySessionJoiningMemberCommon = academySessionCoaches.get(position);

        if(academySessionJoiningMemberCommon.getAvatar().isEmpty()){
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_default))
                    .error(mContext.getResources().getDrawable(R.drawable.user_default))
                    .into(holder.academySessionJoiningMemberItemBinding.ivStudents);
        }else{
            Glide.with(mContext).load(academySessionJoiningMemberCommon.getAvatar()).placeholder(mContext.getResources().getDrawable(R.drawable.user_default)).into(holder.academySessionJoiningMemberItemBinding.ivStudents);
        }

        holder.academySessionJoiningMemberItemBinding.ivStudents.setOnClickListener(v -> showZoomDialog(academySessionJoiningMemberCommon.getAvatar(),academySessionJoiningMemberCommon.getName()));


    }

    @Override
    public int getItemCount() {
        return academySessionCoaches.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        AcademySessionJoiningMemberItemBinding academySessionJoiningMemberItemBinding;

        public MyViewHolder(@NonNull AcademySessionJoiningMemberItemBinding academySessionJoiningMemberItemBinding) {
            super(academySessionJoiningMemberItemBinding.getRoot());
            this.academySessionJoiningMemberItemBinding = academySessionJoiningMemberItemBinding;
        }


    }

    private void showZoomDialog(String imageUrl,String name) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_zoom_image_session);

        RoundedImageView ivZoomedImage = dialog.findViewById(R.id.iv_zoomed_image);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        TextView tvName = dialog.findViewById(R.id.tvName);
        tvName.setText(name);

        Glide.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_user)
                .into(ivZoomedImage);

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

}
