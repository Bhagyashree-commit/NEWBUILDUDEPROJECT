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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.blucorsys.app.labourcontractorapp.Labour.LabourConsole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
TextView btnlogin,tv_signup,tv_forgetpassword;
EditText et_mobnum,et_password;
CustomLoader loader;
Preferences pref;
String mobnum,pass;
String token;
Spinner role;
String usertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin=(TextView)findViewById(R.id.btnlogin);
        et_mobnum=findViewById(R.id.et_mobnum);
        role=findViewById(R.id.role);
        tv_forgetpassword=findViewById(R.id.tv_forgetpassword);
        et_password=findViewById(R.id.et_password);
        tv_signup=findViewById(R.id.tv_signup);

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(this);

        List<String> list = new ArrayList();
        list.add("CHOOSE ROLE");
        list.add("CONTRACTOR");
        list.add("LABOUR");
        list.add("ARCHITECT");
        list.add("ENGINEER");
        list.add("SUPPLIER");
        list.add("OTHER SERVICES");
        list.add("OWNER");
        list.add("DEVELOPER");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(adapter);

        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                 usertype = LoginActivity.this.role.getSelectedItem().toString();
                LoginActivity.this.pref.set(Constants.role, usertype);
                LoginActivity.this.pref.commit();
                Log.e("", "" + LoginActivity.this.pref.get(Constants.role));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


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
        tv_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });
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
            if (LoginActivity.this.pref.get(Constants.role).equalsIgnoreCase("CHOOSE ROLE")) {
                Toast.makeText(LoginActivity.this, "please select your Role", Toast.LENGTH_LONG).show();
            } else {
                Log.e("ttttttt", LoginActivity.this.pref.get(Constants.role));

                loginUser(mobnum,pass,usertype,token);
            }


            // startActivity(new Intent(LoginActivity.this, Choose_RoleActivity.class));
        }
    }

    private void loginUser(final String mobile,final String password,final String usertype,final String token) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.NEWLOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        pref.set(Constants.USERID,object.getString("user_id"));
                        pref.set(Constants.FIRSTNAME,object.getString("first_name"));
                        pref.set(Constants.LASTNAME,object.getString("last_name"));
                        pref.set(Constants.MOBILENUMBER,object.getString("mobileno"));
                        pref.set(Constants.PREFLANG,object.getString("prefered_lang"));
                        pref.set(Constants.STATUS,object.getString("profile_status"));

                        pref.commit();

                        if(pref.get(Constants.role).equals("CONTRACTOR")) {
                            startActivity(new Intent(LoginActivity.this, ContractorConsole.class));
                        }
                        else if(pref.get(Constants.role).equals("LABOUR")){
                            startActivity(new Intent(LoginActivity.this, LabourConsole.class));
                        }
                        else {

                        }
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "wrong credentials", Toast.LENGTH_LONG).show();
                       // startActivity(new Intent(LoginActivity.this, LoginActivity.class));
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
                params.put("fcm_token",token);
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