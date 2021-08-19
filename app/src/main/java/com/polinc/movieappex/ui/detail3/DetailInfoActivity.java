package com.polinc.movieappex.ui.detail3;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.polinc.movieappex.R;
import com.polinc.movieappex.databinding.ActivityMovieDetailBinding;
import com.polinc.movieappex.databinding.ActivityMovieInfoBinding;
import com.polinc.movieappex.main.Api;
import com.polinc.movieappex.main.Consts;
import com.polinc.movieappex.models.Movie;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailInfoActivity extends AppCompatActivity {

    private ActivityMovieInfoBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding= ActivityMovieInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if (extras != null && extras.containsKey(Consts.MOVIE)) {

                 //Share image
                supportPostponeEnterTransition();
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String imageTransitionName = extras.getString(Consts.IMAGE_TRANSITION_NAME);
                     binding.toolbarImage.setTransitionName(imageTransitionName);
                }



                Movie movie = extras.getParcelable(Consts.MOVIE);
                if (movie != null) {
                    binding.contentMovieDetail.txtDetail.setText(movie.getOverview());

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .priority(Priority.HIGH);

                    Glide.with(this)
                            .asBitmap()
                            .load(Api.getPosterPath( movie.getPosterPath()))
                            .apply(options)
                            .into(binding.toolbarImage);
                   }
            }
        }


        //Requiress android:theme="@style/Theme.ScrollApplication.NoActionBar"
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        //actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}