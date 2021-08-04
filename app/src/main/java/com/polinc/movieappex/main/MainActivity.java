package com.polinc.movieappex.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
  
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.polinc.movieappex.ui.intro.IntroSlider;

import java.util.ArrayList;
import java.util.List;
  
public class MainActivity extends AppCompatActivity {
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
  
        Intent i =new Intent(getApplicationContext(), IntroSlider.class);
        startActivity(i);
  
    }
}