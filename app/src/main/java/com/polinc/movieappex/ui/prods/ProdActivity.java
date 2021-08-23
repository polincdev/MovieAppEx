package com.polinc.movieappex.ui.prods;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.polinc.movieappex.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProdActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prod_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProdFragment.newInstance())
                    .commitNow();
        }
    }
}