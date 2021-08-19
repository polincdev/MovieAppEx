package com.polinc.movieappex.ui.login;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.polinc.movieappex.R;
import com.polinc.movieappex.data.InitDataSource;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.databinding.ActivityLoginBinding;
import com.polinc.movieappex.ui.intro.IntroSlider;
import com.polinc.movieappex.ui.intro.MyCustomOnboarder;
import com.polinc.movieappex.ui.movies.MoviesActivity;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    ActivityLoginBinding binding;
    ProgressBar loadingProgressBar;
    EditText usernameEditText;
    EditText passwordEditText;
    TMDBFragment tmdbFragment;
    GuestFragment guestFragment;
    GoogleFragment googleFragment;
    Fragment currentFragment;
    Fragment prevFragment;
    TextView infoText;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Intro
        Intent i =new Intent(getApplicationContext(), MyCustomOnboarder.class);
        startActivity(i);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Disable login appbar
         getSupportActionBar().hide();

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        tmdbFragment=TMDBFragment.newInstance();
        guestFragment=GuestFragment.newInstance();
        googleFragment=GoogleFragment.newInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                   // .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left,R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                    .replace(R.id.frag_container, tmdbFragment)
                    .commitNow();
        }
        currentFragment=tmdbFragment;
        prevFragment=guestFragment;

        usernameEditText = binding.username;
         passwordEditText = binding.password;
        loadingProgressBar = binding.loading;
        infoText    = binding.infoText;


        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();

                   // ScaleAnimation animation = new ScaleAnimation(1, 2, 1, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                   // animation.setDuration(700);
                    //animation.setFillAfter(true);
                     Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    view.startAnimation(animation);

                   // ObjectAnimator animWidth = ObjectAnimator.ofInt(view, "minimumWidth", view.getWidth(),(int)(view.getWidth()*1.5f));
                   // animWidth.setDuration(700);
                   // animWidth.start();

                } else {
                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    view.startAnimation(animation);
                      } else {}

            }
        });

        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.loginTypeSpinner);
         // Spinner Drop down elemesnts
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Guest");
        categories.add("Google");
        categories.add("TMDB");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        //Prevent initial OnItemSelected trigger
        spinner.setSelection(0,false);
        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

                if(item.equals("TMDB")) {


                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                   // fragmentManager.addOnBackStackChangedListener(this);
                    fragmentTransaction.replace(R.id.frag_container, tmdbFragment, "h");
                    fragmentTransaction.addToBackStack("h");
                    fragmentTransaction.commit();
                }
                else if(item.equals("Guest")) {


                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    // fragmentManager.addOnBackStackChangedListener(this);
                    fragmentTransaction.replace(R.id.frag_container, guestFragment, "h");
                    fragmentTransaction.addToBackStack("h");
                    fragmentTransaction.commit();
                }
                else if(item.equals("Google")) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    // fragmentManager.addOnBackStackChangedListener(this);
                    fragmentTransaction.replace(R.id.frag_container, googleFragment, "h");
                    fragmentTransaction.addToBackStack("h");
                    fragmentTransaction.commit();
                }


           }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
               // loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                // Initialize
              //  MoviesGetController moviesGetController = ((MyApplication) LoginActivity.this.getApplication()).moviesGetController;
              //  Observable<MoviesWraper> moviesCall =moviesGetController.getMovies(1);
              //  moviesCall.subscribe(LoginActivity.this::onMovieFetchInitSuccess, LoginActivity.this::onMovieFetchFailed);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        infoText.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                 finish();
            }
        });


    }

    void onMovieFetchInitSuccess(MoviesWraper moviesWraper )
    {

        //
        ArrayList<Movie> movies = new ArrayList<Movie>();
        System.out.println("RESULTAA="+moviesWraper.getMovieList().size() );
        moviesWraper.getMovieList().forEach(movie ->  movies.add(movie));
        //set order for search
        for(int a=0;a<movies.size();a++)
           movies.get(a).order=a;

        InitDataSource initDataSource = ((MyApplication) this.getApplication()).initDataSource;
        initDataSource.initData.put(InitDataSource.MOVIES_POPULAR,movies);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingProgressBar.setVisibility(View.GONE);
            }
        });

        //
        startActivity(new Intent(LoginActivity.this, MoviesActivity.class));
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

      void onMovieFetchFailed(Throwable e) {

        e.printStackTrace();
        //
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingProgressBar.setVisibility(View.GONE);
                Snackbar.make(loadingProgressBar,"Error",2000 ).show();
            }
        });


    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_out, R.anim.activity_in);
    }

}