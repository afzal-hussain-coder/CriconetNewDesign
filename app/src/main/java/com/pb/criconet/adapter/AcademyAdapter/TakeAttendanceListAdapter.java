package com.pb.criconet.adapter.AcademyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.databinding.TakeAttendanceChildBinding;
import com.pb.criconet.model.AcademyModel.AcademyStudenModel;
import com.pb.criconet.model.AcademyModel.AttendanceReport;
import com.pb.criconet.util.DataModel;
import com.pb.criconet.util.DropDownBlue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TakeAttendanceListAdapter extends RecyclerView.Adapter<TakeAttendanceListAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<DataModel> option_attendance_status = new ArrayList<>();

    private List<AcademyStudenModel> data;
    private clickCallback callback;
    JSONArray jsonArrayPresent = null;
    JSONArray jsonArrayAbsent = null;
    JSONObject jsonObjectPresnet = null;
    JSONObject jsonObjectAbsent = null;
    ArrayList<AttendanceReport> studentListreport = null;


    public TakeAttendanceListAdapter(Context context, List<AcademyStudenModel> data, ArrayList<AttendanceReport> studentListreport, clickCallback callback){
        this.mContext = context;
        this.data = data;
        this.studentListreport = studentListreport;
        this.callback = callback;
        // jsonArrayAbsent = new JSONArray();
        jsonArrayPresent = new JSONArray();

        option_attendance_status.add(new DataModel(mContext.getResources().getString(R.string.present)));
        option_attendance_status.add(new DataModel(mContext.getResources().getString(R.string.absent)));

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(TakeAttendanceChildBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        jsonObjectAbsent = new JSONObject();

        AcademyStudenModel manageCoachesModel = data.get(position);

     holder.takeAttendanceChildBinding.tvStudentName.setText(manageCoachesModel.getName());


        for (int j = 0; j < studentListreport.size(); j++) {
            if (studentListreport.get(j).getUser_id().equalsIgnoreCase(manageCoachesModel.getUser_id())) {

//                if (studentListreport.get(j).getAttendance_status().equalsIgnoreCase("A")) {
//                    manageCoachesModel.setChecked(false);
////                    try {
////
////                        jsonObjectPresnet.put("attendance_status", "A");
////                        jsonObjectPresnet.put("user_id", manageCoachesModel.getUser_id());
////                        jsonArrayPresent.put(jsonObjectPresnet);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////
////                    callback.parsent(jsonArrayPresent);
//                }

                if (studentListreport.get(j).getAttendance_status().equalsIgnoreCase("P")) {
                    manageCoachesModel.setChecked(true);
                    try {
                        jsonObjectPresnet = new JSONObject();
                        jsonObjectPresnet.put("attendance_status", "P");
                        jsonObjectPresnet.put("user_id", manageCoachesModel.getUser_id());
                        jsonArrayPresent.put(jsonObjectPresnet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    manageCoachesModel.setChecked(false);
                }

                callback.parsent(jsonArrayPresent);

                break;
            }
        }


        if (manageCoachesModel.isChecked()) {
            holder.takeAttendanceChildBinding.dropAttendanceStatus.setText(mContext.getResources().getString(R.string.present));
        } else {
            holder.takeAttendanceChildBinding.dropAttendanceStatus.setText(mContext.getResources().getString(R.string.absent));
        }

        holder.takeAttendanceChildBinding.dropAttendanceStatus.setOptionList(option_attendance_status);
        holder.takeAttendanceChildBinding.dropAttendanceStatus.setClickListener(new DropDownBlue.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name,String id) {

//                if(name.equalsIgnoreCase(mContext.getResources().getString(R.string.present))){
//                    holder.takeAttendanceChildBinding.dropAttendanceStatus.setTextColor(mContext.getResources().getColor(R.color.indicator_selector));
//                }else{
//                    holder.takeAttendanceChildBinding.dropAttendanceStatus.setTextColor(mContext.getResources().getColor(R.color.purple_700));
//                }

                jsonObjectPresnet = new JSONObject();
                AcademyStudenModel manageCoachesModell = data.get(position);
                if (manageCoachesModell.isChecked() == true) {
                    manageCoachesModell.setChecked(false);

                    holder.takeAttendanceChildBinding.dropAttendanceStatus.setTextColor(mContext.getResources().getColor(R.color.purple_700));

                    for(int i=0;i<jsonArrayPresent.length();i++){

                        try {
                            if(jsonArrayPresent.getJSONObject(i).getString("user_id").equalsIgnoreCase(manageCoachesModell.getUser_id())){
                                jsonArrayPresent.remove(i);
                            }
//                              Toaster.customToast(jsonArrayPresent.getJSONObject(i).getString("user_id")+"/" +
//                                      manageCoachesModell.getUser_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } else {
                    manageCoachesModell.setChecked(true);
                    holder.takeAttendanceChildBinding.dropAttendanceStatus.setTextColor(mContext.getResources().getColor(R.color.indicator_selector));
                    try {
                        jsonObjectPresnet.put("attendance_status", "P");
                        jsonObjectPresnet.put("user_id", manageCoachesModell.getUser_id());
                        jsonArrayPresent.put(jsonObjectPresnet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                callback.parsent(jsonArrayPresent);



            }


            @Override
            public void onDismiss() {
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TakeAttendanceChildBinding takeAttendanceChildBinding;

        public MyViewHolder(@NonNull TakeAttendanceChildBinding binding) {
            super(binding.getRoot());
            takeAttendanceChildBinding = binding;
            
        }
    }


    public interface clickCallback {

        void parsent(JSONArray jsonArray);


        void sendMessage(String coachId, String userId);
    }

}
