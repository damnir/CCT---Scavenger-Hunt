package com.example.scavengerhunt.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    //Login fields
    private EditText emailField;
    private EditText passwordField;
    private LinearLayout loginLayout;

    //Register fields
    private EditText emailFieldR;
    private EditText passwordFieldR;
    private EditText passwordFieldR2;
    LinearLayout registerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);

        emailFieldR = findViewById(R.id.username2);
        passwordFieldR = findViewById(R.id.password2);
        passwordFieldR2 = findViewById(R.id.password3);

        loginLayout = findViewById(R.id.login_layout);
        registerLayout = findViewById(R.id.register_layout);
    }

    public void onLoginCLick(View v){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        firebaseLogin(email, password);
    }

    public void onRegisterClick(View v) {
        String email = emailFieldR.getText().toString();
        String password = passwordFieldR.getText().toString();

        firebaseRegister(email, password);
    }

    private void firebaseRegister(@NonNull String email, @NonNull String password) {
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            newUser(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void firebaseLogin(String email, String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSignUpClick(View v) {
        loginLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
    }

    public void onBackClick(View v) {
        loginLayout.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.GONE);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            Toast.makeText(LoginActivity.this, "Welcome " + user.getEmail(),
                    Toast.LENGTH_SHORT).show();

            User fUser = User.getInstance();
            fUser.setUser(user);
        }

        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }

    private void newUser(FirebaseUser user) {
        User fUser = User.getInstance();
        fUser.setUser(user);

        Database database = new Database();
        database.writeNewUser(fUser);

    }

}