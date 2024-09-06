package com.pb.criconet.model.AcademyModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AcademySessionJoiningMemberCommon implements Serializable {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMid_name() {
        return mid_name;
    }

    public void setMid_name(String mid_name) {
        this.mid_name = mid_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String user_id="";
    private String name="";
    private String username="";
    private String first_name="";
    private String last_name="";
    private String mid_name="";
    private String avatar="";

    public AcademySessionJoiningMemberCommon(String user_id, String name,String username, String first_name,String last_name,String mid_name,String avatar) {
        this.user_id = user_id;
        this.name = name;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mid_name = mid_name;
        this.avatar = avatar;
    }

    // toString() method (optional, for easy debugging)
    @Override
    public String toString() {
        return "AcademySessionJoiningMemberCommon{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", mid_name='" + mid_name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }


}
