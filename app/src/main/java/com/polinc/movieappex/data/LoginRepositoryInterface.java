package com.polinc.movieappex.data;

import com.polinc.movieappex.data.model.LoggedInUser;

public interface LoginRepositoryInterface {

    public boolean isLoggedIn();

    public void logout();

    public Result<LoggedInUser> login(String username, String password) ;

}
