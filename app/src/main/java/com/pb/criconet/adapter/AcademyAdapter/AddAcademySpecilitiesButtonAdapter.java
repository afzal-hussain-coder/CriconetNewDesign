package com.pb.criconet.adapter.AcademyAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.model.DataModel;
import com.pb.criconet.model.Datum;

import java.util.ArrayList;
import java.util.List;

public class AddAcademySpecilitiesButtonAdapter extends RecyclerView.Adapter<AddAcademySpecilitiesButtonAdapter.ViewHolder> {

    List<DataModel.Datum> mValues;
    Context mContext;
    private ArrayList<Datum> editList_speclization;
    protected ItemListener mListener;
    ArrayList<String>checkedArray;

    public AddAcademySpecilitiesButtonAdapter(Context context, ArrayList<Datum> editList_speclizationn, List<DataModel.Datum> values, ItemListener itemListener) {
        editList_speclization=editList_speclizationn;
        mValues = values;
        mContext = context;
        mListener=itemListener;
        checkedArray = new ArrayList<>();

        //Toaster.customToast(editList_speclization.size()+"");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvButton;
        DataModel.Datum items;

        public ViewHolder(View v) {
            super(v);
            tvButton = (TextView) v.findViewById(R.id.tvButton);
        }

//        public void setData(DataModel.Datum item) {
//            this.items = item;
//            tvButton.setText(items.getTitle());
//            for(int j= 0; j<editList_speclization.size();j++){
//                if(editList_speclization.get(j).getTitle().equalsIgnoreCase(item.getTitle())){
//                    coachLanguage.setCheck(true);
//                    checkedArray.add(coachLanguage.getId());
//                    mListener.onItemClick(checkedArray);
//                    break;
//                }
//            }
//            if(item.isCheck()){
//                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_appgreen));
//                tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
//            }else {
//                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_add_academy_specilities));
//                tvButton.setTextColor(mContext.getResources().getColor(R.color.light_grey));
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_add_academy_specilities, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {
        DataModel.Datum datum = mValues.get(position);
        Vholder.tvButton.setText(mValues.get(position).getTitle());
        //Vholder.tvButton.setChecked(coachLanguage.isSelected());


        for(int j= 0; j<editList_speclization.size();j++){
            if(editList_speclization.get(j).getTitle().equalsIgnoreCase(datum.getTitle())){
                datum.setCheck(true);
                checkedArray.add(datum.getId());
                mListener.onItemClick(mValues);
                break;
            }
        }
        if(datum.isCheck()){
            Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
            Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
            Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.dim_grey));
        }

        Vholder.tvButton.setOnClickListener(view -> {
            DataModel.Datum coachLanguage=mValues.get(position);

            if(coachLanguage.isCheck()==true) {
                coachLanguage.setCheck(false);

                Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
                Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.dim_grey));
                checkedArray.remove(coachLanguage.getId());
            }else{
                coachLanguage.setCheck(true);
                Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
                Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
                checkedArray.add(coachLanguage.getId());
            }

            mListener.onItemClick(mValues);
        });

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(List<DataModel.Datum> item);
    }
}
