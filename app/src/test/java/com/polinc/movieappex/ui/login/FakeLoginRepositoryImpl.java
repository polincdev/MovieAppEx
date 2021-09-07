package com.polinc.movieappex.ui.login;

import com.polinc.movieappex.data.LoginDataSourceImpl;
import com.polinc.movieappex.data.LoginDataSourceInterface;
import com.polinc.movieappex.data.LoginRepositoryImpl;
import com.polinc.movieappex.data.LoginRepositoryInterface;
import com.polinc.movieappex.data.Result;
import com.polinc.movieappex.data.model.LoggedInUser;

import java.io.IOException;

public class FakeLoginRepositoryImpl  implements LoginRepositoryInterface {

    LoggedInUser fakeUser =    new LoggedInUser(
                    java.util.UUID.randomUUID().toString(),
                    "Jane Doe");
    Result fakeSuccessResult= new Result.Success<>(fakeUser);
    Result fakeErrorResult=   new Result.Error(new IOException("Error logging in" ));


    private static volatile LoginRepositoryInterface instance;

    private LoginDataSourceInterface dataSource;

    // private constructor : singleton access
    public FakeLoginRepositoryImpl(LoginDataSourceInterface dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepositoryInterface getInstance(LoginDataSourceInterface dataSource) {
        if (instance == null) {
            instance = new FakeLoginRepositoryImpl(dataSource);
        }
        return instance;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void logout() {

    }

    @Override
    public Result<LoggedInUser> login(String username, String password) {
        return fakeSuccessResult;
    }
}
