package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.databinding.AcademyListChildBinding;
import com.pb.criconet.model.AcademyModel.AcademyListModel;

import java.util.ArrayList;

public class AcademyListAdapter extends RecyclerView.Adapter<AcademyListAdapter.MyViewHolder> {

    Context mContext;
    academyItemClickListener itemClickListener;
    private ArrayList<AcademyListModel> academyListModels;

    public AcademyListAdapter(Context mContext, ArrayList<AcademyListModel> academyListModels, academyItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.academyListModels = academyListModels;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AcademyListChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AcademyListModel academyListModel = academyListModels.get(position);


        if (!academyListModel.getBanner_image().isEmpty()) {
            Glide.with(mContext).load(academyListModel.getBanner_image()).into(holder.academyListChildBinding.rIVMyBlog);
        } else {
            Glide.with(mContext).load(R.drawable.academy_bg_avatar)
                    .into(holder.academyListChildBinding.rIVMyBlog);
        }

        holder.academyListChildBinding.tvName.setText(academyListModel.getName());
        holder.academyListChildBinding.tvSpecializatio.setText(academyListModel.getCat_title());
        holder.academyListChildBinding.tvAddress.setText(academyListModel.getAddress());

        holder.academyListChildBinding.flContact.setOnClickListener(v -> {
            itemClickListener.academyContactClick(academyListModel.getContact_person_phone());
        });

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.academyItemClickEvent(academyListModels.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return academyListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AcademyListChildBinding academyListChildBinding;

        public MyViewHolder(@NonNull AcademyListChildBinding binding) {
            super(binding.getRoot());
            academyListChildBinding = binding;
        }
    }

    public interface academyItemClickListener {
        void academyItemClickEvent(AcademyListModel academyListModel);
        void academyContactClick(String number);
    }
}
