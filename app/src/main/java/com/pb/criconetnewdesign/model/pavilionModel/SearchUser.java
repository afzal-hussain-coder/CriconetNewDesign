package com.pb.criconetnewdesign.model.pavilionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SearchUser implements Serializable {
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getCover_picture() {
        return cover_picture;
    }

    public void setCover_picture(String cover_picture) {
        this.cover_picture = cover_picture;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIs_following() {
        return is_following;
    }

    public void setIs_following(String is_following) {
        this.is_following = is_following;
    }

    private String user_id="";
    private String username="";
    private String name ="";
    private String profile_picture ="";
    private String cover_picture ="";
    private String verified ="";
    private String gender ="";
    private String url ="";
    private String is_following ="";

    public SearchUser(JSONObject jsonObject){
        if(jsonObject.has("user_id")){
            try {
                this.user_id = jsonObject.getString("user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("username")){
            try {
                this.username = jsonObject.getString("username");
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
        if(jsonObject.has("profile_picture")){
            try {
                this.profile_picture = jsonObject.getString("profile_picture");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("cover_picture")){
            try {
                this.cover_picture = jsonObject.getString("cover_picture");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("verified")){
            try {
                this.verified = jsonObject.getString("verified");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("gender")){
            try {
                this.gender = jsonObject.getString("gender");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("url")){
            try {
                this.url = jsonObject.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("is_following")){
            try {
                this.is_following = jsonObject.getString("is_following");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
