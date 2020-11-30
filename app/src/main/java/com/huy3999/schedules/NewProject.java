package com.huy3999.schedules;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.huy3999.dragboardview.utils.AttrAboutPhone;
import com.huy3999.schedules.adapter.CollaboratorsAdapter;
import com.huy3999.schedules.adapter.ProjectAdapter;
import com.huy3999.schedules.model.Project;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class NewProject extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView color_project;
    private EditText txt_name;
    private int color_choosed;
    private RecyclerView rv_collaborators;
    private CollaboratorsAdapter adapter;
    private ArrayList<String> arrCollaborators;
    private TextView no_collaborator;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    public static String Name = "Name";
    public static String ColorProject = "Color";
    public static String ListUser = "ListUser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        mapping();

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
    }

    private void mapping() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        color_project = (TextView) findViewById(R.id.color_project);
        txt_name = (EditText) findViewById(R.id.txt_name);
        rv_collaborators= findViewById(R.id.list_collaborators);
        no_collaborator = findViewById(R.id.no_collaborator);

        rv_collaborators.setLayoutManager(new LinearLayoutManager(this));
        arrCollaborators = new ArrayList<>();
        adapter = new CollaboratorsAdapter(arrCollaborators, this);
        rv_collaborators.setAdapter(adapter);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        AttrAboutPhone.saveAttr(this);
        AttrAboutPhone.initScreen(this);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_create:
                if(txt_name.getText().toString().trim().length() != 0 && color_choosed != 0) {
                    final Intent data = new Intent();
                    data.putExtra(Name, txt_name.getText().toString());
                    data.putExtra(ColorProject, color_choosed);
                    data.putExtra(ListUser, arrCollaborators);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
                else {
                    Toast.makeText(this, "Not full information", Toast.LENGTH_SHORT).show();
//                    CustomToast.makeText(NewProject.this, "Error", CustomToast.LONG, CustomToast.SUCCESS,true).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onChooseColor(View view) {
        openColorPicker();
    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#258174");
        colors.add("#3C8D2F");
        colors.add("#20724f");
        colors.add("#6a3ab2");
        colors.add("#323299");
        colors.add("#800080");
        colors.add("#b79716");
        colors.add("#966d37");
        colors.add("#b77231");
        colors.add("#808000");

        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        if(color == 0) {
                            color_project.setText("No color");
                        }
                        else {
                            color_project.setText("");
                        }
                        color_project.setBackgroundColor(color);
                        color_choosed = color;
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }

    public void onChooseCollaborators(View view) {
        openDialogAdd(Gravity.CENTER);
    }

    public void openDialogAdd(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add);
        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = gravity;
        window.setAttributes(windowAtrributes);
        dialog.setCancelable(false);
        EditText add_colla = dialog.findViewById(R.id.add_email);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnAdd = dialog.findViewById(R.id.btn_add);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewProject.this, add_colla.getText().toString(), Toast.LENGTH_SHORT).show();
                arrCollaborators.add(add_colla.getText().toString());
                if(arrCollaborators.size() > 0) {
                    no_collaborator.setText(arrCollaborators.size() + " collaborators");
                }
                else {
                    no_collaborator.setText("No collaborators");
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}