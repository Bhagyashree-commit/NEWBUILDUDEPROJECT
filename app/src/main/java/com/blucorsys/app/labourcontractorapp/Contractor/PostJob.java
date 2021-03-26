package com.blucorsys.app.labourcontractorapp.Contractor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.blucorsys.app.labourcontractorapp.MapsActivity;
import com.blucorsys.app.labourcontractorapp.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostJob extends AppCompatActivity {
    private static final String TAG = PostJob.class.getSimpleName();
Preferences pref;
CustomLoader loader;
TextView iv_plus;
ImageView iv_plus1;
TextView tv_name;
TextView datetimer;
TextView btnpostjob;
TextView tv_location;
Spinner spincat;
LinearLayout layout;
TextView tv_typecat;
    private ArrayList<String> categoryEng;
    private ArrayList<String> categoryID;
    private ArrayList<String> categoryMar;
    private ArrayList<String> categoryHin;
    String date,time,datetime;
    String cat_id="";
    EditText et_sitedetails;
    CustomAdapter customAdapterTwo;

    ArrayList<Spinner> spinnerCatArrayList;
    ArrayList<EditText> etwagesArrayList;
    ArrayList<EditText> etnumberArrayList;


    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        pref=new Preferences( this);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        iv_plus=findViewById(R.id.iv_plus);
        iv_plus1=findViewById(R.id.iv_plus1);
        tv_name=findViewById(R.id.tv_name);
        tv_location=findViewById(R.id.tv_location);
        datetimer=findViewById(R.id.datetime);
        spincat=findViewById(R.id.spincat);
        et_sitedetails=findViewById(R.id.et_sitedetails);
        btnpostjob=findViewById(R.id.btnpostjob);
      // tv_typecat=findViewById(R.id.tv_typecat);

        tv_name.setText(pref.get(Constants.FIRSTNAME )+ " " +pref.get(Constants.LASTNAME));
        categoryEng = new ArrayList<String>();
        categoryID = new ArrayList<String>();
        categoryMar = new ArrayList<String>();
        categoryHin = new ArrayList<String>();
        spinnerCatArrayList=new ArrayList<>();
        etwagesArrayList=new ArrayList<>();
        etnumberArrayList=new ArrayList<>();

        datetimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(PostJob.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        if(selectedmonth>9) {
                            date = +selectedyear + "-" + selectedmonth + "-" + selectedday;
                        }
                        else {
                            date = +selectedyear + "-0" + selectedmonth + "-" + selectedday;
                        }
                        // eReminderDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        Log.e("", "" + date);
                        // sessionManagerContractor.set("date","date");
                    }
                }, mYear, mMonth, mDay);
                Calendar mcurrentTime = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                TimePickerDialog timePickerDialog = new TimePickerDialog(PostJob.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if (mcurrentTime.getTimeInMillis() >= c.getTimeInMillis()) {
                                    //it's after current
                                    int hour = hourOfDay % 12;
//                                    btnPickStartTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
//                                            minute, hourOfDay < 12 ? "am" : "pm"));
                                } else {
                                    //it's before current'
                                    Toast.makeText(getApplicationContext(), "Invalid Time", Toast.LENGTH_LONG).show();
                                }

                                if(hourOfDay>=0 && hourOfDay<12){
                                    time = hourOfDay + ":" + minute +":00" ;
                                } else {
                                    if(hourOfDay == 12){
                                        time = hourOfDay + ":" + minute +":00";
                                    } else{
                                        hourOfDay = hourOfDay -12;
                                        time = hourOfDay + ":" + minute +":00";
                                    }
                                }
                                Log.e("", "" + time);
                                datetime = date +"  "+ time;
                                datetimer.setText(datetime);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
                mDatePicker.setTitle("Select Date");
                mDatePicker.getDatePicker().setMinDate(mcurrentDate.getTimeInMillis());
                mDatePicker.show();
            }
        });

        getCategory();

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pref.get(Constants.Lang).equals("ENGLISH")) {
                    addlayout(categoryEng);
                    iv_plus.setVisibility(View.INVISIBLE);
                    iv_plus1.setVisibility(View.VISIBLE);
                }
                else if(pref.get(Constants.Lang).equals("हिंदी")){
                    addlayout(categoryHin);
                    iv_plus.setVisibility(View.INVISIBLE);
                    iv_plus1.setVisibility(View.VISIBLE);
                }
                else {
                    addlayout(categoryMar);
                    iv_plus.setVisibility(View.INVISIBLE);
                    iv_plus1.setVisibility(View.VISIBLE);
                }
            }
        });
        iv_plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pref.get(Constants.Lang).equals("ENGLISH")) {
                addlayout(categoryEng);
                }
                else if(pref.get(Constants.Lang).equals("हिंदी")){
                    addlayout(categoryHin);
                }
                else {
                    addlayout(categoryMar);
                }
            }
        });

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostJob.this, com.blucorsys.app.labourcontractorapp.Map.class);
                startActivity(intent);
            }
        });
        if(pref.get(Constants.newaddress).equalsIgnoreCase("")){
            Toast.makeText(PostJob.this, "please select area",Toast.LENGTH_LONG).show();
        }
        else {
            tv_location.setText(pref.get(Constants.newaddress));

        }
        btnpostjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<etnumberArrayList.size();i++)
                {
                    HashMap<String,String> map=new HashMap<>();
                    //Log.e("caaa",categoryEng.get(i));
                   // Log.e("caaa1",categoryEng.get(spinnerCatArrayList.get(i).getSelectedItemPosition()));
                    for(int j=0;j<categoryID.size();j++)
                    {
                        if(pref.get(Constants.Lang).equals("ENGLISH")) {
                            if (categoryEng.get(j).equalsIgnoreCase(categoryEng.get(spinnerCatArrayList.get(i).getSelectedItemPosition()))) {
                                // Log.e("ffff",categoryID.get(j));
                                map.put("category_id", categoryID.get(j));
                            }
                        }
                        else if(pref.get(Constants.Lang).equals("हिंदी")) {
                            if (categoryHin.get(j).equalsIgnoreCase(categoryHin.get(spinnerCatArrayList.get(i).getSelectedItemPosition()))) {
                                // Log.e("ffff",categoryID.get(j));
                                map.put("category_id", categoryID.get(j));
                            }
                        }
                        else {
                            if (categoryMar.get(j).equalsIgnoreCase(categoryMar.get(spinnerCatArrayList.get(i).getSelectedItemPosition()))) {
                                // Log.e("ffff",categoryID.get(j));
                                map.put("category_id", categoryID.get(j));
                            }
                        }
                    }
                   map.put("wages",etwagesArrayList.get(i).getText().toString());
                    map.put("number",etnumberArrayList.get(i).getText().toString());
                    arrayList.add(map);
                }
                Log.e("bhagya1",""+arrayList);
                JSONArray array = new JSONArray(arrayList);

                Log.e("array1",""+array);

                LinearLayout layout = (LinearLayout)findViewById(R.id.llview);
                int count = layout.getChildCount();
                for (int i = 0; i < count; i++) {
                    final View row = layout.getChildAt(i);
                    EditText et_wages=row.findViewById(R.id.tv_wages);
                    String data = et_wages.getText().toString();
                    Log.e("rashhhhh",""+data);
                }
                String userid=pref.get(Constants.USERID);
                String loc=tv_location.getText().toString();
                String site=et_sitedetails.getText().toString();
                String date=datetimer.getText().toString();
                String arr= String.valueOf(array);
                String type= "CONTRACTOR";
                if(tv_location.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Please select area", Toast.LENGTH_SHORT).show();
                    tv_location.requestFocus();
                }
                else if(et_sitedetails.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter site deatils !!!", Toast.LENGTH_SHORT).show();
                    et_sitedetails.requestFocus();
                }
                else if(datetimer.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Please enter job date !!!", Toast.LENGTH_SHORT).show();
                    datetimer.requestFocus();
                }
//                else if(etnumberArrayList.get(i).getText().length()==0){
//
//                        Toast.makeText(getApplicationContext(), "Please select category", Toast.LENGTH_SHORT).show();
//
//                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostJob.this);

                    builder.setTitle("Job Post");
                    builder.setMessage("Are You Sure about job posting?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            String la=pref.get(Constants.lat);
                            String ln=pref.get(Constants.lng);
                            postJob(userid, loc, site, date, arr, type,la,ln);
                            et_sitedetails.setText("");
//                                    Intent intent = new Intent(PostJob.this, ContractorProfile.class);
//                                    startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }
    public void addlayout(ArrayList<String> array )
    {
         layout = (LinearLayout)findViewById(R.id.llview);
        View child = getLayoutInflater().inflate(R.layout.table, null);
        Spinner spincat=child.findViewById(R.id.spincat);
        EditText et_wages=child.findViewById(R.id.tv_wages);
        EditText et_number=child.findViewById(R.id.tv_number);
        customAdapterTwo=new CustomAdapter(PostJob.this,array);
        spincat.setAdapter(customAdapterTwo);
        layout.addView(child);

        spinnerCatArrayList.add(spincat);
        etwagesArrayList.add(et_wages);
        etnumberArrayList.add(et_number);
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
                categoryMar.add(json.getString(Constants.CATMAR));
                categoryHin.add(json.getString(Constants.CATHIN));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        if(pref.get(Constants.Lang).equals("ENGLISH")){
//            spincat.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryEng));
//
//        }
//        else if(pref.get(Constants.Lang).equals("हिंदी")){
//            spincat.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryHin));
//
//        }
//        else {
//            spincat.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryMar));
//
//        }
    }

    private void postJob(final String userid,final String loc,final String site,final String date,final String jsonarray,final String type,final String la ,final String ln) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.POSTJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        String newstatus=object.getString("jobstatus");
                        pref.set(Constants.newaddress,"");
                        pref.set(Constants.newjobstatus,newstatus);
                        pref.commit();
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PostJob.this,ContractorConsole.class));

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
                params.put("ruserid",userid);
                params.put("location", loc);
                params.put("site_detail", site);
                params.put("create_datetime", date);
                params.put("cattype_id", jsonarray);
                params.put("usertype", type);
                params.put("latitude", la);
                params.put("longitude", ln);
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