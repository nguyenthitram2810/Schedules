
package com.huy3999.schedules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.huy3999.schedules.dragboardview.utils.AttrAboutPhone;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity{
    private FirebaseAuth auth;
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private TextView navHeaderUsername;
    private View header;
    private String TAG = "oke";
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        checkLogin();
        drawer = findViewById(R.id.drawer_layout);

        //Information on header of DrawerLayout
        navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        navHeaderUsername = header.findViewById(R.id.nav_header_username);
        ImageView imageView = header.findViewById(R.id.nav_header_imageView);
        Log.d(TAG, "onCreate: " + auth.getCurrentUser().getPhotoUrl());
        Picasso.with(this).load(auth.getCurrentUser().getPhotoUrl().toString()).into(imageView);
        navHeaderUsername.setText(auth.getCurrentUser().getDisplayName());

        //Click on One item on DrawerNavigation
        Menu menuDrawer = navigationView.getMenu();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkLogin() {
        if(auth.getCurrentUser() == null){
            //User signed in
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        AttrAboutPhone.saveAttr(this);
        AttrAboutPhone.initScreen(this);
        super.onWindowFocusChanged(hasFocus);
    }

}