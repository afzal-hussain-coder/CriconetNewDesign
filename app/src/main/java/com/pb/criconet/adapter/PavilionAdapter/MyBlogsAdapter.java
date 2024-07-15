package com.pb.criconet.adapter.PavilionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.pb.criconet.R;
import com.pb.criconet.databinding.MyblogChildBinding;
import com.pb.criconet.model.Blog.BlogListData;
import java.util.ArrayList;

public class MyBlogsAdapter extends RecyclerView.Adapter<MyBlogsAdapter.MyViewHolder> {

    Context mContext;
    blogItemClickListener itemClickListener;
    ArrayList<BlogListData> blogListDataArrayList;

    public MyBlogsAdapter(Context mContext, ArrayList<BlogListData> blogListDataArrayList, blogItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.itemClickListener = itemClickListener;
        this.blogListDataArrayList = blogListDataArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(MyblogChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.myblogChildBinding.tvTitle.setText(blogListDataArrayList.get(position).getTitle());

        holder.myblogChildBinding.tvDescription.setText(blogListDataArrayList.get(position).getDescription());

        holder.myblogChildBinding.tvAuthorName.setText(mContext.getResources().getString(R.string.By)+" "+ blogListDataArrayList.get(position).getAuthor().getName());

        holder.myblogChildBinding.tvDate.setText(blogListDataArrayList.get(position).getPosted());

        holder.myblogChildBinding.tvViewerCount.setText(blogListDataArrayList.get(position).getView());

        Glide.with(mContext).load(blogListDataArrayList.get(position).getAuthor().getCover()).into(holder.myblogChildBinding.rIVMyBlog);



        ArrayList<String> specializationArray = blogListDataArrayList.get(position).getTagList();
        holder.myblogChildBinding.chipTag.removeAllViews();
        for (String chipText : specializationArray) {
            Chip chip = new Chip(mContext);
            chip.setText(chipText);
            ChipDrawable chipDrawable = (ChipDrawable) chip.getChipDrawable();
            chipDrawable.setChipBackgroundColorResource(R.color.blue_intro_color);
            chip.setChipStrokeColorResource(R.color.white);
            chip.setChipStrokeWidth(2.0f);
            chip.setTextAppearance(R.style.MyChipTextAppearanceWhite);
            holder.myblogChildBinding.chipTag.addView(chip);
        }


        holder.itemView.setOnClickListener(v -> itemClickListener.blogItemClickEvent(blogListDataArrayList.get(position)));

    }

    @Override
    public int getItemCount() {
        return blogListDataArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MyblogChildBinding myblogChildBinding;

        public MyViewHolder(@NonNull MyblogChildBinding binding) {
            super(binding.getRoot());
            myblogChildBinding = binding;
        }
    }

    public interface blogItemClickListener {
         void blogItemClickEvent(BlogListData blogListData);
    }
}
