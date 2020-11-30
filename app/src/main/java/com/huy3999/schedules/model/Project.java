package com.huy3999.schedules.model;

import java.util.ArrayList;

public class Project {
    public final int id;
    public final String name;
    public final int color;
    public final ArrayList<String> member;

    public Project(int id, String name, int color, ArrayList<String> member) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.member = member;
    }
}
