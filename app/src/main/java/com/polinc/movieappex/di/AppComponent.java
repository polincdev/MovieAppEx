package com.polinc.movieappex.di;



import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.net.NetworkModule;
import com.polinc.movieappex.ui.login.LoginActivity;


import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules={AppModule.class,NetworkModule.class,LoginModule.class,DashModule.class, MoviesPopularModule.class,DetailModule.class })
public interface AppComponent {

    MoviesGetController provideMoviesGetController( );
    void inject(MyApplication application);
    void inject(LoginActivity loginActivity);
}
