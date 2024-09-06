package com.pb.criconet.model.AcademyModel;

import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AcademyTipsPreviewModel implements Serializable {
    public String getTips_id() {
        return tips_id;
    }

    public void setTips_id(String tips_id) {
        this.tips_id = tips_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(String academy_id) {
        this.academy_id = academy_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String tips_id="";
    private String category_id="";
    private String privacy="";
    private String create_at="";
    private String user_id="";
    private String academy_id="";
    private String title="";
    private String details="";
    private String video_path="";
    private String status="";
    private String cat_title="";
    private String name="";

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    private String media_type="";


    public AcademyTipsPreviewModel(JSONObject jsonObject){
         if(jsonObject.has("tips_id")){
             try {
                 this.tips_id = jsonObject.getString("tips_id");
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
        if(jsonObject.has("category_id")){
            try {
                this.category_id = jsonObject.getString("category_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("privacy")){
            try {
                this.privacy = jsonObject.getString("privacy");
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
        if(jsonObject.has("academy_id")){
            try {
                this.academy_id = jsonObject.getString("academy_id");
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
        if(jsonObject.has("details")){
            try {
                this.details = jsonObject.getString("details");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("video_path")){
            try {
                this.video_path = jsonObject.getString("video_path");
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
        if(jsonObject.has("cat_title")){
            try {
                this.cat_title = jsonObject.getString("cat_title");
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

        if(jsonObject.has("media_type")){
            try {
                this.media_type = jsonObject.getString("media_type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
