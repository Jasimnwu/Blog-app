package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mauth;
    public EditText emaillogin,loginpass;
    public Button lbtn,caccbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth = FirebaseAuth.getInstance();
        findelement();
        lbtn.setOnClickListener(this);
        caccbtn.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mauth.getCurrentUser();
        if ( currentuser != null ) {
            onactivity();
        }
    }
    // find all id here
    private void findelement() {
        emaillogin = findViewById(R.id.loginemail);
        loginpass = findViewById(R.id.loginpass);
        lbtn = findViewById(R.id.loginbtn);
        caccbtn = findViewById(R.id.crateaccoutn);
    }
    @Override
    public void onClick(View v) {
        if ( v.getId() == R.id.loginbtn ) {
            String email = emaillogin.getText().toString().trim();
            String pass = loginpass.getText().toString();
            if ( !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) ) {
                new SpotsDialog.Builder()
                        .setContext(LoginActivity.this)
                        .build()
                        .show();
                mauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("tag","signInWithEmail:success");
                            onactivity();
                        }
                        else {
                            Log.w("tag", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        new SpotsDialog.Builder()
                                .setContext(LoginActivity.this)
                                .build()
                                .dismiss();
                    }
                });
            }
        }
        if ( v.getId() == R.id.crateaccoutn ) {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
    //onactivity
    private void onactivity() {
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

}