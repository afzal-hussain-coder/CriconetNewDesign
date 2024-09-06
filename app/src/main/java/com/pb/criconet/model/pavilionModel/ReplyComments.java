package com.pb.criconet.model.pavilionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ReplyComments implements Serializable {

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    private String comment_text="";
    private String time="";
    private String user_id ="";
    private String name ="";
    private String avatar ="";
    private String cover ="";

    public String getComment_likes() {
        return comment_likes;
    }

    public void setComment_likes(String comment_likes) {
        this.comment_likes = comment_likes;
    }

    private String comment_likes="";

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    private String reply_id ="";

    public ReplyComments(JSONObject jsonObject){

        if(jsonObject.has("reply_id")){
            try {
                this.reply_id = jsonObject.getString("reply_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("comment_likes")){
            try {
                this.comment_likes = jsonObject.getString("comment_likes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("comment_text")){
            try {
                this.comment_text = jsonObject.getString("comment_text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("time")){
            try {
                this.time = jsonObject.getString("time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("user_id")){
            try {
                this.user_id = jsonObject.getString("user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("name")){
            try {
                this.name = jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("avatar")){
            try {
                this.avatar = jsonObject.getString("avatar");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("cover")){
            try {
                this.cover = jsonObject.getString("cover");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
