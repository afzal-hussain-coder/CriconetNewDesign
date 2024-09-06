package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.R;
import com.pb.criconet.model.AcademyModel.AcademyGallery;
import com.pb.criconet.util.Toaster;

import java.util.ArrayList;

public class AcademyGalleryMultipleImageAdapter extends RecyclerView.Adapter<AcademyGalleryMultipleImageAdapter.ViewHolder> {

    ArrayList<AcademyGallery> mArrayUri;
    Context mContext;
    protected ItemListener mListener;

    public AcademyGalleryMultipleImageAdapter(Context context, ArrayList<AcademyGallery> mArrayUri, ItemListener mmListener) {
        mContext = context;
        mListener = mmListener;
        this.mArrayUri = mArrayUri;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RoundedImageView ivGallery;
        ImageView ivClose;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ivGallery = v.findViewById(R.id.ivGallery);
            ivClose =  v.findViewById(R.id.ivClose);
        }


        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.gv_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {

        AcademyGallery galleryImageModel = mArrayUri.get(position);


        if (galleryImageModel.getFiles()!=null) {
            Glide.with(mContext).load(galleryImageModel.getFiles())
                    .into(Vholder.ivGallery);

        } else {
            Glide.with(mContext).load(R.drawable.bg_image)
                    .into(Vholder.ivGallery);


        }

        Vholder.ivClose.setOnClickListener(view -> {
            int newPosition = Vholder.getAdapterPosition();
            Log.d("Position",newPosition+"");
            mArrayUri.remove(newPosition);
            notifyItemRemoved(newPosition);
            notifyItemRangeChanged(newPosition, mArrayUri.size());
            mListener.onItemClickk(newPosition,galleryImageModel.getId());
        });

    }


    @Override
    public int getItemCount() {
        return mArrayUri.size();
    }

    public interface ItemListener {
        void onItemClickk(int size,String _galeryId);
    }
}