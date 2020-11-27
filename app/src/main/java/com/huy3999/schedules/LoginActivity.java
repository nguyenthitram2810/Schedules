package com.huy3999.schedules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private Button btnSignInGoogle;
    private LoginButton btnSignInFb;
    private CallbackManager mCallbackManager;
    private final String TAG = "Facebook";
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init Firebase in this app
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        checkLoginGG();
        createRequest();

        mapping();
        addEvents();
        setUpLoginFB();
        checkLogin();
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkLoginGG();
    }

    private void checkLoginGG() {
        if(auth.getCurrentUser() != null){
            //User da login
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void setUpLoginFB() {
        mCallbackManager = CallbackManager.Factory.create();
        btnSignInFb.setPermissions(Arrays.asList("email", "public_profile"));
        btnSignInFb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: " + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
                saveUserInfo(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + error);
                saveUserInfo(null);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            Log.d(TAG, "onComplete: " + firebaseUser);
                            saveUserInfo(firebaseUser);
                            navigateToPlacesActivity();
                        }else{
                            Log.d(TAG, "onComplete: fail signInWithCredential");
                            saveUserInfo(null);
                        }
                    }
                });
    }

    private void navigateToPlacesActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void saveUserInfo(FirebaseUser facebookUser){
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(facebookUser != null){
            String userId = facebookUser.getUid();
            String email = facebookUser.getEmail();
            String name = facebookUser.getDisplayName();
            Uri photoUrl = facebookUser.getPhotoUrl();
            editor.putString("userId", userId);
            editor.putString("email", email);
            editor.putString("name", name);
            editor.putString("photoUrl", String.valueOf(photoUrl));
            editor.putString("userType", "facebook");
            editor.putInt("isLoggedIn", 1);
        }else{
            editor.putString("userId", "");
            editor.putString("email", "");
            editor.putString("name", "");
            editor.putString("photoUrl", "");
            editor.putString("userType", "");
            editor.putInt("isLoggedIn", 0);
        }
        editor.commit();
    }

    private void checkLogin(){
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences.getInt("isLoggedIn", 0) == 1){
            navigateToPlacesActivity();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = (GoogleSignInAccount) task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "LOI ACTIVITY RESULT" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("API", "onActivityResult: " + e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addEvents() {
        //Form register
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void createRequest() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void mapping() {
        btnSignInGoogle = findViewById(R.id.button_login_gg);
        btnSignInFb = findViewById(R.id.button_login_fb);
    }

}