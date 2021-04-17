package com.example.blogapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class PostActivity extends AppCompatActivity {
    private Toolbar ptoolbar;
    private ImageView postimage;
    private EditText posttxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ptoolbar = findViewById(R.id.posttoolbar);
        setSupportActionBar(ptoolbar);
        getSupportActionBar().setTitle("New Post");
        postimage = findViewById(R.id.blogimg);
        posttxt = findViewById(R.id.posttext);
        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}