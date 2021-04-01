package com.blucorsys.app.labourcontractorapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blucorsys.app.CustomComponent.Constants;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.Contractor.ContractorConsole;
import com.blucorsys.app.labourcontractorapp.Labour.LabourConsole;


public class SplashScreen extends AppCompatActivity {

    private boolean isFirstAnimation = false;
    TextView shantal;
    Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        Animation hold = AnimationUtils.loadAnimation(this, R.anim.hold);
        /* Translates ImageView from current position to its original position, scales it from
        larger scale to its original scale.*/
        final Animation translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale);

        final ImageView imageView = findViewById(R.id.ivLogo);
        shantal=findViewById(R.id.shantal);

        pref=new Preferences(this);

        translateScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirstAnimation) {
                    imageView.clearAnimation();


                    if (pref.get(Constants.USERID).isEmpty() || pref.get(Constants.role).isEmpty()) {
                        // User is already logged in. Take him to main activity
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else if(!pref.get(Constants.USERID).isEmpty() && pref.get(Constants.role).equalsIgnoreCase("CONTRACTOR")) {
                        Intent i1 = new Intent(SplashScreen.this, ContractorConsole.class);
                        startActivity(i1);
                        finish();
                    }
                    else if(!pref.get(Constants.USERID).isEmpty() && pref.get(Constants.role).equalsIgnoreCase("LABOUR")) {
                        Intent i1 = new Intent(SplashScreen.this, LabourConsole.class);
                        startActivity(i1);
                        finish();
                    }
                    else if(!pref.get(Constants.USERID).isEmpty() && pref.get(Constants.role).equalsIgnoreCase("ARCHITECT")) {
                        showPopUp();
                    }

                    else if(!pref.get(Constants.USERID).isEmpty() && pref.get(Constants.role).equalsIgnoreCase("ENGINEER")) {
                        showPopUp();
                    }
                    else if(!pref.get(Constants.USERID).isEmpty() && pref.get(Constants.role).equalsIgnoreCase("SUPPLIER")) {
                        showPopUp();
                    }
                    else if(!pref.get(Constants.USERID).isEmpty() && pref.get(Constants.role).equalsIgnoreCase("OTHER SERVICES")) {
                        showPopUp();
                    }
                    else if(!pref.get(Constants.USERID).isEmpty() && pref.get(Constants.role).equalsIgnoreCase("OWNER")) {
                        showPopUp();
                    }
                    else if(!pref.get(Constants.USERID).isEmpty() && pref.get(Constants.role).equalsIgnoreCase("DEVELOPER")) {
                        showPopUp();
                    }
                }

                isFirstAnimation = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        hold.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                imageView.startAnimation(translateScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(hold);

        //getSupportActionBar().hide();
        //StartAnimations();
        new Handler().postDelayed(new Runnable() {


            @Override public void run() {
                // This method will be executed once the timer is over
//                Intent i = new Intent(SplashScreen.this, MainActivity.class);
//                startActivity(i);
//                finish();
            }
        }, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void showPopUp(){
        AlertDialog alertDialog = new AlertDialog.Builder(SplashScreen.this).create();
        alertDialog.setMessage("Thank You For Registering with BUILDUDE! We Will get back to you with this functionality very soon!");
        //alertDialog.setMessage(" Your Last Location Is "+mCurrentLocation);

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
