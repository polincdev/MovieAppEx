package com.polinc.movieappex.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.polinc.movieappex.databinding.GuestFragmentBinding;
import com.polinc.movieappex.databinding.TmdbFragmentBinding;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.net.MoviesGetController;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;


public class TMDBFragment extends Fragment {


      Button loginButton;
    public static TMDBFragment newInstance() {
        return new TMDBFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.main_fragment, container, false);

        TmdbFragmentBinding binding=TmdbFragmentBinding.inflate(getLayoutInflater());
        View rootView =binding.getRoot();

        loginButton = binding.login;

        // TODO: Use the ViewModel
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("loginButton="+v );
                ((LoginActivity) getActivity()).loadingProgressBar.setVisibility(View.VISIBLE);

                ((LoginActivity) getActivity()).loginViewModel.login(((LoginActivity) getActivity()).usernameEditText.getText().toString(),
                ((LoginActivity) getActivity()).passwordEditText.getText().toString());

                // Initialize
                MoviesGetController moviesGetController = ((MyApplication)  ((LoginActivity) getActivity()).getApplication()).moviesGetController;
                Observable<MoviesWraper> moviesCall =moviesGetController.getMoviesPopular(1);
               moviesCall.subscribe( ((LoginActivity) getActivity())::onMovieFetchInitSuccess,  ((LoginActivity) getActivity())::onMovieFetchFailed);
             //   moviesCall.subscribe( TMDBFragment::test,  ((LoginActivity) getActivity())::onMovieFetchFailed);

            }
        });

        return rootView;
    }

   static void test(MoviesWraper moviesWraper ) {

        //
         System.out.println("RESULTAA=" + moviesWraper);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }

}