package com.blucorsys.app.labourcontractorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
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
import com.blucorsys.app.labourcontractorapp.Labour.LabourConsole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Choose_RoleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Choose_RoleActivity.class.getSimpleName();
CardView cv1,cv2;
LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4;
Preferences pref;
CustomLoader loader;
String token;
String mobile,password,usertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__role);
        cv1=(CardView)findViewById(R.id.cv1);
        cv2=(CardView)findViewById(R.id.cv2);
        linearLayout1=(LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout2=(LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout3=(LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4=(LinearLayout) findViewById(R.id.linearLayout4);

        linearLayout1.startAnimation(inFromLeftAnimation());
        linearLayout2.startAnimation(inFromRightAnimation());
        linearLayout3.startAnimation(inFromLeftAnimation());
        linearLayout4.startAnimation(inFromRightAnimation());

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(this);

        cv1.setOnClickListener(this);
        cv2.setOnClickListener(this);
    getFCM_token();

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
                        Toast.makeText(Choose_RoleActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(700);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(700);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cv1:
                 mobile=pref.get(Constants.mobnum);
                 password= pref.get(Constants.pass);
                 usertype="Contractor";
                loginUser(mobile,password,usertype,token);
                break;
            case R.id.cv2:
                 mobile=pref.get(Constants.mobnum);
                 password= pref.get(Constants.pass);
                 usertype="Labour";
               // startActivity(new Intent(Choose_RoleActivity.this, LabourConsole.class));
                loginUser1(mobile,password,usertype,token);
                break;

        }
    }


    private void loginUser(final String mobile,final String password,final String usertype,final String token) {
       // loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
              //  loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        pref.set(Constants.USERID, object.getString("user_id"));
                        pref.set(Constants.FIRSTNAME, object.getString("first_name"));
                        pref.set(Constants.LASTNAME, object.getString("last_name"));
                        pref.set(Constants.MOBILENUMBER, object.getString("mobileno"));
                        pref.set(Constants.PREFLANG, object.getString("prefered_lang"));
                        pref.set(Constants.STATUS, object.getString("profile_status"));
                        pref.commit();
                        startActivity(new Intent(Choose_RoleActivity.this, ContractorConsole.class));
                        Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "wrong credentials", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileno", mobile);
                params.put("password", password);
                params.put("usertype", usertype);
                params.put("fcm_token", token);
                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }


    private void loginUser1(final String mobile,final String password,final String usertype,final String token) {
        // loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                //  loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        pref.set(Constants.USERID, object.getString("user_id"));
                        pref.set(Constants.FIRSTNAME, object.getString("first_name"));
                        pref.set(Constants.LASTNAME, object.getString("last_name"));
                        pref.set(Constants.MOBILENUMBER, object.getString("mobileno"));
                        pref.set(Constants.PREFLANG, object.getString("prefered_lang"));
                        pref.set(Constants.STATUS, object.getString("profile_status"));
                        pref.commit();
                        startActivity(new Intent(Choose_RoleActivity.this, LabourConsole.class));
                        Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "wrong credentials", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileno", mobile);
                params.put("password", password);
                params.put("usertype", usertype);
                params.put("fcm_token", token);
                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }

}