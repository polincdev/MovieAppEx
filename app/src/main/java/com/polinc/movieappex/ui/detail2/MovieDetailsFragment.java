package com.polinc.movieappex.ui.detail2;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.polinc.movieappex.R;
import com.polinc.movieappex.databinding.FragmentMovieDetailsBinding;
import com.polinc.movieappex.main.Api;
import com.polinc.movieappex.main.Consts;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.Review;
import com.polinc.movieappex.models.Video;
import com.polinc.movieappex.net.MoviesGetController;

import java.util.List;

import javax.inject.Inject;

 

public class MovieDetailsFragment extends Fragment implements MovieDetailsView, View.OnClickListener {

    MovieDetailsPresenter movieDetailsPresenter ;


    ImageView poster;
    CollapsingToolbarLayout collapsingToolbar;
    TextView title;
    TextView releaseDate;
    TextView rating;
    TextView overview;
    TextView label;
    LinearLayout trailers;
    HorizontalScrollView horizontalScrollView;
    TextView reviews;
    LinearLayout reviewsContainer;
    FloatingActionButton favorite;

    @Nullable
    Toolbar toolbar;

    private Movie movie;

    MoviesGetController moviesGetController;
    public MovieDetailsFragment() {

    }

    public static MovieDetailsFragment getInstance(@NonNull Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(Consts.MOVIE, movie);
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(args);
        return movieDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        moviesGetController = ((MyApplication) this.getActivity().getApplication()).moviesGetController;
        MovieDetailsInteractor movieDetailsInteractor=new MovieDetailsInteractorImpl(moviesGetController);
        movieDetailsPresenter=new MovieDetailsPresenterImpl(movieDetailsInteractor);

        FragmentMovieDetailsBinding binding=FragmentMovieDetailsBinding.inflate(getLayoutInflater());
        View rootView =binding.getRoot();

        poster=binding.moviePoster;
        collapsingToolbar=binding.collapsingToolbar;
        title=binding.movieName;
        releaseDate=binding.movieYear;
        rating=binding.movieRating;
        overview=binding.movieDescription;
        reviews=binding.reviewsBox.reviewsLabel;
        reviewsContainer=binding.reviewsBox.reviews;
        toolbar=binding.toolbar;


        setToolbar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Movie movie = (Movie) getArguments().get(Consts.MOVIE);
            if (movie != null) {
                this.movie = movie;
                movieDetailsPresenter.setView(this);
                movieDetailsPresenter.showDetails((movie));

            }
        }
    }

    private void setToolbar() {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        collapsingToolbar.setTitle(getString(R.string.movie_details));
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null) {
             ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

    @Override
    public void showDetails(Movie movie) {
        Glide.with(getContext()).load(Api.getBackdropPath(movie.getBackdropPath())).into(poster);
        title.setText(movie.getTitle());
        releaseDate.setText(String.format(getString(R.string.release_date), movie.getReleaseDate()));
        rating.setText(String.format(getString(R.string.rating), String.valueOf(movie.getVoteAverage())));
        overview.setText(movie.getOverview());

        movieDetailsPresenter.showReviews(movie);
    }


    @Override
    public void showReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            this.reviews.setVisibility(View.GONE);
            reviewsContainer.setVisibility(View.GONE);
        } else {
            this.reviews.setVisibility(View.VISIBLE);
            reviewsContainer.setVisibility(View.VISIBLE);

            reviewsContainer.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            for (Review review : reviews) {
                ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.review, reviewsContainer, false);
                TextView reviewAuthor = reviewContainer.findViewById(R.id.review_author);
                TextView reviewContent = reviewContainer.findViewById(R.id.review_content);
                reviewAuthor.setText(review.getAuthor());
                reviewContent.setText(review.getContent());
                reviewContent.setOnClickListener(this);
                reviewsContainer.addView(reviewContainer);
            }
        }
    }




    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_thumb:
                onThumbnailClick(view);
                break;

            case R.id.review_content:
                onReviewClick((TextView) view);
                break;

            case R.id.favorite:

                break;

            default:
                break;
        }
    }

    private void onReviewClick(TextView view) {
        if (view.getMaxLines() == 5) {
            view.setMaxLines(500);
        } else {
            view.setMaxLines(5);
        }
    }

    private void onThumbnailClick(View view) {
        String videoUrl = (String) view.getTag(R.id.glide_tag);
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movieDetailsPresenter.destroy();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // ((BaseApplication) getActivity().getApplication()).releaseDetailsComponent();
    }
}
