package com.huy3999.schedules.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huy3999.schedules.R;
import com.huy3999.schedules.model.Project;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {
    private static ArrayList<Project> arrProjects;
    private static Context mContext;

    public ProjectAdapter(ArrayList<Project> arrProjects, Context mContext) {
        this.arrProjects = arrProjects;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        ProjectAdapter.MyViewHolder vh = new ProjectAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(arrProjects.get(position).name);
        holder.tvCollaborators.setText(arrProjects.get(position).member.size() + " collaborators");
        holder.itemProject.setBackgroundColor(Color.parseColor(arrProjects.get(position).color));
    }

    @Override
    public int getItemCount() {
        return arrProjects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView tvName;
        public TextView tvCollaborators;
        public LinearLayout itemProject;

        public MyViewHolder(View view) {
            super(view);
            view = view;
            tvName = view.findViewById(R.id.tv_name);
            tvCollaborators = view.findViewById(R.id.tv_collaborators);
            itemProject = view.findViewById(R.id.item_project);
        }
    }
}
