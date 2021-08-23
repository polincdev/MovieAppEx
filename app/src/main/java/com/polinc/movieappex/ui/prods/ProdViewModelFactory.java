package com.polinc.movieappex.ui.prods;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class ProdViewModelFactory implements ViewModelProvider.Factory {
    private Fragment fragment;


    public ProdViewModelFactory(Fragment fragment) {
        this.fragment = fragment;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ProdViewModel(fragment );
    }
}