package com.blucorsys.app.labourcontractorapp.Labour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.blucorsys.app.CustomComponent.JobModel;
import com.blucorsys.app.ServerCall.AppConfig;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.Contractor.AcceptOfferFragment;
import com.blucorsys.app.labourcontractorapp.Contractor.ContractorConsole;
import com.blucorsys.app.labourcontractorapp.Contractor.PostJob;
import com.blucorsys.app.labourcontractorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobStatus extends AppCompatActivity {
    private static final String TAG = JobStatus.class.getSimpleName();
    Preferences pref;
    CustomLoader loader;
    String type,credate,wagess;
    private ArrayList<String> loca;
    private ArrayList<String> date;
    private ArrayList<String> wage;
    private ArrayList<String> categoryMar;
    private ArrayList<String> categoryHin;
    ArrayList<JobModel> joblist;
    Spinner spin_loc,spincategory;
    String location,cattype_id;
    TextView tv_date,tv_wage,tv_ok,btn_accept;
    private ArrayList<String> categoryEng;
    private ArrayList<String> categoryID;
    RecyclerView rv_acceptjob;
    private int row_index = -1;
    String areaId="";
    Boolean status =false;
   JobModel pu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_status);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(this);

        loca = new ArrayList<String>();
        date = new ArrayList<String>();
        wage = new ArrayList<String>();
        categoryEng = new ArrayList<String>();
        categoryID = new ArrayList<String>();
        categoryMar = new ArrayList<String>();
        categoryHin = new ArrayList<String>();

        joblist = new ArrayList<JobModel>();
        spin_loc=findViewById(R.id.spin_loc);
        rv_acceptjob=findViewById(R.id.rv_acceptjob);
        spincategory=findViewById(R.id.spincategory);
        tv_date=findViewById(R.id.tv_date);
        tv_wage=findViewById(R.id.tv_wages);
        tv_ok=findViewById(R.id.tv_ok);
        btn_accept=findViewById(R.id.btn_accept);
type="LABOUR";
        getLocationL();
        getCategory();

        spin_loc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                credate=date.get(position);
                wagess=wage.get(position);
                location=spin_loc.getSelectedItem().toString();
                Log.e("nishaaa3",""+credate);
                tv_date.setText(credate);
                tv_wage.setText(wagess);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spincategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cattype_id=spincategory.getSelectedItem().toString();
                Log.e("nishaaa3",""+cattype_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status== true) {
                    hitAPi(pref.get(Constants.USERID),type,pref.get(Constants.JOBNEWID));

                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Select Address",Toast.LENGTH_LONG).show();

                }
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    hitAPIFORFILTER(cattype_id);

            }
        });
    }

    private void getLocationL() {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.JOBACCEPTLOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();

                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);
                    btn_accept.setVisibility(View.VISIBLE);
                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        JSONArray array=object.getJSONArray("Show-Job");
                        getlocations(array);

                      //  Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(getActivity(), ContractorConsole.class));

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
        });
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
                wage.add(json.getString(Constants.wage));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spin_loc.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, loca));
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
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
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
        if(pref.get(Constants.Lang).equals("ENGLISH")){
            spincategory.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryEng));

        }
        else if(pref.get(Constants.Lang).equals("हिंदी")){
            spincategory.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryHin));

        }
        else {
            spincategory.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryMar));

        }
    }


    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerview.setAdapter(new JobStatus.AcceptJobAdapter(joblist));
    }

    //*Recyclerview Adapter*//
    private class AcceptJobAdapter extends RecyclerView.Adapter<JobStatus.Holder> {
        private Context context;
        private List<JobModel> jobmodel;

        public AcceptJobAdapter(List<JobModel> jobmodel) {

            this.jobmodel = jobmodel;
        }

        public JobStatus.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new JobStatus.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_status_item, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final JobStatus.Holder holder, final int position) {
            holder.itemView.setTag(jobmodel.get(position));

             pu = jobmodel.get(position);
            holder.jobareainput.setText(pu.getLocation());
            holder.wagerateinput.setText(pu.getWage());
            holder.jobdateinput.setText(pu.getCreate_datetime());
            holder.labortypeinput.setText(pu.getCat_english());

            if(row_index==position)
            {
                String addrid = pu.getJobid_details();
                String jobid = pu.getAccept_jobid();
                pref.set(Constants.RADIOID,addrid);
                pref.set(Constants.JOBNEWID,jobid);
               pref.commit();
                status=true;
                Log.e("imageview",""+addrid);
                holder.ivradioOn.setVisibility(View.VISIBLE);
                holder.ivradio.setVisibility(View.GONE);
            }
            else
            {
                String addrid = pu.getId();
                holder.ivradioOn.setVisibility(View.GONE);
                holder.ivradio.setVisibility(View.VISIBLE);
            }


            holder.lljob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index=position;
                    notifyDataSetChanged();
                    areaId=pu.getId();
                }
            });

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

        TextView jobareainput;
        TextView labortypeinput;
        TextView jobdateinput;
        TextView wagerateinput;
        RadioButton rb_job;
        RadioGroup rgjob;
        LinearLayout lljob;
        ImageView ivradio;
        ImageView ivradioOn;


        public Holder(View itemView) {
            super(itemView);
            jobareainput=itemView.findViewById(R.id.jobareainput);
            labortypeinput=itemView.findViewById(R.id.labortypeinput);
            jobdateinput=itemView.findViewById(R.id.jobdateinput);
            wagerateinput=itemView.findViewById(R.id.wagerateinput);
            lljob=itemView.findViewById(R.id.lljob);
            ivradio=itemView.findViewById(R.id.ivradio);
            ivradioOn=itemView.findViewById(R.id.ivradioOn);

        }
    }
    private void hitAPIFORFILTER(final String cat_type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ACCEPTJOBFILTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                joblist.clear();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);
                    btn_accept.setVisibility(View.VISIBLE);
                    if(object.getString("Success").equalsIgnoreCase("true")) {

                        JSONArray array=object.getJSONArray("Show-Job");
                        {
                            Log.d(TAG, array.toString());
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject job = array.getJSONObject(i);
                                JobModel jobmodel = new JobModel();
                                //adding the product to product list
//                                jobmodel.setId(job.getString("id"));
                                     jobmodel.setJobid_details(job.getString("jobid_details"));
                                jobmodel.setLocation(job.getString("location"));
                              //  jobmodel.setFirst_name(job.getString("first_name"));
                                jobmodel.setAccept_jobid(job.getString("accept_jobid"));
                                jobmodel.setCattype_id(job.getString("cattype_id"));
                                jobmodel.setWage(job.getString("wage"));
                                jobmodel.setCreate_datetime(job.getString("create_datetime"));
                             //   jobmodel.setNo(job.getString("no"));
                                jobmodel.setCat_english(job.getString("cat_english"));
                                jobmodel.setCat_hindi(job.getString("cat_hindi"));
                                jobmodel.setCat_marathi(job.getString("cat_marathi"));

                                joblist.add(jobmodel);
                            }
                        }

                        setAdapter(rv_acceptjob);
                    }
                    else {
//                   rv_applyjob.setVisibility(View.INVISIBLE);
//                    btn_apply.setVisibility(View.INVISIBLE);
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

                params.put("cattype_id", cat_type);
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


    private void hitAPi(final String userid,final String type,final String jobid) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ACCEPTJOBNEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Response: " + response.toString());
                loader.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {

                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        Intent i=(new Intent(getApplicationContext(),StartTrackActivity.class));
                        i.putExtra("job",pu);
                        startActivity(i);
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
                params.put("usertype", type);
                params.put("accept_jobid", jobid);

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