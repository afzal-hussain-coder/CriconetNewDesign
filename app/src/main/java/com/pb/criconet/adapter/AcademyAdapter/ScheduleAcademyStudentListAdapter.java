package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.model.AcademyModel.AcademyStudenModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScheduleAcademyStudentListAdapter extends RecyclerView.Adapter<ScheduleAcademyStudentListAdapter.MyViewHolder> {

    private Context context;
    private List<AcademyStudenModel> data;
    private clickCallback callback;
    private ArrayList<String> integerArrayList;
    private ArrayList<String> studentsNameList;

    public ScheduleAcademyStudentListAdapter(Context context, List<AcademyStudenModel> data, clickCallback callback) {
        this.context = context;
        this.data = data;
        this.callback = callback;
        integerArrayList = new ArrayList<>();
        studentsNameList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_row_academy_student, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        AcademyStudenModel manageCoachesModel = data.get(position);
        Glide.with(context).load(manageCoachesModel.getAvatar()).dontAnimate().placeholder(context.getResources().getDrawable(R.drawable.placeholder_user))
                .into(holder.iv_students);
        holder.tv_name.setText(manageCoachesModel.getName());


        holder.cb_fourty_five.setOnClickListener(v -> {
            CheckBox myCheckBox = (CheckBox) v;
            AcademyStudenModel coachLanguage1 = data.get(position);

            if (myCheckBox.isChecked()) {
                coachLanguage1.setChecked(true);
                integerArrayList.add(coachLanguage1.getStudent_id());
                studentsNameList.add(coachLanguage1.getName());
            } else if (!myCheckBox.isChecked()) {
                coachLanguage1.setChecked(false);
                integerArrayList.remove(coachLanguage1.getStudent_id());
                studentsNameList.remove(coachLanguage1.getName());
            }
            callback.getStudentsId(integerArrayList,studentsNameList);
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

        MyViewHolder(View itemView) {
            super(itemView);
            iv_students = itemView.findViewById(R.id.iv_students);
            tv_name = itemView.findViewById(R.id.tv_name);
            cb_fourty_five = itemView.findViewById(R.id.cb_fourty_five);
        }
    }

    public interface clickCallback {
        void getStudentsId(ArrayList<String> integerArrayList, ArrayList<String> studentsNameList);
    }


}