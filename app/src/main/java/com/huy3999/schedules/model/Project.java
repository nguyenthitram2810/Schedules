package com.huy3999.schedules.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {
    @SerializedName("id")
    public final String id;
    @SerializedName("name")
    public final String name;
    @SerializedName("color")
    public final String color;
    @SerializedName("member")
    public final ArrayList<String> member;

    public Project(String id, String name, String color, ArrayList<String> member) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.member = member;
    }
}
