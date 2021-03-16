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
                addlayout(categoryEng);

                iv_plus.setVisibility(View.INVISIBLE);
                iv_plus1.setVisibility(View.VISIBLE);

            }
        });
        iv_plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addlayout(categoryEng);

            }
        });

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostJob.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        if(pref.get(Constants.address).equalsIgnoreCase("")){
            Toast.makeText(PostJob.this, "please select area",Toast.LENGTH_LONG).show();
        }
        else {
            tv_location.setText(pref.get(Constants.address));

        }
        btnpostjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<etnumberArrayList.size();i++)
                {

//                    if(etnumberArrayList.get(i).getText().length()==0)
//                    {
//
//                    }
                    HashMap<String,String> map=new HashMap<>();


                    Log.e("caaa",categoryEng.get(i));
                    Log.e("caaa1",categoryEng.get(spinnerCatArrayList.get(i).getSelectedItemPosition()));

                    for(int j=0;j<categoryID.size();j++)
                    {
                        if(categoryEng.get(j).equalsIgnoreCase(categoryEng.get(spinnerCatArrayList.get(i).getSelectedItemPosition())))

                        {
                            Log.e("ffff",categoryID.get(j));
                            map.put("category_id",categoryID.get(j));
                        }
                    }

                    Log.e("ggg",categoryEng.get(spinnerCatArrayList.get(i).getSelectedItemPosition()));
                  //  String spin=spinnerCatArrayList.get(i).getSelectedItem().toString();

                    Log.e("testss", String.valueOf(spinnerCatArrayList.get(i).getSelectedItemPosition()));
                    map.put("wages",etwagesArrayList.get(i).getText().toString());
                    map.put("number",etnumberArrayList.get(i).getText().toString());
                   // map.put("category",spin);
                   // etwagesArrayList.get(i).getText().toString();
                    arrayList.add(map);

                }
                Log.e("bhagya1",""+arrayList);
                JSONArray array = new JSONArray(arrayList);

                Log.e("array1",""+array);

                LinearLayout layout = (LinearLayout)findViewById(R.id.llview);
                int count = layout.getChildCount();
                for (int i = 0; i < count; i++) {
                    final View row = layout.getChildAt(i);
                  //  TextView textOut = (TextView)row.findViewById(R.id.textout);
                    //String data = textOut.getText().toString();
                    Log.e("","");
                }
                String userid=pref.get(Constants.USERID);
                String loc=tv_location.getText().toString();
                String site=et_sitedetails.getText().toString();
                String date=datetimer.getText().toString();
                String arr= String.valueOf(array);
                String type= "CONTRACTOR";
                if(tv_location.equals("LOCATION OF WORK")){
                    Toast.makeText(getApplicationContext(), "Please select area", Toast.LENGTH_SHORT).show();
                    tv_location.requestFocus();
                }
                else if(et_sitedetails.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter site deatils !!!", Toast.LENGTH_SHORT).show();
                    et_sitedetails.requestFocus();
                }
                else if(datetimer.equals("DATE AND TIME")){
                    Toast.makeText(getApplicationContext(), "Please enter location !!!", Toast.LENGTH_SHORT).show();
                    datetimer.requestFocus();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostJob.this);

                    builder.setTitle("Job Post");
                    builder.setMessage("Are You Sure about job posting?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            postJob(userid, loc, site, date, arr, type);
                            pref.set("add", "");
                            pref.commit();
                            et_sitedetails.setText("");
//                                    Intent intent = new Intent(PostJobs.this, ContractorProfile.class);
//                                    startActivity(intent);
                            dialog.dismiss();

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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void postJob(final String userid,final String loc,final String site,final String date,final String jsonarray,final String type) {
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
                        pref.set(Constants.address,"");
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
                params.put("latitude", pref.get(Constants.lat));
                params.put("longitude", pref.get(Constants.lng));
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