package com.huy3999.schedules.model;

import com.google.gson.annotations.SerializedName;
import com.huy3999.schedules.dragboardview.model.DragItem;

import java.util.ArrayList;

public class Item implements DragItem {
    @SerializedName("id")
    public final String id;
    @SerializedName("name")
    public final String name;
    @SerializedName("description")
    public final String description;
    @SerializedName("state")
    public final String state;
    @SerializedName("project_id")
    public final String project_id;
    @SerializedName("member")
    public final ArrayList<String> member;


    public Item(String id, String name, String description, String state, String project_id, ArrayList<String> member) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        this.project_id = project_id;
        this.member = member;
    }

    @Override
    public int getColumnIndex() {
        return 0;
    }

    @Override
    public int getItemIndex() {
        return 0;
    }

    @Override
    public void setColumnIndex(int columnIndexInHorizontalRecycleView) {

    }

    @Override
    public void setItemIndex(int itemIndexInVerticalRecycleView) {

    }
}


