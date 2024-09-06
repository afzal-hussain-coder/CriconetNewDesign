package com.pb.criconet.model.AcademyModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AcademyRole implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole_txt() {
        return role_txt;
    }

    public void setRole_txt(String role_txt) {
        this.role_txt = role_txt;
    }

    private String id="";
    private String name="";
    private String role_txt="";

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;



    public AcademyRole(JSONObject jsonObject) throws JSONException {
        if(jsonObject.has("id")){
            this.id = jsonObject.getString("id");
        }
        if(jsonObject.has("name")){
            this.name = jsonObject.getString("name");
        }
        if(jsonObject.has("role_txt")){
            this.role_txt = jsonObject.getString("role_txt");
        }


    }
}
