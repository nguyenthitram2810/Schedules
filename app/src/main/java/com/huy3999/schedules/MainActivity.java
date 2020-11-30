package com.huy3999.schedules;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.huy3999.dragboardview.DragBoardView;
import com.huy3999.dragboardview.model.DragColumn;
import com.huy3999.dragboardview.model.DragItem;
import com.huy3999.dragboardview.utils.AttrAboutPhone;
import com.huy3999.schedules.adapter.ColumnAdapter;
import com.huy3999.schedules.model.Entry;
import com.huy3999.schedules.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ColumnAdapter mAdapter;
    DragBoardView dragBoardView;
    private List<DragColumn> mData = new ArrayList<>();
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
//    Button btnE;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        checkLogin();
//        testExit();

        //Create a new toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);

        //Add Button Navigation Drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        dragBoardView = findViewById(R.id.layout_main);
        mAdapter = new ColumnAdapter(this);
        mAdapter.setData(mData);
        dragBoardView.setHorizontalAdapter(mAdapter);
        getDataAndRefreshView();

        navigationView.setNavigationItemSelectedListener(this);
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

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void mapping() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

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

    private void getDataAndRefreshView() {
        for (int i = 0; i < 3; i++) {
            List<DragItem> itemList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                itemList.add(new Item("entry " + i + " item id " + j, "item name " + j, "info " + j));
            }
            //mData.add(new Entry("entry id " + i, "name " + i, itemList));
            mData.add(new Entry("entry 0","Todo",itemList));
            mData.add(new Entry("entry 1","Doing",itemList));
            mData.add(new Entry("entry 2","Done",itemList));
        }
        mAdapter.notifyDataSetChanged();
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
}