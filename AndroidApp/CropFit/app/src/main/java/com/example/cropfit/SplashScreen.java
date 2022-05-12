package com.example.cropfit;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Thread.sleep(1200);
        } catch(InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
        }
        startActivity(new Intent(this, MainActivity.class));
    }
}
