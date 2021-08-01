package com.polinc.movieappex.ui.movies;

import android.app.Activity;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UpcomingViewModelFactory implements ViewModelProvider.Factory {
    private Activity activity;


    public UpcomingViewModelFactory(Activity activity) {
        this.activity = activity;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new UpcomingViewModel(activity );
    }
}