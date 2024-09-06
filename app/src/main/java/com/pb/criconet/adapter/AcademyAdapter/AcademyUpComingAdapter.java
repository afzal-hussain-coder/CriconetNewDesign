package com.pb.criconet.adapter.AcademyAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.Activity.Academy.AcademyJoinSessionActivity;
import com.pb.criconet.databinding.UpcomingSessionItemBinding;
import com.pb.criconet.model.AcademyModel.AcademySessionCoach;
import com.pb.criconet.model.AcademyModel.AcademySessionJoiningMemberCommon;
import com.pb.criconet.model.AcademyModel.AcademySessionStudent;
import com.pb.criconet.model.AcademyModel.ScheduledListModel;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.Toaster;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AcademyUpComingAdapter extends RecyclerView.Adapter<AcademyUpComingAdapter.MyViewHolder>{

    Context context;
    LayoutInflater inflater;
    OnItemClickListener itemClickListener;
    int count = 0;
    ArrayList<Integer> integers;
    String leaugeName = "";
    private ArrayList<ScheduledListModel> myContestListModelslist;
    String session_status="";

    public AcademyUpComingAdapter(Context context, ArrayList<ScheduledListModel> myContestListModelslist, String leaugeName,String session_status){
        this.context = context;
        this.myContestListModelslist = myContestListModelslist;
        this.leaugeName = leaugeName;
        this.session_status = session_status;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(UpcomingSessionItemBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ScheduledListModel myContestListModel = myContestListModelslist.get(position);

        holder.upcomingSessionItemBinding.tvTopic.setText("Toady Session Topic : "+myContestListModel.getTitle());

        String duration_hour=myContestListModel.getDuration_hrs();
        String duration_minute = myContestListModel.getDuration_mins();

        String finalDuration="";

        if(duration_hour.equalsIgnoreCase("0")){
            finalDuration ="00"+":"+duration_minute+" Minute";
        }else if(duration_minute.equalsIgnoreCase("0")){
            finalDuration=duration_hour+":"+"00"+"Hour";
        }else{
            finalDuration=duration_hour+" Hour"+" : "+duration_minute+" Minute";
        }

        holder.upcomingSessionItemBinding.tvBookingDuration.setText("Slot Duration- "+finalDuration);

        holder.upcomingSessionItemBinding.tvBookingDate.setText(Global.convertUTCDateToLocall(myContestListModel.getSchedule_date())+" , "+
                myContestListModel.getSchedule_time());

        holder.upcomingSessionItemBinding.flJoinSession.setVisibility(View.VISIBLE);

        ArrayList<AcademySessionJoiningMemberCommon> academySessionJoiningMemberCommonList = new ArrayList<>();


        if (myContestListModel.getAcademySessionCoachList() != null) {
            for (AcademySessionCoach coach : myContestListModel.getAcademySessionCoachList()) {
                AcademySessionJoiningMemberCommon commonMember = new AcademySessionJoiningMemberCommon(
                        coach.getUser_id(),
                        coach.getName(),
                        coach.getUsername(),
                        coach.getFirst_name(),
                        coach.getLast_name(),
                        coach.getMid_name(),
                        coach.getAvatar()
                );
                academySessionJoiningMemberCommonList.add(commonMember);
            }
        }

        if (myContestListModel.getAcademySessionStudentList() != null) {
            for (AcademySessionStudent student : myContestListModel.getAcademySessionStudentList()) {
                AcademySessionJoiningMemberCommon commonMember = new AcademySessionJoiningMemberCommon(
                        student.getUser_id(),
                        student.getName(),
                        student.getUsername(),
                        student.getFirst_name(),
                        student.getLast_name(),
                        student.getMid_name(),
                        student.getAvatar()
                );
                academySessionJoiningMemberCommonList.add(commonMember);
            }
        }


        holder.upcomingSessionItemBinding.rcvJoiningMember.setHasFixedSize(true);
        holder.upcomingSessionItemBinding.rcvJoiningMember.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.upcomingSessionItemBinding.rcvJoiningMember.setAdapter(new AcademySessionJoiningMemberAdapter(context,academySessionJoiningMemberCommonList));


        if(session_status.equalsIgnoreCase("Past")){
            holder.upcomingSessionItemBinding.flJoinSession.setVisibility(View.GONE);
            holder.upcomingSessionItemBinding.tvJoningMember.setText("Members who have done the session");
        }else{
            holder.upcomingSessionItemBinding.flJoinSession.setVisibility(View.VISIBLE);
            holder.upcomingSessionItemBinding.tvJoningMember.setText("Joining member");

        }

        if(myContestListModel.getJoin_btn().equalsIgnoreCase("0")){
            holder.upcomingSessionItemBinding.flJoinSession.setVisibility(View.GONE);
        }else{
            holder.upcomingSessionItemBinding.flJoinSession.setVisibility(View.VISIBLE);
        }

        holder.upcomingSessionItemBinding.flJoinSession.setOnClickListener(v -> {
            context.startActivity(new Intent(context, AcademyJoinSessionActivity.class)
                    .putExtra("id",myContestListModel.getId())
                    .putExtra("channel_id",myContestListModel.getChanel_id())
                    .putExtra("timeDuration",holder.upcomingSessionItemBinding.tvBookingDuration.getText().toString()));
        });


    }

    @Override
    public int getItemCount() {
        return myContestListModelslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        UpcomingSessionItemBinding upcomingSessionItemBinding;

        public MyViewHolder(@NonNull UpcomingSessionItemBinding upcomingSessionItemBinding) {
            super(upcomingSessionItemBinding.getRoot());
            this.upcomingSessionItemBinding = upcomingSessionItemBinding;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ArrayList<Integer> integers);
    }
}
