package com.pb.criconetnewdesign.model;

import java.io.Serializable;

public class AmbassadorModel implements Serializable {
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    private String text;
    private int image;

    public AmbassadorModel(String text,int image){
        this.text = text;
        this.image = image;

    }
}
