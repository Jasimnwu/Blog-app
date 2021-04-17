package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Placeholder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar settingbar;
    private CircleImageView cimge;
    public Uri imageuri = null;
    private EditText uname;
    private Button setp_btn;
    private StorageReference storageRef;
    private FirebaseAuth mauth;
    private FirebaseFirestore db;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingbar = findViewById(R.id.settoolbar);
        setSupportActionBar(settingbar);
        getSupportActionBar().setTitle("photo Blog");
        cimge = findViewById(R.id.profile_image);
        uname = findViewById(R.id.username);
        setp_btn = findViewById(R.id.probtn);
        storageRef = FirebaseStorage.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cimge.setOnClickListener(this);
        setp_btn.setOnClickListener(this);
        userid = mauth.getCurrentUser().getUid();
        db.collection("user_profile").document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String name = task.getResult().getString("name");
                                String image = task.getResult().getString("image");
                                imageuri = Uri.parse(image);
                                uname.setText(name);
                                Glide.with(SettingActivity.this).load(image).into(cimge);
                            }
                            else {
                                Log.d("tag","Error getting documents",task.getException());
                            }
                        }
                        else {
                            Log.d("tag","Error getting documents",task.getException());
                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if ( v.getId() == R.id.profile_image ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                }
            }
        }
        if ( v.getId() == R.id.probtn ) {
           String user_name = uname.getText().toString().trim();
           if ( !TextUtils.isEmpty(user_name) && imageuri != null ) {
               userid = mauth.getCurrentUser().getUid();
                new SpotsDialog.Builder()
                        .setContext(SettingActivity.this)
                        .build()
                        .show();
                StorageReference img_path = storageRef.child("profile_image").child(userid+".jpg");
                img_path.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if ( task.isSuccessful() ) {
                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                            Map<String,String> user = new HashMap<>();
                            user.put("name", user_name);
                            user.put("image",downloadUrl);
                            db.collection("user_profile").document(userid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingActivity.this,"firestone upload imgae",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SettingActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SettingActivity.this,"Error:"+error,Toast.LENGTH_SHORT);
                                    }
                                    new SpotsDialog.Builder()
                                            .setContext(SettingActivity.this)
                                            .build()
                                            .dismiss();
                                }
                            });
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(SettingActivity.this,"Error:"+error,Toast.LENGTH_SHORT);
                            new SpotsDialog.Builder()
                                    .setContext(SettingActivity.this)
                                    .build()
                                    .dismiss();
                        }

                    }
                });
           }
           else {
               Toast.makeText(SettingActivity.this,"insert name or image",Toast.LENGTH_SHORT).show();
           }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();
                cimge.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}