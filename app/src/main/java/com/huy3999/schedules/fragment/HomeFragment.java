package com.huy3999.schedules.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.huy3999.schedules.NewProject;
import com.huy3999.schedules.R;
import com.huy3999.schedules.adapter.ProjectAdapter;
import com.huy3999.schedules.apiservice.BaseApiService;
import com.huy3999.schedules.apiservice.UtilsApi;
import com.huy3999.schedules.model.Project;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    private FloatingActionButton addButton;
    private RecyclerView rv_projects;
    private ProjectAdapter adapter;
    private ArrayList<Project> arrProjects;
    private FirebaseAuth auth;
    private BaseApiService mApiService;
    private static final int REQUEST_CODE_EXAMPLE = 0x9345;
    private static View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_home_fragment, container, false);
        init();
        return root;
    }

    public void init() {
        auth = FirebaseAuth.getInstance();
        addButton = root.findViewById(R.id.btn_add_project);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddProject(view);
            }
        });

        rv_projects = root.findViewById(R.id.list_project);
        rv_projects.setLayoutManager(new LinearLayoutManager(getContext()));
        mApiService = UtilsApi.getAPIService();
        arrProjects = new ArrayList<Project>();
        adapter = new ProjectAdapter(arrProjects, getContext());
        rv_projects.setAdapter(adapter);
        fetchData(auth.getCurrentUser().getEmail());
    }

    public void fetchData(String email) {
        mApiService.getAllProjects(email)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Project>>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Project> projects) {
                        arrProjects.removeAll(arrProjects);
                        for(Project project : projects) {
                            arrProjects.add(project);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onAddProject(View view) {
        Intent i = new Intent(getContext(), NewProject.class);
        startActivityForResult(i, REQUEST_CODE_EXAMPLE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.fetchData(auth.getCurrentUser().getEmail());
    }
}