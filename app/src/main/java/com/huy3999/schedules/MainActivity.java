package com.huy3999.schedules;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.huy3999.dragboardview.DragBoardView;
import com.huy3999.dragboardview.model.DragColumn;
import com.huy3999.dragboardview.model.DragItem;
import com.huy3999.dragboardview.utils.AttrAboutPhone;
import com.huy3999.schedules.adapter.ColumnAdapter;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton btn_add_project;
    private RecyclerView rv_projects;
    private ProjectAdapter adapter;
    private ArrayList<Project> arrProjects;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private BaseApiService mApiService;
    private static final int REQUEST_CODE_EXAMPLE = 0x9345;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        checkLogin();

        //Create a new toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);

        //Add Button Navigation Drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setNavigationItemSelectedListener(this);
        getData(auth.getCurrentUser().getEmail());

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
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }


    private void mapping() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        btn_add_project = findViewById(R.id.btn_add_project);
        rv_projects = findViewById(R.id.list_project);
        rv_projects.setLayoutManager(new LinearLayoutManager(this));
        mApiService = UtilsApi.getAPIService();
        arrProjects = new ArrayList<Project>();
        adapter = new ProjectAdapter(arrProjects, this);
        rv_projects.setAdapter(adapter);
    }

    private void checkLogin() {
        if(auth.getCurrentUser() == null){
            //User da login roi
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        AttrAboutPhone.saveAttr(this);
        AttrAboutPhone.initScreen(this);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("CCC", "onNavigationItemSelected: " + id);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void onAddProject(View view) {
        Intent i = new Intent(MainActivity.this, NewProject.class);
        startActivityForResult(i, REQUEST_CODE_EXAMPLE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}