package com.blucorsys.app.labourcontractorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blucorsys.app.CustomComponent.Constants;
import com.blucorsys.app.CustomComponent.CustomLoader;
import com.blucorsys.app.ServerCall.Preferences;


public class LoginActivity extends AppCompatActivity {
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
       // getFCM_token();

    }


    private void checkValidation(){
        mobnum=  et_mobnum.getText().toString();
        pass = et_password.getText().toString();
        if (!mobnum.isEmpty() && !pass.isEmpty()) {
            pref.set(Constants.mobnum,mobnum);
            pref.commit();
            pref.set(Constants.pass,pass);
            pref.commit();
            Log.e("",""+pref.get(Constants.Lang));
            Log.e("",""+pref.get(Constants.mobnum));
            Log.e("",""+pref.get(Constants.pass));
            startActivity(new Intent(LoginActivity.this, Choose_RoleActivity.class));

        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Please enter the credentials!", Toast.LENGTH_LONG)
                    .show();
        }
    }
}