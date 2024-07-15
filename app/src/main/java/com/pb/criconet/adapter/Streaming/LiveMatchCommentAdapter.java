package com.pb.criconet.adapter.Streaming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.pb.criconet.R;
import com.pb.criconet.model.StreamingModel.LiveMatchCommentModel;
import com.pb.criconet.util.Global;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveMatchCommentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<LiveMatchCommentModel> liveMatchCommentModelArrayList;

    public LiveMatchCommentAdapter(Context mContext, ArrayList<LiveMatchCommentModel> liveMatchCommentModelArrayList) {
        this.mContext = mContext;
        this.liveMatchCommentModelArrayList = liveMatchCommentModelArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case LiveMatchCommentModel.simple_type:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_match_comment_chat, parent, false);
                return new TextTypeViewHolder(view);
            case LiveMatchCommentModel.emoji_type:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_child_send_item, parent, false);
                return new ImageTypeViewHolder(view);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LiveMatchCommentModel liveMatchCommentModel = liveMatchCommentModelArrayList.get(position);

        if (liveMatchCommentModel != null) {

//            ((TextTypeViewHolder)holder).tv_name.setText(Global.capitalizeFirstLatterOfString(liveMatchCommentModel.getName()));
//            Glide.with(mContext).load(liveMatchCommentModel.getAvatar()).into( ((TextTypeViewHolder)holder).iv_profile);
//            ((TextTypeViewHolder)holder).tv_message.setText(liveMatchCommentModel.getComment_text());

//            if(((TextTypeViewHolder) holder).liTextImage.getVisibility() == View.GONE){
//                    ((TextTypeViewHolder) holder).liTextImage.setVisibility(View.VISIBLE);
//                }
//            ((TextTypeViewHolder)holder).tv_name.setText(Global.capitalizeFirstLatterOfString(liveMatchCommentModel.getName()));
//            Glide.with(mContext).load(liveMatchCommentModel.getAvatar()).into( ((TextTypeViewHolder)holder).iv_profile);
//            ((TextTypeViewHolder)holder).tv_message.setText(liveMatchCommentModel.getComment_text());

//            Glide.with(mContext).load(liveMatchCommentModel.getEmojiImage()).into( ((TextTypeViewHolder)holder).imageViewAttached);


//            if(liveMatchCommentModel.getEmojiImage().isEmpty() && !liveMatchCommentModel.getComment_text().isEmpty()){
//
//                if(((TextTypeViewHolder) holder).tv_message.getVisibility() == View.GONE){
//                    ((TextTypeViewHolder) holder).tv_message.setVisibility(View.VISIBLE);
//                }else{
//                    ((TextTypeViewHolder) holder).tv_message.setVisibility(View.GONE);
//                }
//                if(((TextTypeViewHolder) holder).liTextImage.getVisibility() == View.VISIBLE){
//                    ((TextTypeViewHolder) holder).liTextImage.setVisibility(View.GONE);
//                }
//
//
//
//            }else if(!liveMatchCommentModel.getEmojiImage().isEmpty() && !liveMatchCommentModel.getComment_text().isEmpty()){
//
//                if(((TextTypeViewHolder) holder).tv_message.getVisibility() == View.VISIBLE){
//                    ((TextTypeViewHolder) holder).tv_message.setVisibility(View.GONE);
//                }
//
//                if(((TextTypeViewHolder) holder).liTextImage.getVisibility() == View.GONE){
//                    ((TextTypeViewHolder) holder).liTextImage.setVisibility(View.VISIBLE);
//                }
//
//                if(((TextTypeViewHolder) holder).textViewAttachedWithImage.getVisibility() == View.GONE){
//                    ((TextTypeViewHolder) holder).textViewAttachedWithImage.setVisibility(View.VISIBLE);
//                }
//
//
//                ((TextTypeViewHolder)holder).textViewAttachedWithImage.setText(liveMatchCommentModel.getComment_text());
//                Glide.with(mContext).load(liveMatchCommentModel.getEmojiImage()).into( ((TextTypeViewHolder)holder).imageViewAttached);
//            }else{
//                if(((TextTypeViewHolder) holder).tv_message.getVisibility() == View.VISIBLE){
//                    ((TextTypeViewHolder) holder).tv_message.setVisibility(View.GONE);
//                }
//
//
//                if(((TextTypeViewHolder) holder).liTextImage.getVisibility() == View.GONE){
//                    ((TextTypeViewHolder) holder).liTextImage.setVisibility(View.VISIBLE);
//                }
//
//                if(((TextTypeViewHolder) holder).textViewAttachedWithImage.getVisibility() == View.VISIBLE){
//                    ((TextTypeViewHolder) holder).textViewAttachedWithImage.setVisibility(View.GONE);
//                }
//                Glide.with(mContext).load(liveMatchCommentModel.getEmojiImage()).into( ((TextTypeViewHolder)holder).imageViewAttached);
//            }

            switch (liveMatchCommentModel.getType()) {

                case LiveMatchCommentModel.simple_type:

//                  String userName = Global.capitalizeFirstLatterOfString(liveMatchCommentModel.getName());
//                  int length = userName.length();
//                  String message = liveMatchCommentModel.getComment_text();
//                  String final_value  = userName +" "+" "+ message;
//
//                  SpannableString spannableString = new SpannableString(final_value);
//                  spannableString.setSpan(new RelativeSizeSpan(1.2f), 0,length, 0);
//                  spannableString.setSpan(
//                          new ForegroundColorSpan(mContext.getResources().getColor(R.color.dark_grey)),
//                          0, length,
//                          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                  ((TextTypeViewHolder)holder).tv_message.setText(spannableString);

                    if(!liveMatchCommentModel.getName().equalsIgnoreCase("")){
                        ((TextTypeViewHolder) holder).tv_name.setText(Global.capitalizeFirstLatterOfString(liveMatchCommentModel.getName()));
                    }


                    Glide.with(mContext).load(liveMatchCommentModel.getAvatar()).into(((TextTypeViewHolder) holder).iv_profile);
                    ((TextTypeViewHolder) holder).tv_message.setText(liveMatchCommentModel.getComment_text());


//                  if (liveMatchCommentModel.getVerified().equalsIgnoreCase("1")) {
//                      ((TextTypeViewHolder)holder).iv_verified.setVisibility(View.VISIBLE);
//                      if(liveMatchCommentModel.getVerified().equalsIgnoreCase("1") &&
//                              liveMatchCommentModel.getCriconet_verified().equalsIgnoreCase("1")){
//                          ((TextTypeViewHolder)holder).iv_verified.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
//                      }else{
//                          ((TextTypeViewHolder)holder).iv_verified.setColorFilter(ContextCompat.getColor(mContext, R.color.verified_user_color));
//                      }
//                  } else {
//                      ((TextTypeViewHolder)holder).iv_verified.setVisibility(View.GONE);
//                      ((TextTypeViewHolder)holder).iv_verified.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.bckground_light));
//                  }


//                  if (liveMatchCommentModel.getSearchUserList().isEmpty()) {
//                      ((TextTypeViewHolder)holder).tv_message.setVisibility(View.VISIBLE);
//                      ((TextTypeViewHolder)holder).post_text_autolink.setVisibility(View.GONE);
//                      ((TextTypeViewHolder)holder).tv_message.setText(liveMatchCommentModel.getComment_text());
//                  } else {
//                      ((TextTypeViewHolder)holder).tv_message.setVisibility(View.GONE);
//                      ((TextTypeViewHolder)holder).post_text_autolink.setVisibility(View.VISIBLE);
//                      ((TextTypeViewHolder)holder).post_text_autolink.addAutoLinkMode(
//                              AutoLinkMode.MODE_HASHTAG,
//                              AutoLinkMode.MODE_PHONE,
//                              AutoLinkMode.MODE_URL,
//                              AutoLinkMode.MODE_EMAIL,
//                              AutoLinkMode.MODE_CUSTOM,
//                              AutoLinkMode.MODE_MENTION);
//                      ((TextTypeViewHolder)holder).post_text_autolink.setAutoLinkText(Global.capitalizeFirstLatterOfString(liveMatchCommentModel.getComment_text()));
//                      ((TextTypeViewHolder)holder).post_text_autolink.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.app_green));
//                      ((TextTypeViewHolder)holder).post_text_autolink.setPhoneModeColor(ContextCompat.getColor(mContext, R.color.app_green));
//                      ((TextTypeViewHolder)holder).post_text_autolink.setMentionModeColor(ContextCompat.getColor(mContext, R.color.app_green));
//                      ((TextTypeViewHolder)holder).post_text_autolink.setUrlModeColor(ContextCompat.getColor(mContext, R.color.app_green));
//                      ((TextTypeViewHolder)holder).post_text_autolink.setEmailModeColor(ContextCompat.getColor(mContext, R.color.app_green));
//                      ((TextTypeViewHolder)holder).post_text_autolink.setSelectedStateColor(ContextCompat.getColor(mContext, R.color.competition_main_color));
//
//                      ((TextTypeViewHolder)holder).post_text_autolink.setAutoLinkOnClickListener((autoLinkMode, matchedText) -> {
//                          String match = matchedText.trim();
//
//                          if (autoLinkMode == AutoLinkMode.MODE_URL) {
//                              Intent i = new Intent(mContext, WebView_Activity.class);
//                              i.putExtra(WebView_Activity.WebURL, matchedText.trim());
//                              mContext.startActivity(i);
//                          }
////                          else if (autoLinkMode == AutoLinkMode.MODE_PHONE) {
////                              mContext.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" +"+91-"+ matchedText.trim())));
////                          }
//                          else if(autoLinkMode == AutoLinkMode.MODE_EMAIL){
//                              Intent intent = new Intent(Intent.ACTION_SEND);
//                              intent.setType("plain/text");
//                              intent.putExtra(Intent.EXTRA_EMAIL, new String[] { matchedText.trim() });
//                              intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                              intent.putExtra(Intent.EXTRA_TEXT, "");
//                              mContext.startActivity(Intent.createChooser(intent, ""));
//                          }else{
//                              for (int i = 0; i < liveMatchCommentModel.getSearchUserList().size(); i++) {
//                                  // Toaster.customToast(match + "/" + tagModelArrayList.get(i).getUser_id() + "?" + tagModelArrayList.get(i).getMatch_name());
//                                  if (match.equalsIgnoreCase(liveMatchCommentModel.getSearchUserList().get(i).getMatch_name().trim())) {
//
//                                      Intent intent = new Intent(mContext, UserDetails.class);
//                                      intent.putExtra("user_id", liveMatchCommentModel.getSearchUserList().get(i).getUser_id());
//                                      intent.putExtra("FROM", "3");
//                                      mContext.startActivity(intent);
//                                      // (Activity)mContext
//
//                                  if (liveMatchCommentModel.getPublisher_type().equalsIgnoreCase("user")) {
//                                      Intent intent = new Intent(mContext, UserDetails.class);
//                                      intent.putExtra("user_id", data.getTagsusers().get(i).getUser_id());
//                                      intent.putExtra("FROM", "3");
//                                      mContext.startActivity(intent);
//                                      mContext.finish();
//                                  } else {
//                                      Intent intent = new Intent(mContext, PagesDetails.class);
//                                      intent.putExtra("page_id", data.getPage_id());
//                                      intent.putExtra("FROM", "3");
//                                      mContext.startActivity(intent);
//                                      mContext.finish();
//                                  }
//                                  }
//                              }
//                          }
//
//
//
//                      });
//
//
                    break;

                case LiveMatchCommentModel.emoji_type:


//                  int len = Global.capitalizeFirstLatterOfString(liveMatchCommentModel.getName()).length();
//                  SpannableString spannableStringg = new SpannableString(Global.capitalizeFirstLatterOfString(liveMatchCommentModel.getName()));
//                  spannableStringg.setSpan(new RelativeSizeSpan(1.2f), 0,len, 0);
//                  ((ImageTypeViewHolder)holder).tv_name.setText(spannableStringg);



                    if(!liveMatchCommentModel.getName().equalsIgnoreCase("")){
                        ((ImageTypeViewHolder) holder).tv_name.setText(Global.capitalizeFirstLatterOfString(liveMatchCommentModel.getName()));
                    }

                    ((ImageTypeViewHolder) holder).textViewAttachedWithImage.setText(liveMatchCommentModel.getComment_text());

                    Glide.with(mContext).load(liveMatchCommentModel.getAvatar()).into(((ImageTypeViewHolder) holder).iv_profile);
                    Glide.with(mContext).load(liveMatchCommentModel.getEmojiImage()).into(((ImageTypeViewHolder) holder).imageViewAttached);

//                  if (liveMatchCommentModel.getVerified().equalsIgnoreCase("1")) {
//                      ((ImageTypeViewHolder)holder).iv_verified.setVisibility(View.VISIBLE);
//                      if(liveMatchCommentModel.getVerified().equalsIgnoreCase("1") &&
//                              liveMatchCommentModel.getCriconet_verified().equalsIgnoreCase("1")){
//                          ((ImageTypeViewHolder)holder).iv_verified.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
//                      }else{
//                          ((ImageTypeViewHolder)holder).iv_verified.setColorFilter(ContextCompat.getColor(mContext, R.color.verified_user_color));
//                      }
//                  } else {
//                      ((ImageTypeViewHolder)holder).iv_verified.setVisibility(View.GONE);
//                      ((ImageTypeViewHolder)holder).iv_verified.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.bckground_light));
//

                    break;
            }

        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (liveMatchCommentModelArrayList.get(position).type) {
            case 0:
                return LiveMatchCommentModel.simple_type;
            case 1:
                return LiveMatchCommentModel.emoji_type;
            default:
                return -1;
        }
    }

    public void resetData(ArrayList<LiveMatchCommentModel> liveMatchCommentModelArrayListt) {
        liveMatchCommentModelArrayList.clear();
        liveMatchCommentModelArrayList.addAll(liveMatchCommentModelArrayListt);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return liveMatchCommentModelArrayList.size();
    }


    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {

        CircleImageView iv_profile;
        TextView tv_message;
        AutoLinkTextView post_text_autolink;
        TextView tv_name;
        ImageView iv_verified;


        public TextTypeViewHolder(View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_message = itemView.findViewById(R.id.tv_message);
            post_text_autolink = itemView.findViewById(R.id.post_text_autolink);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_verified = itemView.findViewById(R.id.iv_verified);
        }
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        //ImageView iv_emoji_send;
        CircleImageView iv_profile;
        TextView tv_name;
        ImageView iv_verified;

        ShapeableImageView imageViewAttached;
        TextView textViewAttachedWithImage;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.imageViewAttached = itemView.findViewById(R.id.imageViewAttachedd);
            this.textViewAttachedWithImage = itemView.findViewById(R.id.textViewAttachedWithImagee);

            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.tv_name);

            //this.iv_verified = itemView.findViewById(R.id.iv_verified);
        }
    }
}
