package com.polinc.movieappex.ui.intro;

import android.os.Bundle;
  
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.polinc.movieappex.R;

public class IntroSlider extends AppIntro {
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  

        addSlide(AppIntroFragment.newInstance("Popular movies", "List of popular movies",
                R.drawable.logo_small,  ContextCompat.getColor(getApplicationContext(), R.color.purple_200) ));
  
         addSlide(AppIntroFragment.newInstance("Top rated movies", "List of top rated movies", R.drawable.logo_small ,
                ContextCompat.getColor(getApplicationContext(), R.color.purple_200)));


        AppIntroFragment slide=AppIntroFragment.newInstance("Upcoming movies",
                "List of upcoming movies",
                R.drawable.logo_small,
                ContextCompat.getColor(getApplicationContext(),R.color.purple_200),
                ContextCompat.getColor(getApplicationContext(),R.color.teal_200),
                ContextCompat.getColor(getApplicationContext(),R.color.white),
                R.font.dejavusansbold,
                R.font.dejavusansitalics,
                R.drawable.background
                );

        addSlide(slide);
    }

    public void   onSkipPressed(Fragment currentFragment ) {
        super.onSkipPressed(currentFragment);
        // Decide what to do when the user clicks on "Skip"
        finish();
    }

    public void onDonePressed(Fragment currentFragment ) {
        super.onDonePressed(currentFragment);
        // Decide what to do when the user clicks on "Done"
        finish();
    }
}