package com.pb.criconet.adapter.AcademyAdapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.adapter.PavilionAdapter.HomeAdapter;
import com.pb.criconet.layout.ExpandableTextView;
import com.pb.criconet.model.AcademyModel.AcademyTipsPreviewModel;
import com.pb.criconet.model.pavilionModel.NewPostModel;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import java.io.IOException;
import java.util.ArrayList;

import jaygoo.widget.wlv.WaveLineView;

public class AcademyTipsPreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_VIDEO = 0, TYPE_IMAGE = 1, TYPE_AUDIO =  2;

    private Context mContext;
    private ArrayList<AcademyTipsPreviewModel> academyTipsPreviewModels;
    protected ItemListener mListener;
    String FROM="";
    SharedPreferences preferences;
    //private MediaPlayer mPlayer=null;

    private MediaPlayer mPlayer = null;
    private int playingPosition = -1;
    private MyViewHolderAudio playingHolder = null;

    public AcademyTipsPreviewAdapter(String FROM, Context mContext, ArrayList<AcademyTipsPreviewModel> academyTipsPreviewModels, ItemListener mListener) {
        this.mContext = mContext;
        this.academyTipsPreviewModels = academyTipsPreviewModels;
        this.mListener=mListener;
        this.FROM= FROM;
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

       // mPlayer = new MediaPlayer();
    }

    @Override
    public int getItemViewType(int position) {
      final  AcademyTipsPreviewModel academyTipsPreviewModel = academyTipsPreviewModels.get(position);

        if (academyTipsPreviewModel.getMedia_type().equalsIgnoreCase("Image")) {
            return TYPE_IMAGE;
        } else if (academyTipsPreviewModel.getMedia_type().equalsIgnoreCase("Video")) {
            return TYPE_VIDEO;
        } else if (academyTipsPreviewModel.getMedia_type().equalsIgnoreCase("Audio")) {
            return TYPE_AUDIO;
        } else {
            return TYPE_IMAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_VIDEO: {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tips_preview_child, parent, false);
                return new MyViewHolder(itemView);
            }
            case TYPE_IMAGE: {
                View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.tips_preview_child_image, parent, false);
                return new MyViewHolderImage(itemView1);
            }
            default: {
                View itemView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.tips_preview_child_audio, parent, false);
                return new MyViewHolderAudio(itemView2);
            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final AcademyTipsPreviewModel academyTipsPreviewModel = academyTipsPreviewModels.get(position);

        switch (getItemViewType(position)){
            case TYPE_IMAGE:
                ((MyViewHolderImage)holder).tvofferr.setText(academyTipsPreviewModel.getCat_title());
                ((MyViewHolderImage)holder).tv_dess.setText(academyTipsPreviewModel.getDetails());
                ((MyViewHolderImage)holder).tv_dess.initViews();
                ((MyViewHolderImage)holder).edit_text_namee.setText(academyTipsPreviewModel.getTitle());
                Glide.with(mContext).load(academyTipsPreviewModel.getVideo_path()).into(((MyViewHolderImage)holder).imageView);

                ((MyViewHolderImage)holder).iv_closee.setOnClickListener(v -> {
                    int newPosition = holder.getAdapterPosition();
                    Log.d("Position",newPosition+"");
                    academyTipsPreviewModels.remove(newPosition);
                    notifyItemRemoved(newPosition);
                    notifyItemRangeChanged(newPosition, academyTipsPreviewModels.size());
                    mListener.onItemClickk(newPosition,academyTipsPreviewModel.getTips_id());
                });

                ((MyViewHolderImage)holder).iv_sharee.setOnClickListener(v -> {
                    mListener.onShareItem(academyTipsPreviewModel.getTitle(),academyTipsPreviewModel.getDetails(),academyTipsPreviewModel.getTips_id(),academyTipsPreviewModel.getVideo_path());
                });

//                if((FROM.equalsIgnoreCase("2") && !SessionManager.get_user_id(preferences).equalsIgnoreCase(academyTipsPreviewModel.getUser_id()))|| FROM.equalsIgnoreCase("3") || SessionManager.get_getRoleId(preferences).equalsIgnoreCase("3")){
//                    ((MyViewHolderImage)holder).iv_closee.setVisibility(View.GONE);
//                }
//                /*for comments of DDR at 13-07-23 ye condition add hua hai*/
//                else if(FROM.equalsIgnoreCase("2") && SessionManager.get_user_id(preferences).equalsIgnoreCase(academyTipsPreviewModel.getUser_id())){
//                    ((MyViewHolderImage)holder).iv_closee.setVisibility(View.VISIBLE);
//                }
//                else{
//                    ((MyViewHolderImage)holder).iv_closee.setVisibility(View.VISIBLE);
//                }

                break;

            case TYPE_VIDEO:
                ((MyViewHolder)holder).tvoffer.setText(academyTipsPreviewModel.getCat_title());
                ((MyViewHolder)holder).tv_des.setText(academyTipsPreviewModel.getDetails());
                ((MyViewHolder)holder).tv_des.initViews();
                ((MyViewHolder)holder).edit_text_name.setText(academyTipsPreviewModel.getTitle());
                if(academyTipsPreviewModel.getVideo_path().isEmpty()){
                    ((MyViewHolder)holder).rl_video.setVisibility(View.GONE);
                }else{
                    ((MyViewHolder)holder).rl_video.setVisibility(View.VISIBLE);
                    ((MyViewHolder)holder).videoview.setVideoPath(academyTipsPreviewModel.getVideo_path());
                    // Request layout and focus
                    ((MyViewHolder) holder).videoview.requestLayout();
                    ((MyViewHolder) holder).videoview.requestFocus();
                    ///((MyViewHolder) holder).videoview.start();

                    // Set up an OnPreparedListener to ensure the video starts as soon as it is ready
                    ((MyViewHolder) holder).videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                @Override
                                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                        // Video is ready, start playback
                                        ((MyViewHolder) holder).videoview.start();
                                        return true;
                                    }
                                    return false;
                                }
                            });

                            // Set up a completion listener if you want to take some action when the video ends
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    // Handle video completion
                                }
                            });
                        }
                    });
                }

//                if((FROM.equalsIgnoreCase("2") && !SessionManager.get_user_id(preferences).equalsIgnoreCase(academyTipsPreviewModel.getUser_id()))|| FROM.equalsIgnoreCase("3") || SessionManager.get_getRoleId(preferences).equalsIgnoreCase("3")){
//                    ((MyViewHolder)holder).iv_close.setVisibility(View.GONE);
//                }
//                /*for comments of DDR at 13-07-23 ye condition add hua hai*/
//                else if(FROM.equalsIgnoreCase("2") && SessionManager.get_user_id(preferences).equalsIgnoreCase(academyTipsPreviewModel.getUser_id())){
//                    ((MyViewHolder)holder).iv_close.setVisibility(View.VISIBLE);
//                }
//                else{
//                    ((MyViewHolder)holder).iv_close.setVisibility(View.VISIBLE);
//                }

                ((MyViewHolder)holder).iv_close.setOnClickListener(v -> {
                    int newPosition = holder.getAdapterPosition();
                    Log.d("Position",newPosition+"");
                    academyTipsPreviewModels.remove(newPosition);
                    notifyItemRemoved(newPosition);
                    notifyItemRangeChanged(newPosition, academyTipsPreviewModels.size());
                    mListener.onItemClickk(newPosition,academyTipsPreviewModel.getTips_id());
                });

                ((MyViewHolder)holder).iv_share.setOnClickListener(v -> {
                    mListener.onShareItem(academyTipsPreviewModel.getTitle(),academyTipsPreviewModel.getDetails(),academyTipsPreviewModel.getTips_id(),academyTipsPreviewModel.getVideo_path());
                });
                break;

            default:{
                MyViewHolderAudio audioHolder = (MyViewHolderAudio) holder;

                audioHolder.tvoffer.setText(academyTipsPreviewModel.getCat_title());
                audioHolder.tv_des.setText(academyTipsPreviewModel.getDetails());
                audioHolder.tv_des.initViews();
                audioHolder.edit_text_name.setText(academyTipsPreviewModel.getTitle());

                // Update UI based on whether this item is currently playing
                if (position == playingPosition) {
                    audioHolder.waveLineView.setVisibility(View.VISIBLE);
                    audioHolder.waveLineView.startAnim();
                    audioHolder.iv_play.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pause));
                    audioHolder.tv_play.setText(R.string.pause);
                } else {
                    audioHolder.waveLineView.setVisibility(View.GONE);
                    audioHolder.waveLineView.stopAnim();
                    audioHolder.iv_play.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play));
                    audioHolder.tv_play.setText(R.string.play);
                }

                audioHolder.iv_play.setOnClickListener(v -> {
                    if (position == playingPosition) {
                        // If this item is already playing, pause it
                        if (mPlayer != null && mPlayer.isPlaying()) {
                            mPlayer.pause();
                            audioHolder.iv_play.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play));
                            audioHolder.tv_play.setText(R.string.play);
                            audioHolder.waveLineView.stopAnim();
                            audioHolder.waveLineView.setVisibility(View.GONE);
                        } else if (mPlayer != null) {
                            // If paused, resume playing
                            mPlayer.start();
                            audioHolder.iv_play.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pause));
                            audioHolder.tv_play.setText(R.string.pause);
                            audioHolder.waveLineView.setVisibility(View.VISIBLE);
                            audioHolder.waveLineView.startAnim();
                        }
                    } else {
                        // Stop previous playback if another item was playing
                        if (mPlayer != null) {
                            mPlayer.stop();
                            mPlayer.release();
                            mPlayer = null;

                            // Reset the UI of the previously playing item
                            if (playingHolder != null) {
                                playingHolder.iv_play.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play));
                                playingHolder.tv_play.setText(R.string.play);
                                playingHolder.waveLineView.stopAnim();
                                playingHolder.waveLineView.setVisibility(View.GONE);
                            }
                        }

                        // Start playing new audio
                        mPlayer = new MediaPlayer();
                        try {
                            // Set the wave view to visible immediately
                            audioHolder.waveLineView.setVisibility(View.VISIBLE);

                            mPlayer.setDataSource(academyTipsPreviewModel.getVideo_path());
                            mPlayer.prepareAsync();  // Use prepareAsync to avoid blocking the UI thread

                            // Set listeners for MediaPlayer
                            mPlayer.setOnPreparedListener(mp -> {
                                mPlayer.start();

                                // Start animation after preparation
                                audioHolder.waveLineView.startAnim();
                                audioHolder.iv_play.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pause));
                                audioHolder.tv_play.setText(R.string.pause);
                            });

                            // Update the playing position and the current holder
                            playingPosition = position;
                            playingHolder = audioHolder;

                            mPlayer.setOnCompletionListener(mp -> {
                                mPlayer.stop();
                                mPlayer.release();
                                mPlayer = null;
                                playingPosition = -1;
                                playingHolder = null;

                                audioHolder.iv_play.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play));
                                audioHolder.tv_play.setText(R.string.play);
                                audioHolder.waveLineView.stopAnim();
                                audioHolder.waveLineView.setVisibility(View.GONE);
                            });
                        } catch (IOException e) {
                            Log.e(TAG, "prepare() failed", e);
                        }
                    }
                });

                if(academyTipsPreviewModel.getUser_id().equalsIgnoreCase(SessionManager.get_user_id(preferences))|| (SessionManager.get_getRoleId(preferences).equalsIgnoreCase("1")||SessionManager.get_getRoleId(preferences).equalsIgnoreCase("2"))){
                    audioHolder.iv_close.setVisibility(View.VISIBLE);
                }else{
                    audioHolder.iv_close.setVisibility(View.GONE);
                }

                audioHolder.iv_close.setOnClickListener(v -> {
                    int newPosition = holder.getAdapterPosition();
                    academyTipsPreviewModels.remove(newPosition);
                    notifyItemRemoved(newPosition);
                    notifyItemRangeChanged(newPosition, academyTipsPreviewModels.size());
                    mListener.onItemClickk(newPosition, academyTipsPreviewModel.getTips_id());
                });

                audioHolder.iv_share.setOnClickListener(v -> {
                    mListener.onShareItem(academyTipsPreviewModel.getTitle(), academyTipsPreviewModel.getDetails(), academyTipsPreviewModel.getTips_id(), academyTipsPreviewModel.getVideo_path());
                });
                break;
            }
        }




    }

    @Override
    public int getItemCount() {
        return academyTipsPreviewModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ExpandableTextView tv_des;
        VideoView videoview;
        TextView edit_text_name, tvoffer;
        RelativeLayout rl_video;
        ImageView iv_close;
        ImageView iv_share;
        //ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvoffer = itemView.findViewById(R.id.tvoffer);
            tv_des = itemView.findViewById(R.id.tv_des);
            videoview = itemView.findViewById(R.id.videoview);
            videoview.setMediaController(new MediaController(mContext));
            new MediaController(mContext).setAnchorView(videoview);
            edit_text_name = itemView.findViewById(R.id.edit_text_name);
            rl_video = itemView.findViewById(R.id.rl_video);
            iv_close = itemView.findViewById(R.id.iv_close);
            iv_share = itemView.findViewById(R.id.iv_share);
           // imageView=itemView.findViewById(R.id.imageView);
        }
    }

    class MyViewHolderImage extends RecyclerView.ViewHolder {
        ExpandableTextView tv_dess;
        TextView edit_text_namee, tvofferr;
        ImageView iv_closee;
        ImageView iv_sharee;
        ImageView imageView;

        public MyViewHolderImage(@NonNull View itemView) {
            super(itemView);
            tvofferr = itemView.findViewById(R.id.tvofferr);
            tv_dess = itemView.findViewById(R.id.tv_dess);
            edit_text_namee = itemView.findViewById(R.id.edit_text_namee);
            iv_closee = itemView.findViewById(R.id.iv_closee);
            iv_sharee = itemView.findViewById(R.id.iv_sharee);
           imageView=itemView.findViewById(R.id.imageView);
        }
    }

    class MyViewHolderAudio extends RecyclerView.ViewHolder {
        ExpandableTextView tv_des;
        TextView edit_text_name, tvoffer;
        ImageView iv_close;
        ImageView iv_share;
        ImageView iv_play;
        TextView tv_play;
        WaveLineView waveLineView;

        public MyViewHolderAudio(@NonNull View itemView) {
            super(itemView);
            tvoffer = itemView.findViewById(R.id.tvoffer);
            tv_des = itemView.findViewById(R.id.tv_des);
            edit_text_name = itemView.findViewById(R.id.edit_text_name);
            iv_close = itemView.findViewById(R.id.iv_close);
            iv_share = itemView.findViewById(R.id.iv_share);
            iv_play = itemView.findViewById(R.id.iv_play);
            tv_play = itemView.findViewById(R.id.tv_play);
            waveLineView = itemView.findViewById(R.id.waveLineView);

        }
    }

    public interface ItemListener {
        void onItemClickk(int size,String _tipsId);
        void onShareItem(String title,String details,String tipsId,String videoPath);
    }
}
