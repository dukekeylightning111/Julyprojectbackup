
package com.example.mysportfriends_school_project;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       func_splash_screen();
    }

 public void func_splash_screen(){
        CountDownTimer countDownTimer = new CountDownTimer(4500, 400) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Intent i1 = new Intent(getApplicationContext(), WelcomeUserActivity.class);
                startActivity(i1);
                finish();
            }
        };
        countDownTimer.start();
    }


}
