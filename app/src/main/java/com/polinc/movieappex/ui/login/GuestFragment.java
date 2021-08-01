package com.polinc.movieappex.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.polinc.movieappex.databinding.FragmentMovieDetailsBinding;
import com.polinc.movieappex.databinding.GuestFragmentBinding;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.net.MoviesGetController;

import io.reactivex.rxjava3.core.Observable;


public class GuestFragment extends Fragment {


      Button guestButton;
    public static GuestFragment newInstance() {
         return new GuestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.main_fragment, container, false);

        GuestFragmentBinding binding=GuestFragmentBinding.inflate(getLayoutInflater());
        View rootView =binding.getRoot();

          guestButton = binding.btnGuestLogin;


        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("guestButton="+v );
                ((LoginActivity) getActivity()).loadingProgressBar.setVisibility(View.VISIBLE);
                // Initialize
                MoviesGetController moviesGetController = ((MyApplication)  ((LoginActivity) getActivity()).getApplication()).moviesGetController;
                Observable<MoviesWraper> moviesCall =moviesGetController.getMoviesPopular(1);
                moviesCall.subscribe( ((LoginActivity) getActivity())::onMovieFetchInitSuccess,  ((LoginActivity) getActivity())::onMovieFetchFailed);
               // moviesCall.subscribe(System.out::println);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}