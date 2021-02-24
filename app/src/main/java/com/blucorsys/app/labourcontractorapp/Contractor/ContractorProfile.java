package com.blucorsys.app.labourcontractorapp.Contractor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.blucorsys.app.labourcontractorapp.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContractorProfile extends AppCompatActivity {
    private static final String TAG = ContractorProfile.class.getSimpleName();
Preferences pref;
CustomLoader loader;
EditText et_comname,et_location,et_pincode,et_emailid,et_genralinfo;
TextView btn_submitdeatils;
String comname,location,pincode,email,generalinfo,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_profile);
        et_comname=findViewById(R.id.et_compname);
        et_location=findViewById(R.id.et_location);
        et_pincode=findViewById(R.id.et_pincode);
        et_emailid=findViewById(R.id.et_email);
        et_genralinfo=findViewById(R.id.et_generalinfo);
        btn_submitdeatils=findViewById(R.id.btn_submitdeatils);

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(this);

        getProfile(pref.get(Constants.USERID));
        btn_submitdeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comname=et_comname.getText().toString();
                location=et_location.getText().toString();
                pincode=et_pincode.getText().toString();
                email=et_emailid.getText().toString();
                generalinfo=et_genralinfo.getText().toString();
                userid=pref.get(Constants.USERID);
                updateProfile(comname,location,pincode,email,generalinfo,userid);
            }
        });
    }

    private void updateProfile(final String comname,final String location,final String pincode,final String email,final String generalinfo,final String userid) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.CONTRACTORPROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {

                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        pref.set(Constants.STATUS,"1");
                        pref.commit();
                        startActivity(new Intent(ContractorProfile.this,ContractorConsole.class));

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
                params.put("company_name", comname);
                params.put("location", location);
                params.put("pincode", pincode);
                params.put("emailid", email);
                params.put("general_info", generalinfo);
                params.put("ruserid", userid);
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

    private void getProfile(final String userid) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GETCONTRACTORPROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();
               // btn_submitdeatils.setVisibility(View.INVISIBLE);
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        btn_submitdeatils.setVisibility(View.INVISIBLE);
                        JSONArray array = object.getJSONArray("Profile");
                        for (int i = 0; i < array.length(); i++) {

                            //getting product object from json array
                            JSONObject obj = array.getJSONObject(i);
                            et_comname.setText(obj.getString("company_name"));
                            et_location.setText(obj.getString("location"));
                            et_pincode.setText(obj.getString("pincode"));
                            et_emailid.setText(obj.getString("emailid"));
                            et_genralinfo.setText(obj.getString("general_info"));
                            pref.set(Constants.STATUS,"1");
                            pref.commit();
                            Log.e("",""+pref.get(Constants.STATUS));

                        }
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
                params.put("user_id", userid);
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