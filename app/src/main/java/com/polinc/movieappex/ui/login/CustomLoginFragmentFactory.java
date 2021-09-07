package com.polinc.movieappex.ui.login;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import javax.inject.Inject;

public class CustomLoginFragmentFactory extends FragmentFactory {

    @Inject
    public CustomLoginFragmentFactory(){};

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {

        if(className.equals( GoogleFragment.class.getName()))
            return  GoogleFragment.newInstance();
        else if(className.equals( GuestFragment.class.getName()))
            return  GuestFragment.newInstance();
        else if(className.equals( TMDBFragment.class.getName()))
            return  TMDBFragment.newInstance();

        return super.instantiate(classLoader, className);
    }
}
