package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.databinding.ManageStudentsChildBinding;
import com.pb.criconet.model.AcademyModel.AcademyStudenModel;
import com.pb.criconet.model.AmbassadorModel;
import com.pb.criconet.util.Global;

import java.util.ArrayList;

public class ManageStudentsAdapter extends RecyclerView.Adapter<ManageStudentsAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<AcademyStudenModel> studenModelArrayList;
    private onItemClick onItemClick;


    public ManageStudentsAdapter(Context mContext, ArrayList<AcademyStudenModel> studenModelArrayList, onItemClick onItemClick) {
        this.mContext = mContext;
        this.studenModelArrayList = studenModelArrayList;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ManageStudentsChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AcademyStudenModel academyStudenModel = studenModelArrayList.get(position);
        holder.manageStudentsChildBinding.tvName.setText(academyStudenModel.getName());
        holder.manageStudentsChildBinding.tvSkills.setText(academyStudenModel.getPlayer_type());
        Glide.with(mContext).load(academyStudenModel.getAvatar()).into(holder.manageStudentsChildBinding.ivAvtar);

        if(academyStudenModel.getPhone_number().isEmpty()){
            holder.manageStudentsChildBinding.liPhone.setVisibility(View.GONE);
        }else{
            holder.manageStudentsChildBinding.liPhone.setVisibility(View.VISIBLE);
            holder.manageStudentsChildBinding.tvPhone.setText(academyStudenModel.getPhone_number());
        }


        holder.manageStudentsChildBinding.tvEmail.setText(academyStudenModel.getEmail());


        holder.manageStudentsChildBinding.tvPhone.setOnClickListener(v -> {
            Intent phone_intent = new Intent(Intent.ACTION_CALL);
            phone_intent.setData(Uri.parse("tel:" + academyStudenModel.getPhone_number()));
            mContext.startActivity(phone_intent);
        });
        holder.manageStudentsChildBinding.flKnowMore.setOnClickListener(v -> {
            onItemClick.UpdateStudents(academyStudenModel);
        });
    }

    @Override
    public int getItemCount() {
        return studenModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ManageStudentsChildBinding manageStudentsChildBinding;

        public MyViewHolder(@NonNull ManageStudentsChildBinding binding) {
            super(binding.getRoot());
            manageStudentsChildBinding = binding;

        }
    }

    public interface onItemClick {
        void UpdateStudents(AcademyStudenModel academyStudenModel);
    }

}
