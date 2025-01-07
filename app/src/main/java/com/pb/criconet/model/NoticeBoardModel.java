package com.pb.criconet.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NoticeBoardModel implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotic_type() {
        return notic_type;
    }

    public void setNotic_type(String notic_type) {
        this.notic_type = notic_type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    private String id ="";
    private String title ="";
    private String notic_type ="";
    private String details ="";
    private String created_by ="";

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;
    private String created_at ="";
    private String status ="";
    private String name ="";
    private String avatar ="";


    public NoticeBoardModel(JSONObject jsonObject){
        if(jsonObject.has("id")){
            try {
                this.id = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(jsonObject.has("title")){
            try {
                this.title = jsonObject.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(jsonObject.has("notic_type")){
            try {
                this.notic_type = jsonObject.getString("notic_type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(jsonObject.has("details")){
            try {
                this.details = jsonObject.getString("details");
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
        if(jsonObject.has("created_by")){
            try {
                this.created_by = jsonObject.getString("created_by");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if(jsonObject.has("created_at")){
            try {
                this.created_at = jsonObject.getString("created_at");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(jsonObject.has("status")){
            try {
                this.status = jsonObject.getString("status");
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
    }
}
