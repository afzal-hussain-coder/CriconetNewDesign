package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.model.AcademyModel.ManageCoachesModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScheduleAcademyCoachesListAdapter extends RecyclerView.Adapter<ScheduleAcademyCoachesListAdapter.MyViewHolder> {

    private Context context;
    private List<ManageCoachesModel> data;
    private clickCallback callback;
    private ArrayList<String>integerArrayList;
    private ArrayList<String>selectedCoachNameList;

    public ScheduleAcademyCoachesListAdapter(Context context, List<ManageCoachesModel> data, clickCallback callback) {
        this.context = context;
        this.data = data;
        this.callback = callback;
        integerArrayList = new ArrayList<>();
        selectedCoachNameList = new ArrayList<>();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_academy_schedule_coach, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        ManageCoachesModel manageCoachesModel = data.get(position);
        Glide.with(context).load(manageCoachesModel.getAvatar()).dontAnimate().placeholder(context.getResources().getDrawable(R.drawable.placeholder_user))
                .into(holder.iv_students);


        if (manageCoachesModel.getVerified().equalsIgnoreCase("1")) {
            holder.iv_verified.setVisibility(View.VISIBLE);
            if(manageCoachesModel.getVerified().equalsIgnoreCase("1") &&
                    manageCoachesModel.getCriconet_verified().equalsIgnoreCase("1")){
                holder.iv_verified.setColorFilter(ContextCompat.getColor(context, R.color.purple_200));
            }else{
                holder.iv_verified.setColorFilter(ContextCompat.getColor(context, R.color.verified_user_color));
            }
        } else {
            holder.iv_verified.setVisibility(View.GONE);
            holder.iv_verified.setImageTintList(ContextCompat.getColorStateList(context, R.color.bckground_light));
        }

        holder.tv_name.setText(manageCoachesModel.getName());

        holder.cb_fourty_five.setOnClickListener(v -> {
            CheckBox myCheckBox = (CheckBox) v;
            ManageCoachesModel coachLanguage1 = data.get(position);

            if (myCheckBox.isChecked()) {
                coachLanguage1.setCheck(true);
                integerArrayList.add(coachLanguage1.getCoach_id());
                selectedCoachNameList.add(coachLanguage1.getName());
            } else if (!myCheckBox.isChecked()) {
                coachLanguage1.setCheck(false);
                integerArrayList.remove(coachLanguage1.getCoach_id());
                selectedCoachNameList.remove(coachLanguage1.getName());
            }
            callback.getCoachId(integerArrayList,selectedCoachNameList);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        CircleImageView iv_students;
        CheckBox cb_fourty_five;
        ImageView iv_verified;

        MyViewHolder(View itemView) {
            super(itemView);
            iv_students = itemView.findViewById(R.id.iv_students);
            tv_name = itemView.findViewById(R.id.tv_name);
            cb_fourty_five = itemView.findViewById(R.id.cb_fourty_five);
            iv_verified = itemView.findViewById(R.id.iv_verified);
        }
    }

    public interface clickCallback {
       void getCoachId(ArrayList<String>integerArrayList,ArrayList<String>coachNameArrayList);
    }


}