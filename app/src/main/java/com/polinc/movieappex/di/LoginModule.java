package com.polinc.movieappex.di;



import com.polinc.movieappex.ui.login.LoginActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
@InstallIn(SingletonComponent.class)
public abstract class LoginModule {

}
