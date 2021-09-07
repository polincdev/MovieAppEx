package com.polinc.movieappex.ui.login;

import com.polinc.movieappex.data.LoginDataSourceInterface;
import com.polinc.movieappex.data.Result;
import com.polinc.movieappex.data.model.LoggedInUser;

public class FakeLoginDataSourceImpl implements LoginDataSourceInterface {
    @Override
    public Result<LoggedInUser> login(String username, String password) {
        return null;
    }

    @Override
    public void logout() {

    }
}
