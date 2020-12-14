package com.huy3999.schedules.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.huy3999.schedules.MainActivity;
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
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;
    private FloatingActionButton btn_add_project;
    private RecyclerView rv_projects;
    private ProjectAdapter adapter;
    private ArrayList<Project> arrProjects;
    private FirebaseAuth auth;
    private BaseApiService mApiService;
    private static final int REQUEST_CODE_EXAMPLE = 0x9345;
    private AppBarConfiguration mAppBarConfiguration;
    private static View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
<<<<<<< HEAD
        root = inflater.inflate(R.layout.activity_home_fragment, container, false);
=======
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        if (root != null) {
            ViewGroup parent = (ViewGroup) root.getParent();
            if (parent != null)
                parent.removeView(root);
        }
        try {
            root = inflater.inflate(R.layout.activity_home_fragment, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        auth = FirebaseAuth.getInstance();
>>>>>>> 24b8a7a5b866f62195f569140870b24d3a06c1cf
        btn_add_project = root.findViewById(R.id.btn_add_project);
        rv_projects = root.findViewById(R.id.list_project);
        rv_projects.setLayoutManager(new LinearLayoutManager(getContext()));
        mApiService = UtilsApi.getAPIService();
        arrProjects = new ArrayList<Project>();
        adapter = new ProjectAdapter(arrProjects, getContext());
        rv_projects.setAdapter(adapter);
        getData(auth.getCurrentUser().getEmail());
        return root;
    }
    public void getData(String email) {
        mApiService.getAllProjects(email)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Project>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Project> projects) {
                        Log.d("DEBUG2", "no");
                        for(Project project : projects) {
                            arrProjects.add(project);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void onAddProject(View view) {
        Intent i = new Intent(getContext(), NewProject.class);
        startActivityForResult(i, REQUEST_CODE_EXAMPLE);
    }
}