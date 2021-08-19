package com.polinc.movieappex.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.polinc.movieappex.databinding.GoogleFragmentBinding;
import com.polinc.movieappex.databinding.GuestFragmentBinding;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.net.MoviesGetController;

import io.reactivex.rxjava3.core.Observable;


public class GoogleFragment extends Fragment {


      Button guestButton;
    public static GoogleFragment newInstance() {
         return new GoogleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.main_fragment, container, false);

        GoogleFragmentBinding binding= GoogleFragmentBinding.inflate(getLayoutInflater());
        View rootView =binding.getRoot();

          guestButton = binding.btnGoogleLogin;


        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("googleButton="+v );
                ((LoginActivity) getActivity()).loadingProgressBar.setVisibility(View.VISIBLE);
                String loginText=((LoginActivity) getActivity()).usernameEditText.getText().toString() ;
                String passText= ((LoginActivity) getActivity()).passwordEditText.getText().toString();

                if(loginText.isEmpty() || passText.isEmpty() ){
                    ((LoginActivity) getActivity()).onMovieFetchFailed(new Exception());
                    return;
                }

                Task<AuthResult> authTask=((MyApplication)  ((LoginActivity) getActivity()).getApplication()).mAuth.signInWithEmailAndPassword(loginText, passText);
                authTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Initialize
                        MoviesGetController moviesGetController = ((MyApplication)  ((LoginActivity) getActivity()).getApplication()).moviesGetController;
                        Observable<MoviesWraper> moviesCall =moviesGetController.getMoviesPopular(1);
                        moviesCall.subscribe( ((LoginActivity) getActivity())::onMovieFetchInitSuccess,  ((LoginActivity) getActivity())::onMovieFetchFailed);
                        // moviesCall.subscribe(System.out::println);
                    }
                });



                authTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ((LoginActivity) getActivity()).onMovieFetchFailed(e);
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}