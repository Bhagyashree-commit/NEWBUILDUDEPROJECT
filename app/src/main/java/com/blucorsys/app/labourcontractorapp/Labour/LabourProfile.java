package com.blucorsys.app.labourcontractorapp.Labour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.blucorsys.app.labourcontractorapp.Contractor.ContractorProfile;
import com.blucorsys.app.labourcontractorapp.Contractor.CustomAdapter;
import com.blucorsys.app.labourcontractorapp.Contractor.PostJob;
import com.blucorsys.app.labourcontractorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class LabourProfile extends AppCompatActivity {
    private static final String TAG = LabourProfile.class.getSimpleName();
    Spinner spincat;
    CustomLoader loader;
    Preferences pref;
    private ArrayList<String> categoryEng;
    private ArrayList<String> categoryID;
    LinearLayout linlay;
    CustomAdapter customAdapterTwo;
    TextView btn_submitlabordetails,tv_dob;
    RadioGroup rg_gender;
    RadioButton rb_male,rb_female;
    String gender,userid,location,dateofbirth,wagerate,aadharnum,pincode ;
    EditText et_location,et_adharnumber,et_wage,et_pincode;
    String date,time,datetime;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String spin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_profile);
        spincat = findViewById(R.id.spinn_cat);
        btn_submitlabordetails = findViewById(R.id.btn_submitlabordetails);
        linlay = findViewById(R.id.linlay);
        rg_gender = findViewById(R.id.rg_gender);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        et_location = findViewById(R.id.et_location);
        et_adharnumber = findViewById(R.id.et_adharnumber);
        et_pincode = findViewById(R.id.et_pincode);
        et_wage = findViewById(R.id.et_wage);
        tv_dob = findViewById(R.id.tv_dob);

        pref = new Preferences(this);
        categoryEng = new ArrayList<String>();
        categoryID = new ArrayList<String>();

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        getProfile(pref.get(Constants.USERID));

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                 gender= String.valueOf(radioButton.getText());
                Toast.makeText(getBaseContext(), gender, Toast.LENGTH_SHORT).show();
            }
        });

        getCategory();

        spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spin=spincat.getSelectedItem().toString();
               Log.e("nishaaa",""+spin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        tv_dob.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          final Calendar c = Calendar.getInstance();
                                          year = c.get(Calendar.YEAR);
                                          month = c.get(Calendar.MONTH);
                                          dayOfMonth = c.get(Calendar.DAY_OF_MONTH);


                                          DatePickerDialog datePickerDialog = new DatePickerDialog(LabourProfile.this,
                                                  new DatePickerDialog.OnDateSetListener() {

                                                      @Override
                                                      public void onDateSet(DatePicker view, int year,
                                                                            int monthOfYear, int dayOfMonth) {

                                                          tv_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                                      }
                                                  }, year, month, dayOfMonth);
                                          datePickerDialog.show();
                                      }
                                  });
        btn_submitlabordetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid=pref.get(Constants.USERID);
                location=et_location.getText().toString();
                dateofbirth=tv_dob.getText().toString();
                wagerate=et_wage.getText().toString();
                aadharnum=et_adharnumber.getText().toString();
                pincode=et_pincode.getText().toString();

                updateProfileLabour(userid,gender,location,dateofbirth,wagerate,aadharnum,pincode,spin);
            }
        });

    }

    private void getCategory() {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.GETCATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    //Parsing the fetched Json String to JSON Object
                    j = new JSONObject(response);

                    JSONArray jsonArray= j.getJSONArray(Constants.JSONARRAY);

                    //Calling method getStudents to get the students from the JSON Array
                    getCategoriess(jsonArray);
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
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }

    private void getCategoriess(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                categoryID.add(json.getString(Constants.CATID));
             categoryEng.add(json.getString(Constants.CATENG));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spincat.setAdapter(new ArrayAdapter<String>(LabourProfile.this, android.R.layout.select_dialog_item, categoryEng));
    }

    private void updateProfileLabour(final String userid,final String gender,final String location,final String dateofbirth,final String wagerate,final String aadharnum,final String pincode,final String spin) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LABOURPROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("success").equalsIgnoreCase("true")) {

                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        pref.set(Constants.STATUS,"1");
                        pref.commit();
                        startActivity(new Intent(LabourProfile.this, LabourConsole.class));

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
                params.put("location", location);
                params.put("pincode", pincode);
                params.put("ruserid", userid);
                params.put("gender", gender);
                params.put("cat_name", spin);
                params.put("dob", dateofbirth);
                params.put("min_rate", wagerate);
                params.put("adhar_cardno", aadharnum);
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
                AppConfig.GETLABOURPROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                loader.dismiss();
                // btn_submitdeatils.setVisibility(View.INVISIBLE);
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        btn_submitlabordetails.setVisibility(View.INVISIBLE);
                        JSONArray array = object.getJSONArray("Profile");
                        for (int i = 0; i < array.length(); i++) {

                            //getting product object from json array
                            JSONObject obj = array.getJSONObject(i);
                            tv_dob.setText(obj.getString("dob"));
                            et_location.setText(obj.getString("location"));
                            et_pincode.setText(obj.getString("pincode"));
                            et_wage.setText(obj.getString("min_rate"));
                            et_adharnumber.setText(obj.getString("adhar_cardno"));
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