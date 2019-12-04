package com.example.kipo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends Activity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    /**
     * Called when the activity is first created.
     */

    //Firebase authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                if (user != null) {
                    Intent mainIntent = new Intent(SplashScreen.this, PrincipalMenu.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}