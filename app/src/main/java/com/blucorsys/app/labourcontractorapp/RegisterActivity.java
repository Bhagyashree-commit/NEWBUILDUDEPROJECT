package com.blucorsys.app.labourcontractorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    EditText et_mobnum,et_otp,et_password,et_fname,et_lname,et_refnumber;
    TextView tv_check,tv_verify,btnsignup,tv_login;
    LinearLayout ll1,ll2;
    String mobnum,data,firstname,lastname,pass,refnumber,deviceid,userRole,token;
    CustomLoader loader;
    Preferences pref;
    CheckBox ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnsignup=findViewById(R.id.btnsignup);
        tv_check=findViewById(R.id.tv_check);
        et_mobnum=findViewById(R.id.et_mobnum);
        tv_verify=findViewById(R.id.tv_verify);
        et_otp=findViewById(R.id.et_otp);
        et_password=findViewById(R.id.et_password);
        et_fname=findViewById(R.id.et_fname);
        et_lname=findViewById(R.id.et_lname);
        et_refnumber=findViewById(R.id.et_refmobile);
        ll1=(LinearLayout) findViewById(R.id.ll1);
        ll2=(LinearLayout) findViewById(R.id.ll2);
        tv_login=findViewById(R.id.tv_login);
        ch=findViewById(R.id.ch);

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(this);

        tv_verify.setOnClickListener(this);
        tv_check.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
        tv_login.setOnClickListener(this);
       // getFCM_token();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_check:

                checkValidation();
                break;
            case R.id.tv_verify:
                validateMobile(mobnum,data);
                break;
            case R.id.btnsignup:

            validation();
              //  startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
            case  R.id.tv_login:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

        }
    }

    private void checkValidation(){
        mobnum=  et_mobnum.getText().toString();

        if (et_mobnum.getText().toString().trim().length()<10) {
            Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            et_mobnum.requestFocus();

        }
        else{
            ChekNo(mobnum);
        }

    }
    private void validation(){
        mobnum=  et_mobnum.getText().toString();
        pass=  et_password.getText().toString();
        firstname=  et_fname.getText().toString();
        lastname=  et_lname.getText().toString();
        refnumber=  et_refnumber.getText().toString();
        deviceid = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (et_mobnum.getText().toString().trim().length()<10) {
            Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            et_mobnum.requestFocus();
        }
        else if (et_password.getText().toString().trim().length()==0){
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
            et_password.requestFocus();
        }
        else if (et_fname.getText().toString().trim().length()==0){
            Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
            et_fname.requestFocus();
        }
        else if(!ch.isChecked()){
            Toast.makeText(getApplicationContext(), "Please Accept Terms And Conditions", Toast.LENGTH_SHORT).show();

        }
        else
        {
            registerUser(mobnum,pass,firstname,lastname,refnumber,deviceid,token);
        }
    }

    private void ChekNo(final String mobnum) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SENDOTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("success").equalsIgnoreCase("true")) {
                        ll1.setVisibility(View.VISIBLE);
                         data=object.getString("data");
                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                        et_otp.setText(data);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), object.getString("data"), Toast.LENGTH_LONG).show();
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
                params.put("mobileno", mobnum);
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

    private void validateMobile(final String mobnum,final  String otp) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.VERIFYMOBILENO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("success").equalsIgnoreCase("true")) {
                        ll2.setVisibility(View.VISIBLE);
                        String mgs=object.getString("message");

                        Toast.makeText(getApplicationContext(), mgs, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("mobileno", mobnum);
                params.put("otp", otp);
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

    private void registerUser(final String mobnum,final String pass,final String firstname,final String lastname,final String refnumber,final String deviceid,final String token) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("success").equalsIgnoreCase("true")) {

                        String mgs=object.getString("message");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        Toast.makeText(getApplicationContext(), mgs, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("mobileno", mobnum);
                params.put("password", pass);
                params.put("first_name", firstname);
                params.put("last_name", lastname);
                params.put("referal_mobileno", refnumber);
                params.put("device_id", deviceid);
                params.put("prefered_lang", pref.get(Constants.Lang));
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