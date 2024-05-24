package com.pb.criconetnewdesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.pb.criconetnewdesign.R;
import com.pb.criconetnewdesign.model.DataModelSpecialities;
import java.util.ArrayList;
import java.util.List;

public class SpecialitiesAdapter extends RecyclerView.Adapter<SpecialitiesAdapter.ViewHolder> {

    List<DataModelSpecialities.Datum> mValues;
    Context mContext;
    private ArrayList<DataModelSpecialities.Datum>editList_speclization;
    ArrayList<String>checkedArray;
    protected ItemListenerr mListener;

    public SpecialitiesAdapter(Context context, ArrayList<DataModelSpecialities.Datum>editList_speclizationn, List<DataModelSpecialities.Datum> values, ItemListenerr itemListener) {
        this.editList_speclization=editList_speclizationn;
        this.mValues = values;
        mContext = context;
        mListener=itemListener;
        //Toaster.customToast(editList_speclization.size()+"/"+mValues.size());
        checkedArray = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvButton;

        public ViewHolder(View v) {
            super(v);
            //v.setOnClickListener(this);
            tvButton = (TextView) v.findViewById(R.id.tvButton);
        }

//        public void setData(DataModel.Datum item) {
//            this.items = item;
//            tvButton.setText(items.getTitle());
//
//            for(int j= 0; j<editList_speclization.size();j++){
//                if(editList_speclization.get(j).getTitle().equalsIgnoreCase(item.getTitle())){
//                    item.setCheck(true);
//                    mValues.get(j).setCheck(false);
//                    mListener.onItemClick(mValues);
//                    break;
//                }
//            }
//
//            if(item.isCheck()){
//                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
//                tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
//            }else {
//                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
//                tvButton.setTextColor(mContext.getResources().getColor(R.color.dim_grey));
//            }
//        }
//
//
//        @Override
//        public void onClick(View view) {
//            if (mListener != null) {
//
//                if(mValues.get(getAbsoluteAdapterPosition()).isCheck()) {
//                    mValues.get(getAbsoluteAdapterPosition()).setCheck(false);
//
//                    notifyDataSetChanged();
//                }else {
//                    mValues.get(getAbsoluteAdapterPosition()).setCheck(true);
//                    //mListener.onItemClick(mValues);
//                    notifyDataSetChanged();
//                }
//                mListener.onItemClick(mValues);
//            }
//        }
    }

    @Override
    public SpecialitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.specialities_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        DataModelSpecialities.Datum coachLanguage= mValues.get(position);

        Vholder.tvButton.setText(coachLanguage.getTitle());
        //Toaster.customToast(editList_speclization.size()+"");
        for(int j= 0; j<editList_speclization.size();j++){
            //Toaster.customToast(editList_speclization.get(j).getTitle()+"h");
            if(editList_speclization.get(j).getTitle().equalsIgnoreCase(coachLanguage.getTitle())){
                coachLanguage.setCheck(true);
                checkedArray.add(coachLanguage.getId());
                mListener.onItemClick(checkedArray);
                break;
            }
        }
        if(coachLanguage.isCheck()){
            Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
            Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
            }else {
            Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
            Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.dimGray));
            }

        Vholder.tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModelSpecialities.Datum coachLanguage=mValues.get(position);

                if(coachLanguage.isCheck()==true) {
                    coachLanguage.setCheck(false);

                    Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
                    Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.dimGray));
                    checkedArray.remove(coachLanguage.getId());
                }else{
                    coachLanguage.setCheck(true);
                    Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
                    Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
                    checkedArray.add(coachLanguage.getId());
                }

                mListener.onItemClick(checkedArray);
            }
        });


    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListenerr {
        void onItemClick(List<String> item);
    }
}