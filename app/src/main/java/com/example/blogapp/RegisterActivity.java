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

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText cemail,pass,cpass;
    private Button acclogin,alacc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        findallid();
        acclogin.setOnClickListener(this);
        alacc.setOnClickListener(this);
    }

    private void findallid() {
        cemail = findViewById(R.id.loginemail);
        pass = findViewById(R.id.loginpass);
        cpass = findViewById(R.id.cloginpass);
        acclogin = findViewById(R.id.crateacc);
        alacc = findViewById(R.id.loginbtn);
    }

    @Override
    public void onClick(View v) {
        if ( v.getId() == R.id.crateacc ) {
            String emal = cemail.getText().toString().trim();
            String apass = pass.getText().toString().trim();
            String acpass = cpass.getText().toString().trim();
            if (TextUtils.isEmpty(emal)) {
                cemail.setError("enter email address");
            }
            else if (TextUtils.isEmpty(apass)) {
                pass.setError("enter password");
            }
            else if ( TextUtils.isEmpty(acpass) ) {
                cpass.setError("enter confirm password");
            }
            else if ( !apass.matches(acpass) ) {
                Toast.makeText(RegisterActivity.this,"Password and confirm password",Toast.LENGTH_SHORT).show();
            }
            else {
                new SpotsDialog.Builder()
                        .setContext(RegisterActivity.this)
                        .build()
                        .show();
                mAuth.createUserWithEmailAndPassword(emal,apass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful() ) {
                            Log.d("tag", "createUserWithEmail:success");
                            Intent intent = new Intent(RegisterActivity.this,SettingActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.w("tag", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        new SpotsDialog.Builder()
                                .setContext(RegisterActivity.this)
                                .build()
                                .dismiss();
                    }
                });

            }
        }
        if (v.getId() == R.id.loginbtn) {
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}