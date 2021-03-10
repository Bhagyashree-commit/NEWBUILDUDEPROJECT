package com.blucorsys.app.labourcontractorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blucorsys.app.CustomComponent.Constants;
import com.blucorsys.app.CustomComponent.CustomLoader;
import com.blucorsys.app.ServerCall.AppConfig;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.Contractor.ContractorConsole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
TextView btnlogin,tv_signup;
EditText et_mobnum,et_password;
CustomLoader loader;
Preferences pref;
String mobnum,pass;
String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin=(TextView)findViewById(R.id.btnlogin);
        et_mobnum=findViewById(R.id.et_mobnum);
        et_password=findViewById(R.id.et_password);
        tv_signup=findViewById(R.id.tv_signup);

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(this);
        CheckGpsStatus();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
      //  getFCM_token();

    }

    public void getFCM_token()
    {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {

                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("Log", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        token = task.getResult();
                        // Log and toast
                        String  msg = token;
                        Log.e("msg", msg);
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkValidation(){
        mobnum=  et_mobnum.getText().toString();
        pass = et_password.getText().toString();
        pref.set(Constants.pass,pass);
        pref.set(Constants.mobnum, mobnum);
        pref.commit();

        if(et_mobnum.getText().toString().length()<10){
            Toast.makeText(getApplicationContext(), "Please enter valid mobile!!!", Toast.LENGTH_SHORT).show();
            et_mobnum.requestFocus();

        }
        else if(et_password.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(), "Please enter valid password!!!", Toast.LENGTH_SHORT).show();
            et_password.requestFocus();
        }
        else {

            startActivity(new Intent(LoginActivity.this, Choose_RoleActivity.class));
        }


    }

    public void CheckGpsStatus(){

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}