package com.polinc.movieappex.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.polinc.movieappex.data.LoginDataSourceImpl;
import com.polinc.movieappex.data.LoginRepositoryImpl;

import javax.inject.Inject;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    public LoginViewModelFactory(){}

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepositoryImpl.getInstance(new LoginDataSourceImpl()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}