package com.example.user.snakegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends AppCompatActivity {

    SnakeDemo sd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        DisplayMetrics dm=getResources().getDisplayMetrics();
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        sd=new SnakeDemo(this,width,height);
        setContentView(sd);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        sd.resume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        sd.pause();
    }


 }
