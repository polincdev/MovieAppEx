package com.polinc.movieappex.di;



import com.polinc.movieappex.data.LoginDataSourceImpl;
import com.polinc.movieappex.data.LoginRepositoryImpl;
import com.polinc.movieappex.data.LoginRepositoryInterface;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public  class LoginModule {

    @Provides
    LoginDataSourceImpl provideLoginDataSource(  ) {
        return new LoginDataSourceImpl();
    }
    @Provides
    LoginRepositoryInterface provideLoginRepository(LoginDataSourceImpl loginDataSource  ) {
         return new LoginRepositoryImpl(loginDataSource);
    }
}
