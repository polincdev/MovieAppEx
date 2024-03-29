package com.polinc.movieappex.data;

import com.google.firebase.auth.FirebaseAuth;
import com.polinc.movieappex.data.model.LoggedInUser;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSourceImpl implements LoginDataSourceInterface {

    private FirebaseAuth mAuth;

    @Inject
public LoginDataSourceImpl(){};

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}