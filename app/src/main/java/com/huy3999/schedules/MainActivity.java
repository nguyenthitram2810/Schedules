package com.huy3999.schedules;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
//    private Button btn_logout;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init Firebase in this app
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        checkAuth();
        //init
//        btn_logout = findViewById(R.id.button);
//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                auth.signOut();
//                checkAuth();
//            }
//        });
    }

    private void checkAuth() {
        if(auth.getCurrentUser() == null){
            //User chua login
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    public void onBackPressed() {
        //do nothing
    }
}