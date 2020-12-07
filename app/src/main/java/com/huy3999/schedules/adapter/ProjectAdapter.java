package com.huy3999.schedules.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huy3999.schedules.MainActivity;
import com.huy3999.schedules.NewProject;
import com.huy3999.schedules.R;
import com.huy3999.schedules.apiservice.BaseApiService;
import com.huy3999.schedules.apiservice.UtilsApi;
import com.huy3999.schedules.model.Project;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {
    private static ArrayList<Project> arrProjects;
    private static Context mContext;
    private BaseApiService mApiService;

    public ProjectAdapter(ArrayList<Project> arrProjects, Context mContext) {
        this.arrProjects = arrProjects;
        this.mContext = mContext;
        mApiService = UtilsApi.getAPIService();
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
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.option);
                popupMenu.inflate(R.menu.menu_project);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.view:
                                Toast.makeText(mContext, "view", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.edit:
                                Intent intent = new Intent(mContext, NewProject.class);
                                intent.putExtra("id", arrProjects.get(position).id);
                                mContext.startActivity(intent);
                                break;
                            case R.id.delete:
                                mApiService.deleteProject(arrProjects.get(position).id)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<String>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onNext(String s) {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                arrProjects.remove(position);
                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });
                                Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
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
        public ImageView option;

        public MyViewHolder(View view) {
            super(view);
            view = view;
            tvName = view.findViewById(R.id.tv_name);
            tvCollaborators = view.findViewById(R.id.tv_collaborators);
            itemProject = view.findViewById(R.id.item_project);
            option = view.findViewById(R.id.option);
        }
    }
}
