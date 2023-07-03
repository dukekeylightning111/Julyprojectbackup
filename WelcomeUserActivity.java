package com.example.mysportfriends_school_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class WelcomeUserActivity extends AppCompatActivity {

    private Button signUpButton;
    private Button logInButton;
    private Button back_to_main_page_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        LogInFragment logInFragment = new LogInFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.myFragmentContainerView, logInFragment)
                .commit();
        signUpButton = findViewById(R.id.signUpButton);
        logInButton = findViewById(R.id.logInButton);
        logInButton.setVisibility(View.INVISIBLE);
        logInButton.setClickable(false);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment signUpFragment = new SignUpFragment();
                loadFragment(signUpFragment);
                signUpButton.setVisibility(View.INVISIBLE);
                signUpButton.setClickable(false);
                logInButton.setVisibility(View.VISIBLE);
                logInButton.setClickable(true);
            }
        });
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogInFragment logInFragment = new LogInFragment();
                loadFragment(logInFragment);
                logInButton.setVisibility(View.INVISIBLE);
                logInButton.setClickable(false);
                signUpButton.setVisibility(View.VISIBLE);
                signUpButton.setClickable(true);
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.myFragmentContainerView, fragment);
        transaction.commit();
    }
}