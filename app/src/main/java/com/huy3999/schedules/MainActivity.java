package com.huy3999.schedules;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
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
import android.view.SubMenu;
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
import com.huy3999.schedules.fragment.DragBoardFragment;
import com.huy3999.schedules.model.Project;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton btn_add_project;
    private RecyclerView rv_projects;
    private ProjectAdapter adapter;
    private ArrayList<Project> arrProjects;
    private FirebaseAuth auth;
    private BaseApiService mApiService;
    private static final int REQUEST_CODE_EXAMPLE = 0x9345;
    private AppBarConfiguration mAppBarConfiguration;
    Menu menu;
    int projectItemId = 0;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mapping();

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        checkLogin();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_settings, R.id.nav_logout)
//                .setDrawerLayout(drawer)
//                .build();

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
        mApiService = UtilsApi.getAPIService();
        arrProjects = new ArrayList<Project>();
        Log.d("project", "email " + auth.getCurrentUser().getEmail());
        getData(auth.getCurrentUser().getEmail());
        setupDrawerContent(navigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        if(menuItem.getItemId()!=R.id.nav_home||menuItem.getItemId()!=R.id.nav_settings||menuItem.getItemId()!=R.id.nav_logout) {
            Project project = arrProjects.get(menuItem.getItemId());
            try {
                DragBoardFragment dragBoardFragment = (DragBoardFragment.newInstance(project));
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, dragBoardFragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            setTitle(project.name);
            Toast.makeText(this, "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            // Close the navigation drawer
            drawer.closeDrawers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
                        arrProjects.removeAll(arrProjects);
                        for (Project project : projects) {
                            Log.d("project", "project: " + project.name);
                            arrProjects.add(project);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        //adapter.notifyDataSetChanged();
                        if (arrProjects != null) {
                            Log.d("project", "email " + auth.getCurrentUser().getEmail());
                            Log.d("project", "projects " + String.valueOf(arrProjects));
                            final SubMenu subMenu = menu.addSubMenu("Projects");
                            for (Project project : arrProjects) {
                                menu.add(0, projectItemId, 0, project.name);
                                //subMenu.add(0, projectItemId, 0, project.name);
                                projectItemId++;
                                //menu.add(project.name);
                            }
                        }
                    }
                });
    }

    private void mapping() {
        btn_add_project = findViewById(R.id.btn_add_project);
        rv_projects = findViewById(R.id.list_project);
        rv_projects.setLayoutManager(new LinearLayoutManager(this));
        mApiService = UtilsApi.getAPIService();
        arrProjects = new ArrayList<Project>();
        adapter = new ProjectAdapter(arrProjects, this);
        rv_projects.setAdapter(adapter);
    }

    private void checkLogin() {
        if (auth.getCurrentUser() == null) {
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

    public void onAddProject(View view) {
        Intent i = new Intent(MainActivity.this, NewProject.class);
        startActivityForResult(i, REQUEST_CODE_EXAMPLE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.getData(auth.getCurrentUser().getEmail());
    }
}

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        AttrAboutPhone.saveAttr(this);
//        AttrAboutPhone.initScreen(this);
//        super.onWindowFocusChanged(hasFocus);
//    }
//
//    public void onAddProject(View view) {
//        Intent i = new Intent(MainActivity.this, NewProject.class);
//        startActivityForResult(i, REQUEST_CODE_EXAMPLE);
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }


//    private void getDataAndRefreshView() {
//        for (int i = 0; i < 3; i++) {
//            List<DragItem> itemList = new ArrayList<>();
//            for (int j = 0; j < 5; j++) {
//                itemList.add(new Item("entry " + i + " item id " + j, "item name " + j, "info " + j));
//            }
//            //mData.add(new Entry("entry id " + i, "name " + i, itemList));
//            mData.add(new Entry("entry 0","Todo",itemList));
//            mData.add(new Entry("entry 1","Doing",itemList));
//            mData.add(new Entry("entry 2","Done",itemList));
//        }
//        mAdapter.notifyDataSetChanged();
//    }
//    private void testExit() {
//        btnE = findViewById(R.id.exit);
//        btnE.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                auth.signOut();
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            }
//        });
//    }