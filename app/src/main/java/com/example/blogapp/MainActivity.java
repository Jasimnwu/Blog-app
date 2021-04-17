package com.example.blogapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity  {
    private final static int TIME = 5000;
    private Animation topanim,btmanim;
    private ImageView wclimg,phimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        topanim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.flash_top);
        btmanim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.flash_bottom);
        wclimg = findViewById(R.id.wlc);
        phimg = findViewById(R.id.photoblog);
        wclimg.setAnimation(topanim);
        phimg.setAnimation(btmanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },TIME);
    }


}