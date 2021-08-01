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
import com.polinc.movieappex.databinding.UpcomingRecyclerFragmentBinding;
import com.polinc.movieappex.main.Consts;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.ui.detail.DetailActivity;
import com.polinc.movieappex.ui.detail2.MovieDetailsActivity;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class UpcomingRecyclerFragment extends Fragment {


    RecyclerView recyclerView;
    MoviesUpcomingAdapter adapter;
    UpcomingViewModel UpcomingViewModel;
    ArrayList<Movie> movies;
    public static UpcomingRecyclerFragment newInstance() {
        return new UpcomingRecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.main_fragment, container, false);

        UpcomingRecyclerFragmentBinding binding= UpcomingRecyclerFragmentBinding.inflate(getLayoutInflater());
        View rootView =binding.getRoot();


        // Lookup the recyclerview in activity layout
        recyclerView = binding.rvMoviesUpcoming;
        //New items animator
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        //
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        //ViewModel
        UpcomingViewModel =  new ViewModelProvider(getActivity(),new UpcomingViewModelFactory(this.getActivity() )).get(UpcomingViewModel.class);

        // Initialize
        InitDataSource initDataSource = ((MyApplication) getActivity().getApplication()).initDataSource;
        //Initial load
        System.out.println("upcomingVieModel.currentPage="+UpcomingViewModel.currentPage);
        if(UpcomingViewModel.currentPage==0){
            //put initial empty data to use later
            initDataSource.initData.put(InitDataSource.MOVIES_UPCOMING,new ArrayList<Movie>());
            UpcomingViewModel.fetchMovies();
        }
        movies = (ArrayList<Movie>) initDataSource.initData.get(InitDataSource.MOVIES_UPCOMING);

        // Create adapter passing in the sample user data
        adapter = new MoviesUpcomingAdapter(movies);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items. LinearLayoutManager, GridLayoutManager , StaggeredGridLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() ));

        // That's all!
        binding.fabMoreMovTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpcomingViewModel.fetchMovies();
            }
        });

        adapter.setOnItemClickListener(new MoviesUpcomingAdapter.OnItemClickListener() {
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

        adapter.setOnButtonClickListener(new MoviesUpcomingAdapter.OnButtonClickListener() {
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

         //ViewModel and livedata
        LiveData<ArrayList<Movie>> moviesData=UpcomingViewModel.getMoviesData();
        LiveData<Integer> resultStatus=UpcomingViewModel.getResultStatus();

//System.out.println("Pattern="+currentPage);
        //Subsciption
        moviesData.removeObservers(getViewLifecycleOwner());
        moviesData.observe(getViewLifecycleOwner(),new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies == null)
                    return;

                onMovieFetchAddSuccess(  movies);
            }
        });

        //
        resultStatus.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer resultStatus) {
                if (resultStatus == null)
                    return;
                onMovieFetchFailed( );
            }
        });


        return rootView;
    }


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