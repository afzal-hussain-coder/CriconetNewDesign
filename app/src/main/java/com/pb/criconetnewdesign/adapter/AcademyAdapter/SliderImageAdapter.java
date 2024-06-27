package com.pb.criconetnewdesign.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.model.AcademyModel.SliderImageData;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderImageAdapter extends SliderViewAdapter<SliderImageAdapter.SliderAdapterViewHolder> {

    private final List<String> mSliderItems;
    private final List<Integer> mSliderItemsInteger;
    private Context mContext;

    public SliderImageAdapter(Context context, ArrayList<String> sliderDataArrayList, List<Integer> mSliderItemsInteger) {
        this.mContext = context;
        this.mSliderItems = sliderDataArrayList;
        this.mSliderItemsInteger = mSliderItemsInteger;
    }


    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_image_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        if(mSliderItems.size()==0){
            final Integer sliderItemm = mSliderItemsInteger.get(position);
            viewHolder.imageViewBackground.setImageResource(sliderItemm);
        }else{
            final String sliderItem = mSliderItems.get(position);
            Glide.with(viewHolder.itemView)
                    .load(sliderItem)
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);
        }


    }


    @Override
    public int getCount() {
        return mSliderItems.size() == 0 ? mSliderItemsInteger.size() : mSliderItems.size();
    }


    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }
}