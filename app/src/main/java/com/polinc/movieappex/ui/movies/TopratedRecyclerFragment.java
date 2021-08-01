package com.polinc.movieappex.ui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.polinc.movieappex.data.InitDataSource;
import com.polinc.movieappex.databinding.TopratedRecyclerFragmentBinding;
import com.polinc.movieappex.main.Consts;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.ui.detail.DetailActivity;
import com.polinc.movieappex.ui.detail2.MovieDetailsActivity;


import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class TopratedRecyclerFragment extends Fragment {


    RecyclerView recyclerView;
    MoviesTopratedAdapter adapter;
    TopratedViewModel topratedVieModel;
    ArrayList<Movie> movies;
    public static TopratedRecyclerFragment newInstance() {
        return new TopratedRecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.main_fragment, container, false);

        TopratedRecyclerFragmentBinding binding= TopratedRecyclerFragmentBinding.inflate(getLayoutInflater());
        View rootView =binding.getRoot();


        // Lookup the recyclerview in activity layout
        recyclerView = binding.rvMoviesToprated;
        //New items animator
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        //
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        //ViewModel
        topratedVieModel =  new ViewModelProvider(getActivity(),new TopratedViewModelFactory(this.getActivity() )).get(TopratedViewModel.class);

        // Initialize
         InitDataSource initDataSource = ((MyApplication) getActivity().getApplication()).initDataSource;
        //Initial load
        System.out.println("topratedVieModel.currentPage="+topratedVieModel.currentPage);
        if(topratedVieModel.currentPage==0){
            //put initial empty data to use later
            initDataSource.initData.put(InitDataSource.MOVIES_TOPRATED,new ArrayList<Movie>());
            topratedVieModel.fetchMovies();
        }
         movies = (ArrayList<Movie>) initDataSource.initData.get(InitDataSource.MOVIES_TOPRATED);
        // Create adapter passing in the sample user data
        adapter = new MoviesTopratedAdapter(movies);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items. LinearLayoutManager, GridLayoutManager , StaggeredGridLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() ));

        // That's all!
        binding.fabMoreMovTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                topratedVieModel.fetchMovies();
            }
        });

        adapter.setOnItemClickListener(new MoviesTopratedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("MovieDetailsActivity onCreate");
               Movie selMovie = movies.get(position);
                Intent intent = new Intent(getActivity() , MovieDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(Consts.MOVIE, selMovie);
                intent.putExtras(extras);
                 startActivity(intent);

            }
        });

        adapter.setOnButtonClickListener(new MoviesTopratedAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(View view, int position) {
                Movie selMovie = movies.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(Consts.MOVIE, selMovie);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });
        System.out.println("0="+movies.size());
         //ViewModel and livedata
        LiveData<ArrayList<Movie>> moviesData=topratedVieModel.getMoviesData();
        LiveData<Integer> resultStatus=topratedVieModel.getResultStatus();

        System.out.println("1="+movies.size()+" "+moviesData.hasActiveObservers());

        //Subsciption
        moviesData.removeObservers(getViewLifecycleOwner());
        moviesData.observe(getViewLifecycleOwner(),new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies == null)
                    return;
                 System.out.println("observe="+movies.size());
                 onMovieFetchAddSuccess(  movies);
            }
        });
        System.out.println("2="+movies.size());
        //
        resultStatus.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer resultStatus) {
                if (resultStatus == null)
                    return;

                onMovieFetchFailed( );
            }
        });

        System.out.println("4="+movies.size());
        return rootView;
    }
    static int rate=0;

    void onMovieFetchAddSuccess(  ArrayList<Movie> newItems)
    {

        //
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //set order for search
                for(int a=0;a<movies.size();a++)
                    movies.get(a).order=a;


                 //
                int curSize = adapter.getItemCount();
                movies.addAll(newItems);
                adapter.notifyItemRangeInserted(curSize, newItems.size());
                System.out.println("runOnUiThread=" );
                ((MoviesActivity)getActivity()).loadingProgressBar.setVisibility(View.GONE);
                Snackbar.make(recyclerView,"Added "+movies.size(),2000 ).show();

            }
        });
    }

    private void onMovieFetchFailed( ) {


        //
        getActivity(). runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ( (MoviesActivity)getActivity()).loadingProgressBar.setVisibility(View.GONE);
                Snackbar.make(((MoviesActivity)getActivity()).loadingProgressBar,"Error",2000 ).show();
            }
        });
    }



}