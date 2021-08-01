package com.polinc.movieappex.ui.search;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.polinc.movieappex.data.InitDataSource;
import com.polinc.movieappex.databinding.PopularRecyclerFragmentBinding;
import com.polinc.movieappex.databinding.SearchRecyclerFragmentBinding;
import com.polinc.movieappex.main.Consts;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.ui.detail.DetailActivity;
import com.polinc.movieappex.ui.detail2.MovieDetailsActivity;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class SearchRecyclerFragment extends Fragment {

    MoviesGetController moviesGetController ;

    public ArrayList<Movie> movies;

    RecyclerView recyclerView;
    public MoviesSearchAdapter adapter;
    int currentPage=1;
    public SearchRecyclerFragmentBinding binding;
    public static SearchRecyclerFragment newInstance() {
        return new SearchRecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.main_fragment, container, false);

          binding= SearchRecyclerFragmentBinding.inflate(getLayoutInflater());
        View rootView =binding.getRoot();

        //
        moviesGetController =  ((MyApplication) getActivity().getApplication()).moviesGetController;


        // Lookup the recyclerview in activity layout
        recyclerView = binding.rvMoviesPopular;
        //New items animator
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        //
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        // Initialize with data selected in MovieActivity
        InitDataSource initDataSource = ((MyApplication) getActivity().getApplication()).initDataSource;
        movies = (ArrayList<Movie>) initDataSource.initData.get(InitDataSource.currentMovieList);
        // Create adapter passing in the sample user data
        adapter = new MoviesSearchAdapter(movies);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items. LinearLayoutManager, GridLayoutManager , StaggeredGridLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() ));



        adapter.setOnItemClickListener(new MoviesSearchAdapter.OnItemClickListener() {
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

        adapter.setOnButtonClickListener(new MoviesSearchAdapter.OnButtonClickListener() {
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

        adapter.setOnImageClickListener(new MoviesSearchAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(View view, int position) {
                Movie selMovie = movies.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(Consts.MOVIE, selMovie);
                 intent.putExtras(extras);
                intent.putExtra(Consts.IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(view));

                //Shared animation

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view,  ViewCompat.getTransitionName(view));
                // start the new activity
                startActivity(intent, options.toBundle());



            }
        });

        return rootView;
    }




}