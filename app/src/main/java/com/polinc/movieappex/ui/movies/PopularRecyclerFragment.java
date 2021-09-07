package com.polinc.movieappex.ui.movies;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.polinc.movieappex.R;
import com.polinc.movieappex.data.InitDataSource;


import com.polinc.movieappex.databinding.PopularRecyclerFragmentBinding;
import com.polinc.movieappex.main.Consts;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.ui.detail.DetailActivity;
import com.polinc.movieappex.ui.detail2.MovieDetailsActivity;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class PopularRecyclerFragment extends Fragment {

    MoviesGetController moviesGetController;

    public ArrayList<Movie> movies;

    RecyclerView recyclerView;
    public MoviesPopularAdapter adapter;
    int currentPage = 1;
    public PopularRecyclerFragmentBinding binding;

    public static PopularRecyclerFragment newInstance() {
        return new PopularRecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.main_fragment, container, false);

        binding = PopularRecyclerFragmentBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();

        //
        moviesGetController = ((MyApplication) getActivity().getApplication()).moviesGetController;

        // Lookup the recyclerview in activity layout
        recyclerView = binding.rvMoviesPopular;
        //New items animator
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        //
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        // Initialize
        InitDataSource initDataSource = ((MyApplication) getActivity().getApplication()).initDataSource;
        movies = (ArrayList<Movie>) initDataSource.initData.get(InitDataSource.MOVIES_POPULAR);
        // Create adapter passing in the sample user data
        adapter = new MoviesPopularAdapter(movies);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items. LinearLayoutManager, GridLayoutManager , StaggeredGridLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // That's all!
        binding.fabMoreMovPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchMovies();
            }
        });

        adapter.setOnItemClickListener(new MoviesPopularAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("MovieDetailsActivity onCreate");
                Movie selMovie = movies.get(position);
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(Consts.MOVIE, selMovie);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });

        adapter.setOnButtonClickListener(new MoviesPopularAdapter.OnButtonClickListener() {
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

        adapter.setOnImageClickListener(new MoviesPopularAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(View view, int position) {
                Movie selMovie = movies.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(Consts.MOVIE, selMovie);
                intent.putExtras(extras);
                intent.putExtra(Consts.IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(view));

                //Shared animation

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, ViewCompat.getTransitionName(view));
                // start the new activity
                startActivity(intent, options.toBundle());


            }
        });

        return rootView;
    }

    public void fetchMovies() {
        ((MoviesActivity) getActivity()).loadingProgressBar.setVisibility(View.VISIBLE);

        currentPage++;
        if (currentPage > 100)
            currentPage = 100;

        Observable<MoviesWraper> moviesCall = moviesGetController.getMoviesPopular(currentPage);
        moviesCall.subscribe(PopularRecyclerFragment.this::onMovieFetchAddSuccess, PopularRecyclerFragment.this::onMovieFetchFailed);

    }

    void onMovieFetchAddSuccess(MoviesWraper moviesWraper) {
        ArrayList<Movie> newItems = new ArrayList<Movie>();
        System.out.println("WYNIK=" + moviesWraper.getMovieList().size());
        moviesWraper.getMovieList().forEach(movie -> newItems.add(movie));
        //set order for search
        for (int a = 0; a < movies.size(); a++)
            movies.get(a).order = a;
        //
        int curSize = adapter.getItemCount();
        movies.addAll(newItems);
        adapter.notifyItemRangeInserted(curSize, newItems.size());

        //
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MoviesActivity) getActivity()).loadingProgressBar.setVisibility(View.GONE);
                Snackbar.make(recyclerView, "Added " + moviesWraper.getMovieList().size(), 2000).show();

            }
        });
    }

    private void onMovieFetchFailed(Throwable e) {

        e.printStackTrace();
        //
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MoviesActivity) getActivity()).loadingProgressBar.setVisibility(View.GONE);
                Snackbar.make(((MoviesActivity) getActivity()).loadingProgressBar, "Error", 2000).show();
            }
        });
    }


}