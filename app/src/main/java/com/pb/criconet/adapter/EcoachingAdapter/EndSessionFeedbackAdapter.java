package com.pb.criconet.adapter.EcoachingAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.model.Coaching.FeedBackFormChildData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EndSessionFeedbackAdapter extends RecyclerView.Adapter<EndSessionFeedbackAdapter.myViewHolder> {

    private ArrayList<FeedBackFormChildData> feedbackModelList;
    private Context mContext;
    private int lastSelectedPosition = -1;
    private radioClickListenerr radioClickListenerr;
    int pos;
    ArrayList<Integer>question=null;
    ArrayList<Integer>answer=null;

    public EndSessionFeedbackAdapter(ArrayList<FeedBackFormChildData> feedbackModelList, Context mContext,radioClickListenerr radioClickListenerr) {
        this.feedbackModelList = feedbackModelList;
        this.mContext = mContext;
        this.radioClickListenerr =radioClickListenerr;
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.end_session_feedback_item, parent, false);
        return new myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull myViewHolder holder, int position) {
        FeedBackFormChildData feedbackModel = feedbackModelList.get(position);
        question =new ArrayList<>();
        answer =new ArrayList<>();
        int count = position + 1;
        holder.tv_question.setText(count+"."+feedbackModel.getQuestion());
        holder.tv_low_stsr.setText(feedbackModel.getLow_str());
        holder.tv_high_str.setText(feedbackModel.getHigh_str());
        //ArrayList jsonArray =feedbackModelList.get(position).getRatinglist();
        ArrayList jsonArray = new ArrayList();
        jsonArray.add("1");
        jsonArray.add("2");
        jsonArray.add("3");
        jsonArray.add("4");
        jsonArray.add("5");
        jsonArray.add("6");
        jsonArray.add("7");
        jsonArray.add("8");
        jsonArray.add("9");
        jsonArray.add("10");
        EndSessionFeedbackAdapterChild endSessionFeedbackAdapterChild=new EndSessionFeedbackAdapterChild(jsonArray, mContext, count, new EndSessionFeedbackAdapterChild.radioClickListener() {
            @Override
            public void radioClick(int q1, int v1) {


                question.add(q1);
                answer.add(v1);
                //Toaster.customToast(q1+"/"+v1);
                radioClickListenerr.radioClickk(q1,v1);
                //notifyDataSetChanged();
            }
        });
        holder.rec_item.setAdapter(endSessionFeedbackAdapterChild);


    }

    @Override
    public int getItemCount() {
        return feedbackModelList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_question, tv_low_stsr, tv_high_str;
        RecyclerView rec_item;


        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_question = itemView.findViewById(R.id.tv_question);
            tv_low_stsr = itemView.findViewById(R.id.tv_low_stsr);
            tv_high_str = itemView.findViewById(R.id.tv_high_str);
            rec_item =itemView.findViewById(R.id.rec_item);
            rec_item.setLayoutManager(new LinearLayoutManager(
                    mContext,
                    LinearLayoutManager.HORIZONTAL,
                    false));
            rec_item.setHasFixedSize(true);

        }


    }

    public interface radioClickListenerr {
        void radioClickk(int q1, int v1);
    }

}

class EndSessionFeedbackAdapterChild extends RecyclerView.Adapter<EndSessionFeedbackAdapterChild.myViewHolder> {

    private ArrayList<String> feedbackModelListt;
    private Context mContext;
    private radioClickListener radioClickListener;
    ArrayList<Integer>integers=null;
    int selectedItemPos = -1;
    int lastItemSelectedPos = -1;
    int positionn=0;
    ArrayList<Integer>question=null;
    ArrayList<Integer>answer=null;

    public EndSessionFeedbackAdapterChild(ArrayList<String> feedbackModelListt,Context mContext,int pos,radioClickListener radioClickListener) {
        this.feedbackModelListt = feedbackModelListt;
        this.mContext = mContext;
        this.positionn=pos;
        this.radioClickListener =radioClickListener;
        question=new ArrayList<>();
        answer =new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.end_session_feedback_itemm, parent, false);
        return new myViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull @NotNull myViewHolder holder, int position) {

        String feedbackModel = feedbackModelListt.get(position);
        int count = position + 1;
        if(position == selectedItemPos){
            radioClickListener.radioClick(positionn,selectedItemPos+1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tv_2.setVisibility(View.VISIBLE);
                holder.tv_2.setBackground(mContext.getResources().getDrawable(R.drawable.bg_selected));
            }
        }
        else{
            holder.tv_2.setVisibility(View.GONE);
            holder.tv_1.setBackground(mContext.getResources().getDrawable(R.drawable.bg_unselected));
        }

        holder.tv_1.setText(feedbackModel);
        holder.tv_2.setText(feedbackModel);


    }



    @Override
    public int getItemCount() {
        return feedbackModelListt.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView tv_1,tv_2;


        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);

            tv_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItemPos = getLayoutPosition();
                    if(lastItemSelectedPos == -1){
                        lastItemSelectedPos = selectedItemPos;
                        //Toaster.customToast("hi");

                        question.add(positionn);
                        answer.add(selectedItemPos+1);
                    }
                    else {
                        //Toaster.customToast("h2");
                        question.add(positionn);
                        answer.add(selectedItemPos+1);
                        notifyItemChanged(lastItemSelectedPos);
                        lastItemSelectedPos = selectedItemPos;
                    }
                    notifyItemChanged(selectedItemPos);

                }
            });
        }


    }

    public interface radioClickListener {
        void radioClick(int q1, int v1);
    }


}
