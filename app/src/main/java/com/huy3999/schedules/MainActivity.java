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

public class MainActivity extends AppCompatActivity{

    private ColumnAdapter mAdapter;
    DragBoardView dragBoardView;
    private List<DragColumn> mData = new ArrayList<>();
    private FirebaseAuth auth;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle toggle;
//    Button btnE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mapping();
        NavigationView navigationView = findViewById(R.id.nav_view);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        checkLogin();
//        testExit();
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        //Huy
        dragBoardView = findViewById(R.id.layout_main);
        mAdapter = new ColumnAdapter(this);
        mAdapter.setData(mData);
        dragBoardView.setHorizontalAdapter(mAdapter);
        getDataAndRefreshView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
        Log.d("oke", "onOptionsItemSelected: " + item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void mapping() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
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
}