package com.polinc.movieappex.ui.movies;

import android.app.Activity;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TopratedViewModelFactory implements ViewModelProvider.Factory {
    private Activity activity;


    public TopratedViewModelFactory(Activity activity) {
        this.activity = activity;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new TopratedViewModel(activity );
    }
}