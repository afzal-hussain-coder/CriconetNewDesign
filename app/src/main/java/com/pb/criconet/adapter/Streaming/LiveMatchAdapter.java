package com.pb.criconet.adapter.Streaming;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.pb.criconet.Activity.Streaming.Play_Live_Stream_Single;
import com.pb.criconet.R;
import com.pb.criconet.model.StreamingModel.LiveStreamingNewModel;
import java.util.ArrayList;

public class LiveMatchAdapter extends RecyclerView.Adapter<LiveMatchAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    //ArrayList<LiveStreamingModel> arrayList_image;
    itemClickListener itemClickListener;
    ArrayList<LiveStreamingNewModel> arrayList_image;

    public LiveMatchAdapter(ArrayList<LiveStreamingNewModel> chatname_list1, Context context) {
        this.context = context;
        this.arrayList_image = chatname_list1;
        //this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stream_adapter, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items

        LiveStreamingNewModel liveStreamingNewModel = arrayList_image.get(position);
        holder.ground_name.setText(arrayList_image.get(position).getTitle());
//            holder.team_name.setText(arrayList_image.get(position).getTeam_name());
//        if (arrayList_image.get(position).getIs_power().equalsIgnoreCase("0")) {
//            holder.location.setText(arrayList_image.get(position).getPower_msg());
//        } else {
//            holder.location.setText(arrayList_image.get(position).getDesc());
//        }
        holder.location.setText(arrayList_image.get(position).getDesc());

        Glide.with(context).load(arrayList_image.get(position).getCover()).dontAnimate()
                .into(holder.play);

        /*New Api parsing..*/
        if (arrayList_image.get(position).getIs_match_started().equalsIgnoreCase("0")) {
            holder.tv_not_match.setVisibility(View.VISIBLE);
            holder.tv_not_match.setText(arrayList_image.get(position).getIs_match_start_lebel());
            holder.itemView.setClickable(false);
        } else if (arrayList_image.get(position).getIs_power().equalsIgnoreCase("1")) {
            holder.tv_not_match.setVisibility(View.VISIBLE);
            holder.tv_not_match.setText(arrayList_image.get(position).getPower_msg());
            holder.itemView.setClickable(false);
        } else {
            holder.itemView.setClickable(true);
            holder.tv_not_match.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, Play_Live_Stream_Single.class);
                intent.putExtra("FROM", "1");
                intent.putExtra("matchId", liveStreamingNewModel.getId());
                context.startActivity(intent);
            });
        }


        /*Old Api parsing..*/
//        if(arrayList_image.get(position).getIs_match_start().equalsIgnoreCase("0")){
//            holder.tv_not_match.setVisibility(View.VISIBLE);
//            holder.tv_not_match.setText(arrayList_image.get(position).getIs_match_start_lebel());
//            holder.itemView.setClickable(false);
//        }else{
//            holder.itemView.setClickable(true);
//            holder.tv_not_match.setVisibility(View.GONE);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Intent intent = new Intent(context, Play_Stream.class);
////                    Intent intent = new Intent(context, Play_Live_Stream.class);
//                    Intent intent = new Intent(context, Play_Live_Stream_Single.class);
//                    intent.putExtra("FROM","1");
//                    intent.putExtra("data", LiveStreamingModel.toJson(arrayList_image.get(position)).toString());
//                    context.startActivity(intent);
//                }
//            });
//       }

//            try {
//                Glide.with(context).load(arrayList_image.get(position).get())
//                        .apply(new RequestOptions().placeholder(R.drawable.app_icon).dontAnimate())
//                        .into(holder.roundedImageView);
//            } catch (Exception e) {
//                holder.roundedImageView.setImageResource(R.drawable.app_icon);
//            }


//        Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
//        holder.relative_dash.startAnimation(scaleUp);

//        holder.rlShare.setOnClickListener(v -> {
//            itemClickListener.share(position);
//        });
    }

    @Override
    public int getItemCount() {

        return arrayList_image.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView roundedImageView, play;
        TextView ground_name, team_name, location, tv_not_match;
        Button play_icon;
        MaterialCardView relative_dash;
        RelativeLayout rlShare;

        MyViewHolder(View itemView) {
            super(itemView);
            relative_dash = itemView.findViewById(R.id.relative_dash);
            roundedImageView = itemView.findViewById(R.id.roundedImageView);
            ground_name = itemView.findViewById(R.id.ground_name);
//                holder.team_name = (TextView) convertView.findViewById(R.id.team_name);
            location = itemView.findViewById(R.id.location);
            play = itemView.findViewById(R.id.logo_imageview);
            tv_not_match = itemView.findViewById(R.id.tv_not_match);
//                holder.play_icon = (Button) convertView.findViewById(R.id.play_icon);

            rlShare = itemView.findViewById(R.id.rlShare);

        }
    }

    public interface itemClickListener {
        void share(int pos);
    }
}
