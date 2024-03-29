package com.polinc.movieappex.data;

import com.polinc.movieappex.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

public class LoginRepositoryImpl implements LoginRepositoryInterface {

    private static volatile LoginRepositoryImpl instance;

    private LoginDataSourceInterface dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    public LoginRepositoryImpl(LoginDataSourceInterface dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepositoryImpl getInstance(LoginDataSourceImpl dataSource) {
        if (instance == null) {
            instance = new LoginRepositoryImpl(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}