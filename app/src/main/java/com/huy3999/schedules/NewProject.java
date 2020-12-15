package com.huy3999.schedules;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.huy3999.schedules.adapter.CollaboratorsAdapter;
import com.huy3999.schedules.apiservice.BaseApiService;
import com.huy3999.schedules.apiservice.UtilsApi;
import com.huy3999.schedules.model.CreateProjectInfo;
import com.huy3999.schedules.model.Project;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import petrov.kristiyan.colorpicker.ColorPicker;

public class NewProject extends AppCompatActivity {
    private TextView color_project;
    private EditText txt_name;
    private String color_choosed = "";
    private RecyclerView rv_collaborators;
    private CollaboratorsAdapter adapter;
    private ArrayList<String> arrCollaborators;
    private TextView no_collaborator;
    private BaseApiService mApiService;
    private FirebaseAuth auth;
    private String id = null;
    private String deletedCollaborator = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        mapping();

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        //Create a new toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add Button Navigation Drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_collaborators);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            deletedCollaborator = arrCollaborators.get(position);
            arrCollaborators.remove(position);
            adapter.notifyItemRemoved(position);
            no_collaborator.setText(arrCollaborators.size() + " collaborators");
            Snackbar.make(rv_collaborators, deletedCollaborator, Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            arrCollaborators.add(position,deletedCollaborator);
                            adapter.notifyItemInserted(position);
                            no_collaborator.setText(arrCollaborators.size() + " collaborators");
                        }
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(NewProject.this, R.color.tomato))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void mapping() {
        color_project = (TextView) findViewById(R.id.color_project);
        txt_name = (EditText) findViewById(R.id.txt_name);
        rv_collaborators= findViewById(R.id.list_collaborators);
        no_collaborator = findViewById(R.id.no_collaborator);
        mApiService = UtilsApi.getAPIService();
        if(getIntent().getSerializableExtra("id") != null) {
            id = getIntent().getSerializableExtra("id").toString();
            getProject(id);
        }

        rv_collaborators.setLayoutManager(new LinearLayoutManager(this));
        arrCollaborators = new ArrayList<>();
        adapter = new CollaboratorsAdapter(arrCollaborators, this);
        rv_collaborators.setAdapter(adapter);

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
                if(txt_name.getText().toString().trim().length() != 0 && color_choosed != "") {
                    if(!arrCollaborators.contains(auth.getCurrentUser().getEmail())) {
                        arrCollaborators.add(auth.getCurrentUser().getEmail());
                    }
                    CreateProjectInfo project = new CreateProjectInfo(txt_name.getText().toString().trim(), color_choosed, arrCollaborators);
                    if(id != null) {
                        updateProject(project);
                    }
                    else {
                        createProject(project);
                    }
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
        colors.add("#ffffff");
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
                        color_choosed = colors.get(position);
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
                    no_collaborator.setText("0 collaborators");
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void getProject(String id) {
        mApiService.getProject(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Project>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Project project) {
                        txt_name.setText(project.name);
                        arrCollaborators.addAll(project.member);
                        no_collaborator.setText(arrCollaborators.size() + " collaborators");
                        color_choosed = project.color;
                        color_project.setText("");
                        color_project.setBackgroundColor(Color.parseColor(color_choosed));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("DEBUGE", "Error");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void createProject(CreateProjectInfo project) {
        final Intent data = new Intent();
        mApiService.createProject(project)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("DEBUGADDSC", "subcrie");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("DEBUGADDSC", "OK");
                        Toast.makeText(NewProject.this, "Create success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("DEBUGADDSC", "ERROR");
                        Toast.makeText(NewProject.this, "Create fail", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("DEBUGADDSC", "COMPLETE");

                    }
                });
    }

    public void updateProject(CreateProjectInfo project) {
        final Intent intent = new Intent(this, MainActivity.class);
        mApiService.updateProject(id, project)
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
                        startActivity(intent);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}