package com.techamove.view;

import android.graphics.drawable.Drawable;

public class BaseModel {
    String name;
    Drawable icon;


    public BaseModel(String name,Drawable icon) {
        this.name = name;
        this.icon =icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
