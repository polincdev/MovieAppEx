package com.polinc.movieappex.ui.prods;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.polinc.movieappex.R;
import com.polinc.movieappex.databinding.PopularRecyclerFragmentBinding;
import com.polinc.movieappex.databinding.ProdFragmentBinding;
import com.polinc.movieappex.ui.movies.TopratedViewModel;
import com.polinc.movieappex.ui.movies.TopratedViewModelFactory;

import java.util.ArrayList;


public class ProdFragment extends Fragment {


    public FirebaseStorage storage;
    public StorageReference storageRef;

    ProdFragmentBinding binding;
    GridView simpleGrid;
    ProdViewModel prodViewModel;
    CustomAdapter customAdapter;


    public static ProdFragment newInstance() {
        return new ProdFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding= ProdFragmentBinding.inflate(getLayoutInflater());
        View rootView =binding.getRoot();

        //Init Firestora
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        //ViewModel
        prodViewModel =  new ViewModelProvider(getActivity(),new ProdViewModelFactory(this )).get(ProdViewModel.class);


        //Init empty
        simpleGrid = binding.prodGridView; // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
          customAdapter = new CustomAdapter(getActivity() );
        simpleGrid.setAdapter(customAdapter);
        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity

            }
        });


        //gObserve downlaod
        prodViewModel.getImageData().subscribe( bitmaps ->   customAdapter.addItem(bitmaps) );


        //Fetch once
        prodViewModel.fetchImages();



        return rootView;
    }


}