package com.pb.criconet.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class EmojiModel implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String name;
    private String url;

    public EmojiModel(JSONObject jsonObject){
        if(jsonObject.has("name")){
            try {
                this.name = jsonObject.getString("name");
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
    }
}
