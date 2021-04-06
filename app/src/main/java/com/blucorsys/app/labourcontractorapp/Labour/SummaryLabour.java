package com.blucorsys.app.labourcontractorapp.Labour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.blucorsys.app.CustomComponent.JobModel;
import com.blucorsys.app.ServerCall.AppConfig;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.Contractor.summary_consofragment;
import com.blucorsys.app.labourcontractorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryLabour extends AppCompatActivity {
    private static final String TAG = SummaryLabour.class.getSimpleName();
    Preferences pref;
    CustomLoader loader;
    RecyclerView rv_summlab;
    String usertype,credate;
    TextView tv_sdate,tv_ok,tv_edate;
    Spinner spin_loc;
    String location,dates;

    private ArrayList<String> loca;
    private ArrayList<String> date;
    ArrayList<JobModel> joblist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_labour);
        loader = new CustomLoader(SummaryLabour.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(SummaryLabour.this);
        spin_loc=findViewById(R.id.spin_loc);
        tv_sdate=findViewById(R.id.tv_sdate);
        tv_ok=findViewById(R.id.tv_ok);
        rv_summlab=findViewById(R.id.rv_summlab);
        usertype="LABOUR";
        loca = new ArrayList<String>();
        date = new ArrayList<String>();
        joblist = new ArrayList<JobModel>();
        getloc(pref.get(Constants.USERID),usertype);


        spin_loc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                credate=date.get(position);
                location=spin_loc.getSelectedItem().toString();
                Log.e("nishaaa3",""+credate);
                tv_sdate.setText(credate);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitAPI(pref.get(Constants.USERID),location,usertype);
            }
        });

    }
    private void getloc(final String userid,final String type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FILTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {


                        JSONArray array=object.getJSONArray("Job-data");
                        getlocations(array);


                    }
                    else {
//
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
        }){
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
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }

    private void getlocations(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                loca.add(json.getString(Constants.LOC));
                date.add(json.getString(Constants.careda));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spin_loc.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, loca));
    }

    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerview.setAdapter(new SummaryLabour.AcceptJobAdapter(joblist));
    }

    //*Recyclerview Adapter*//
    private class AcceptJobAdapter extends RecyclerView.Adapter<SummaryLabour.Holder> {
        private Context context;
        private List<JobModel> jobmodel;

        public AcceptJobAdapter(List<JobModel> jobmodel) {

            this.jobmodel = jobmodel;
        }

        public SummaryLabour.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SummaryLabour.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_laborlist, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final SummaryLabour.Holder holder, final int position) {
            holder.itemView.setTag(jobmodel.get(position));

            final JobModel pu = jobmodel.get(position);

            holder.tv_sdetailsinput.setText(pu.getSite_detail());
            holder.tv_sdatee.setText(pu.getCreate_datetime());
            holder.tv_wagesinput.setText(pu.getWage());
            holder.tv_dwageinput.setText("0");
            holder.tv_lname.setText(pu.getFirst_name());


        }
        public int getItemCount() {
            //  return 1;
            return jobmodel.size();
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        TextView tv_sdetailsinput,tv_sdatee,tv_wagesinput,tv_dwageinput,tv_lname;



        public Holder(View itemView) {
            super(itemView);
            tv_sdetailsinput=itemView.findViewById(R.id.tv_sdetailsinput);
            tv_sdatee=itemView.findViewById(R.id.tv_sdatee);
            tv_wagesinput=itemView.findViewById(R.id.tv_wagesinput);
            tv_dwageinput=itemView.findViewById(R.id.tv_dwageinput);
            tv_lname=itemView.findViewById(R.id.tv_lnameinput);



        }
    }

    private void hitAPI(final String userid,final String location,final String usertype) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SUMMARYLABOUR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {

                        JSONArray array=object.getJSONArray("Job-data");
                        {
                            Log.d(TAG, array.toString());
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject job = array.getJSONObject(i);
                                JobModel jobmodel = new JobModel();
                                //adding the product to product list
                                // jobmodel.setId(job.getString("id"));
                                jobmodel.setSite_detail(job.getString("site_detail"));
                                jobmodel.setCreate_datetime(job.getString("create_datetime"));
                                jobmodel.setWage(job.getString("wage"));
                                jobmodel.setFirst_name(job.getString("first_name"));


                                joblist.add(jobmodel);
                            }
                        }

                        setAdapter(rv_summlab);
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
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ruserid", userid);
                params.put("location", location);
                params.put("usertype", usertype);
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