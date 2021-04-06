package com.blucorsys.app.labourcontractorapp.Labour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.blucorsys.app.CustomComponent.JobModel;
import com.blucorsys.app.ServerCall.AppConfig;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.Contractor.GPSTracker;
import com.blucorsys.app.labourcontractorapp.Contractor.PostJob;
import com.blucorsys.app.labourcontractorapp.LoginActivity;
import com.blucorsys.app.labourcontractorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StartTrackActivity extends AppCompatActivity {
    private static final String TAG = StartTrackActivity.class.getSimpleName();
    ArrayList<JobModel> joblist;
    Preferences pref;
    CustomLoader loader;
    GPSTracker mGPS;
    String type,jobid,date;
    TextView jobareainput,labortypeinput,wagerateinput,jobdateinput,btn_starttrack,btn_stoptracking,btn_startwork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_track);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(this);
        Bundle data = getIntent().getExtras();
        JobModel model =  data.getParcelable("job");
        jobareainput= findViewById(R.id.jobareainput);
        labortypeinput= findViewById(R.id.labortypeinput);
        wagerateinput= findViewById(R.id.wagerateinput);
        jobdateinput= findViewById(R.id.jobdateinput);
        btn_starttrack= findViewById(R.id.btn_starttrack);
        btn_stoptracking= findViewById(R.id.btn_stoptracking);
        btn_startwork= findViewById(R.id.btn_stopwork);

        jobareainput.setText(model.getLocation());
        labortypeinput.setText(model.getCat_english());
        wagerateinput.setText(model.getWage());
        jobdateinput.setText(model.getCreate_datetime());
        type="LABOUR";
        jobid=model.getJobid_details();
        Log.e("newwwwwwwwwww",""+pref.get(Constants.RADIOID));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = simpledateformat.format(calendar.getTime());
        Log.e("date",""+date);

        btn_starttrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // btn_starttrack.setText("STOP");
                hitAPIcheckdate(model.getCattype_id());
            }
        });

        btn_startwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartTrackActivity.this);

                builder.setTitle("Stop Job");
                builder.setMessage("Are You Sure about job process end?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if(mGPS.canGetLocation() ){
                            mGPS.getLocation();
                        }

                        final String eventlat = String.valueOf(mGPS.getLatitude());
                        final String eventlong = String.valueOf(mGPS.getLongitude());
                        Log.e("bhanu",""+eventlat);
                        Log.e("bhanu",""+eventlong);
                        stopWork(pref.get(Constants.USERID),type,pref.get(Constants.RADIOID),date,eventlat,eventlong);
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        btn_stoptracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGPS = new GPSTracker(StartTrackActivity.this);
                if(mGPS.canGetLocation() ){
                    mGPS.getLocation();
                }

                final String eventlat = String.valueOf(mGPS.getLatitude());
                final String eventlong = String.valueOf(mGPS.getLongitude());
                Log.e("bhanu",""+eventlat);
                Log.e("bhanu",""+eventlong);
                startTrack3(pref.get(Constants.USERID),pref.get(Constants.RADIOID),type,eventlat,eventlong,date);
            }
        });
    }
    private void hitAPIcheckdate(final String typeid) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.CHECKDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Message").equalsIgnoreCase("true")) {

                        JSONArray array=object.getJSONArray("Show-Job");
                        {
                             mGPS = new GPSTracker(StartTrackActivity.this);
                            if(mGPS.canGetLocation() ){
                                mGPS.getLocation();
                            }
                            final String eventlat = String.valueOf(mGPS.getLatitude());
                            final String eventlong = String.valueOf(mGPS.getLongitude());
                            Log.e("bhanu",""+eventlat);
                            Log.e("bhanu",""+eventlong);

                           // Toast.makeText(getApplicationContext(), "Tracking start Successfully", Toast.LENGTH_LONG).show();
                            startTrackAPI(pref.get(Constants.USERID),jobid,type,eventlat,eventlong,date);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry Job Expired", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("cattype_id", typeid);
                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }
    private void startTrackAPI(final String userid,final String jobid,final String type,final  String lat,final  String lng,final String date) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.STARTTRACK1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                btn_starttrack.setVisibility(View.INVISIBLE);
                btn_stoptracking.setVisibility(View.VISIBLE);
                loader.dismiss();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("success").equalsIgnoreCase("true")) {

                        // check if GPS enabled
                        if(mGPS.canGetLocation()){

                            double latitude = mGPS.getLatitude();
                            double longitude = mGPS.getLongitude();
//                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        }else{

                            mGPS.showSettingsAlert();
                        }

                        final String eventlat = String.valueOf(mGPS.getLatitude());
                        final String eventlong = String.valueOf(mGPS.getLongitude());
                        Log.e("bhanu",""+eventlat);
                        Log.e("bhanu",""+eventlong);

                        startTrack2(pref.get(Constants.USERID),pref.get(Constants.RADIOID),type,eventlat,eventlong);
                    }
                    else {

                        Toast.makeText(getApplicationContext(), "Sorry Job Expired", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ruserid", userid);
                params.put("track_jobid", pref.get(Constants.RADIOID));
                params.put("usertype", type);
                params.put("latitude", lat);
                params.put("longitude", lng);
                params.put("added_datetime", date);
                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }
    private void startTrack2(final String userid,final String jobid,final String type,final  String lat,final  String lng) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.STARTTRACK2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("success").equalsIgnoreCase("true")) {

                        // check if GPS enabled
                        if(mGPS.canGetLocation()){

                            double latitude = mGPS.getLatitude();
                            double longitude = mGPS.getLongitude();
//                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        }else{

                            mGPS.showSettingsAlert();
                        }

                        final String eventlat = String.valueOf(mGPS.getLatitude());
                        final String eventlong = String.valueOf(mGPS.getLongitude());
                        Log.e("bhanu",""+eventlat);
                        Log.e("bhanu",""+eventlong);

                        //
                    }
                    else {

                        Toast.makeText(getApplicationContext(), "Sorry Job Expired", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ruserid", userid);
                params.put("track_jobid", pref.get(Constants.RADIOID));
                params.put("usertype", type);
                params.put("dest_latitude", lat);
                params.put("dest_longitude", lng);
                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }

    private void startTrack3(final String userid,final String jobid,final String type,final  String lat,final  String lng,final String datetime) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.STARTTRACK3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("success").equalsIgnoreCase("true")) {
                        btn_stoptracking.setVisibility(View.INVISIBLE);

                        btn_startwork.setVisibility(View.VISIBLE);
                        // check if GPS enabled
                        if(mGPS.canGetLocation()){

                            double latitude = mGPS.getLatitude();
                            double longitude = mGPS.getLongitude();
//                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        }else{

                            mGPS.showSettingsAlert();
                        }

                        final String eventlat = String.valueOf(mGPS.getLatitude());
                        final String eventlong = String.valueOf(mGPS.getLongitude());
                        Log.e("bhanu",""+eventlat);
                        Log.e("bhanu",""+eventlong);



                        AlertDialog.Builder builder = new AlertDialog.Builder(StartTrackActivity.this);

                        builder.setTitle("Work Update!");
                        builder.setMessage("Your Working hours are started!!");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {


                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // startTrack2(pref.get(Constants.USERID),pref.get(Constants.RADIOID),type,eventlat,eventlong);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry Job Expired", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ruserid", userid);
                params.put("track_jobid", pref.get(Constants.RADIOID));
                params.put("usertype", type);
                params.put("dest_latitude", lat);
                params.put("dest_longitude", lng);
                params.put("job_start_datetime", datetime);
                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }
    private void stopWork(final String userid,final String type,final String jobid, final String datetime,final String lat,final String lng) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ENDJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();

                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("success").equalsIgnoreCase("true")) {

                        if(mGPS.canGetLocation()){

                            double latitude = mGPS.getLatitude();
                            double longitude = mGPS.getLongitude();
//                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        }else{

                            mGPS.showSettingsAlert();
                        }

                        final String eventlat = String.valueOf(mGPS.getLatitude());
                        final String eventlong = String.valueOf(mGPS.getLongitude());
                        Log.e("bhanu",""+eventlat);
                        Log.e("bhanu",""+eventlong);

                        AlertDialog.Builder builder = new AlertDialog.Builder(StartTrackActivity.this);

                        builder.setTitle("Work Update!");
                        builder.setMessage("You Want to Stop Your Working Hours?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                startActivity(new Intent(StartTrackActivity.this, LabourConsole.class));
                                dialog.dismiss();

                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry Job Expired", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ruserid", userid);
                params.put("track_jobid", jobid);
                params.put("usertype", type);
                params.put("dest_latitude", lat);
                params.put("dest_longitude", lng);
                params.put("job_end_datetime", datetime);
                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }

}