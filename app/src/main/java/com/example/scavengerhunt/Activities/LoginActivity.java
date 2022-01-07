package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scavengerhunt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
    }

    public void onLoginCLick(View v){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        firebaseLogin(email, password);
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

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            Toast.makeText(LoginActivity.this, "Welcome " + user.getEmail(),
                    Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }
}