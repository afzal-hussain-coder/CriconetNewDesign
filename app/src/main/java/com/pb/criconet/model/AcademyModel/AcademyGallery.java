package com.pb.criconet.model.AcademyModel;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AcademyGallery implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getFiles() {
        return files;
    }

    public void setFiles(Uri files) {
        this.files = files;
    }

    private String id="";
    private Uri files;

    public AcademyGallery(JSONObject jsonObject){
        if(jsonObject.has("id")){
            try {
                this.id= jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObject.has("files")){
            try {
                this.files= Uri.parse(jsonObject.getString("files"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
