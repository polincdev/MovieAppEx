package com.polinc.movieappex.ui.dash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.polinc.movieappex.R;

import dagger.android.AndroidInjection;


public class DashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
    }
}