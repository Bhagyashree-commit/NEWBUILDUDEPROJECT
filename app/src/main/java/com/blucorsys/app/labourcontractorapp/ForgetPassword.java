package com.blucorsys.app.labourcontractorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends AppCompatActivity  {
    private static final String TAG = ForgetPassword.class.getSimpleName();
    CustomLoader loader;
    Preferences pref;
    TextView tv_check,tv_verify,btnchangepass,et_newpass;
    String mobnum,deviceid,data,newpass;
    EditText et_mobnum,et_otp;
    LinearLayout ll8,ll2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(this);
        tv_check=findViewById(R.id.tv_fcheck);
        et_mobnum=findViewById(R.id.et_fmobnum);
        btnchangepass=findViewById(R.id.btnchangepass);
        et_newpass=findViewById(R.id.et_newpass);
        et_otp=findViewById(R.id.et_otp);
        tv_verify=findViewById(R.id.tv_verify);
        ll8=findViewById(R.id.ll8);
        ll2=findViewById(R.id.ll2);

        tv_check.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              checkValidation();
          }
      });

        tv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMobile(mobnum,data);
            }
        });
        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobnum=et_mobnum.getText().toString().trim();
                newpass=et_newpass.getText().toString().trim();
                changepass(mobnum,newpass);
            }
        });



    }

    private void checkValidation(){
        mobnum=  et_mobnum.getText().toString();
        deviceid = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (et_mobnum.getText().toString().trim().length()<10) {
            Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            et_mobnum.requestFocus();
        }
        else{
            ChekNo(mobnum,deviceid);
        }

    }

    private void ChekNo(final String mobnum,final String deviceid) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FORGETPASS1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("success").equalsIgnoreCase("true")) {
                        ll8.setVisibility(View.VISIBLE);
                        data=object.getString("OTP");
                      //  Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                        et_otp.setText(data);
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
                params.put("device_id", deviceid);
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
                AppConfig.FORGETPASS2, new Response.Listener<String>() {
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
                params.put("fotp", otp);
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
    private void changepass(final String mobnum,final String pass) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FORGETPASS3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("success").equalsIgnoreCase("true")) {

                        String mgs=object.getString("message");
                        startActivity(new Intent(ForgetPassword.this, LoginActivity.class));
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
                params.put("newpassword", pass);

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