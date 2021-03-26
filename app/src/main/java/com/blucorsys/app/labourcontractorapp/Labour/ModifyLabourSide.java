package com.blucorsys.app.labourcontractorapp.Labour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.blucorsys.app.labourcontractorapp.Contractor.PostJob;
import com.blucorsys.app.labourcontractorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModifyLabourSide extends AppCompatActivity {
    private static final String TAG = ModifyLabourSide.class.getSimpleName();
    Preferences pref;
    CustomLoader loader;
    String type;
    LinearLayout lljob;
    TextView jobareainput,labortypeinput,wagerateinput,jobdateinput,btn_modify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_labour_side);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(this);
        type="LABOUR";
        jobareainput=findViewById(R.id.jobareainput);
        labortypeinput=findViewById(R.id.labortypeinput);
        wagerateinput=findViewById(R.id.wagerateinput);
        jobdateinput=findViewById(R.id.jobdateinput);
        btn_modify=findViewById(R.id.btn_modify);
        lljob=findViewById(R.id.lljob);

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
alert();
            }
        });

        hitAPi(pref.get(Constants.USERID),type);
    }

    private void hitAPi(final String userid,final String type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GETFORMODIFY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        JSONArray array=object.getJSONArray("Show-Job");
                        {
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject obj = array.getJSONObject(i);
                                    jobareainput.setText(obj.getString("location"));
                                    labortypeinput.setText(obj.getString("cat_english"));
                                    wagerateinput.setText(obj.getString("wage"));
                                    jobdateinput.setText(obj.getString("create_datetime"));

                                    pref.set(Constants.accepted, obj.getString("accept_jobid"));
                                    pref.commit();
                                    Log.e("", "" + pref.get(Constants.accepted));

                                }

                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No Data For Modification", Toast.LENGTH_LONG).show();
                        lljob.setVisibility(View.INVISIBLE);
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
                params.put("ruserid",userid);
                params.put("usertype", type);
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

    void alert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModifyLabourSide.this);
       // alertDialog.setTitle("MODIFY");
        alertDialog.setTitle("Enter Reason For Modification");
        final EditText input = new EditText(ModifyLabourSide.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        //alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      String  reason = input.getText().toString();
                      Log.e("",""+reason);
                        hitModify(pref.get(Constants.USERID),type,pref.get(Constants.accepted),reason);
                    }
                });
        alertDialog.show();
    }
    private void hitModify(final String userid,final String type,final String acceptedjobid,final String reason) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.MODIFYLABOURJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {

                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        lljob.setVisibility(View.INVISIBLE);

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
                params.put("ruserid", userid);
                params.put("usertype", type);
                params.put("accept_jobid", acceptedjobid);
                params.put("reject_comment", reason);

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