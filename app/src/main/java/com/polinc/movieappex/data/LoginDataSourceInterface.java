package com.polinc.movieappex.data;

import com.google.firebase.auth.FirebaseAuth;
import com.polinc.movieappex.data.model.LoggedInUser;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public interface LoginDataSourceInterface {
 
    public Result<LoggedInUser> login(String username, String password)  ;

    public void logout() ;
}