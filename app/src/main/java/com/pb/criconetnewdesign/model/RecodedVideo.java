package com.pb.criconetnewdesign.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RecodedVideo implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(String postVideo) {
        this.postVideo = postVideo;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String id="";
    private String user_id="";
    private String postVideo="";
    private String created="";
    private String status="";

    public int getIs_delete_permission() {
        return is_delete_permission;
    }

    public void setIs_delete_permission(int is_delete_permission) {
        this.is_delete_permission = is_delete_permission;
    }

    private int is_delete_permission;
    public String getRecorded_by() {
        return recorded_by;
    }

    public void setRecorded_by(String recorded_by) {
        this.recorded_by = recorded_by;
    }

    public String getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(String academy_id) {
        this.academy_id = academy_id;
    }

    private String recorded_by="";
    private String academy_id="";

    public RecodedVideo(JSONObject jsonObject){
        if(jsonObject.has("id")){
            try {
                this.id= jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }
        if(jsonObject.has("user_id")){
            try {
                this.user_id= jsonObject.getString("user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }
        if(jsonObject.has("postVideo")){
            try {
                this.postVideo= jsonObject.getString("postVideo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }
        if(jsonObject.has("created")){
            try {
                this.created= jsonObject.getString("created");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }
        if(jsonObject.has("status")){
            try {
                this.status= jsonObject.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }
        if(jsonObject.has("is_delete_permission")){
            try {
                this.is_delete_permission= jsonObject.getInt("is_delete_permission");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }


    }
}
