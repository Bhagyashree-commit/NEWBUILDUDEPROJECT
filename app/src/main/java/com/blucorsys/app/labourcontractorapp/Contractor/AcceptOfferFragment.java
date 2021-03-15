package com.blucorsys.app.labourcontractorapp.Contractor;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.blucorsys.app.labourcontractorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AcceptOfferFragment extends Fragment {
    private static final String TAG = AcceptOfferFragment.class.getSimpleName();
    Preferences pref;
    CustomLoader loader;
    RecyclerView rv_accetjob;
    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    TextView tv_location,tv_date,tv_wage,tv_counter,tv_ok,btn_accept;
    String userid,usertype;
    String loc,date,wage,counter,cattype_id;
    Spinner spincat;
    private ArrayList<String> categoryEng;
    private ArrayList<String> categoryID;
    ArrayList<JobModel> joblist;
    String selectedjob;
    JSONArray array;
    public static List<String> joblisttt  = new ArrayList<>();


    public AcceptOfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_accept_offer, container, false);
        loader = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(getActivity());
        tv_location=v.findViewById(R.id.tv_location);
        tv_date=v.findViewById(R.id.tv_date);
        tv_wage=v.findViewById(R.id.tv_wage);
        tv_ok=v.findViewById(R.id.tv_ok);
        btn_accept=v.findViewById(R.id.btn_accept);
        tv_counter=v.findViewById(R.id.tv_counter);
        rv_accetjob=v.findViewById(R.id.rv_accetjob);
        spincat=v.findViewById(R.id.spin_cat);
        userid=pref.get(Constants.USERID);
        categoryEng = new ArrayList<String>();
        categoryID = new ArrayList<String>();
        joblist = new ArrayList<JobModel>();
        usertype="CONTRACTOR";


         getloc(userid,usertype);
         getCategory();

        spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cattype_id=spincat.getSelectedItem().toString();
                Log.e("nishaaa3",""+cattype_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        tv_ok.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 hitAPI(pref.get(Constants.USERID),cattype_id);
             }
         });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=pref.get(Constants.USERID);
                String jsonarr="";
                hitAcceptJob(id, String.valueOf(array),usertype);
            }
        });


        return v;
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
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
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
        spincat.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryEng));
    }

    private void getloc(final  String userid,final  String usertype) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GETLOCCON, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {

                        JSONArray array=object.getJSONArray("Show-Job");
                        {
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject obj = array.getJSONObject(i);
                                Log.d(TAG, array.toString());
                                tv_location.setText(obj.getString("location"));
                                tv_date.setText(obj.getString("create_datetime"));
                                tv_wage.setText(obj.getString("wage"));
                                tv_counter.setText(obj.getString("no"));
                                pref.set(Constants.LOC, object.getString("location"));
                                pref.set(Constants.CREADATE, object.getString("create_datetime"));
                                pref.set(Constants.wage, object.getString("wage"));
                                pref.set(Constants.count, object.getString("no"));
                                pref.commit();


                            }

                        }
                    }
                    else {
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ruserid",userid);
                params.put("usertype",usertype);

                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }


    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(new AcceptJobAdapter(joblist));
    }

    //*Recyclerview Adapter*//
    private class AcceptJobAdapter extends RecyclerView.Adapter<AcceptOfferFragment.Holder> {
        private Context context;
        private List<JobModel> jobmodel;

        public AcceptJobAdapter(List<JobModel> jobmodel) {

            this.jobmodel = jobmodel;
        }

        public AcceptOfferFragment.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AcceptOfferFragment.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_job_item, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final AcceptOfferFragment.Holder holder, final int position) {
            holder.itemView.setTag(jobmodel.get(position));

            final JobModel pu = jobmodel.get(position);

            holder.tv_lname.setText(pu.getFirst_name());
            holder.tv_wage.setText(pu.getWage());
            holder.tv_rating.setText("3.0");

            Log.e("rashmi","rash"+pu.getFirst_name());


            holder.checklabor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                    if(b){

                        selectedjob= pu.getJob_applied_id();
                        Log.e("AMOLf777",""+selectedjob);
                        joblisttt.add(selectedjob);
                        HashMap<String,String> map;
                        map=new HashMap<>();

                        map.put("accept_jobid",""+selectedjob);
                        arrayList.add(map);

                        array = new JSONArray(arrayList);
                        Log.e("AMOLf",""+array);

                    }
                    else{
                        joblisttt.remove(selectedjob);
                        Log.e("AMOL",""+selectedjob);

                    }

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

    TextView tv_lname;
    TextView tv_wage;
    TextView tv_rating;
    CheckBox checklabor;

        public Holder(View itemView) {
            super(itemView);
            tv_lname=itemView.findViewById(R.id.tv_lname);
            tv_wage=itemView.findViewById(R.id.tv_wage);
            tv_rating=itemView.findViewById(R.id.tv_rating);
            checklabor=itemView.findViewById(R.id.checklabor);
        }
    }
    private void hitAPI(final String userid,final String cat_type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SHOWAPPLYJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        btn_accept.setVisibility(View.VISIBLE);
                        JSONArray array=object.getJSONArray("Show-Job");
                        {
                            Log.d(TAG, array.toString());
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject job = array.getJSONObject(i);
                                JobModel jobmodel = new JobModel();
                                //adding the product to product list
                                jobmodel.setId(job.getString("id"));
                           //     jobmodel.setJob_id(job.getString("job_id"));
//                                jobmodel.setLocation(job.getString("location"));
                                jobmodel.setFirst_name(job.getString("first_name"));
                                jobmodel.setJob_applied_id(job.getString("job_applied_id"));
                                jobmodel.setCattype_id(job.getString("cattype_id"));
                                jobmodel.setWage(job.getString("wage"));
                                jobmodel.setNo(job.getString("no"));
                                jobmodel.setCat_english(job.getString("cat_english"));
                                jobmodel.setCat_hindi(job.getString("cat_hindi"));
                                jobmodel.setCat_marathi(job.getString("cat_marathi"));

                                joblist.add(jobmodel);
                            }
                        }

                        setAdapter(rv_accetjob);
                    }
                    else {
//                   rv_applyjob.setVisibility(View.INVISIBLE);
//                    btn_apply.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("cattype_id", cat_type);
                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }


    private void hitAcceptJob(final String id,final String json,final String usertype) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ACCEPTJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                joblist.clear();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        btn_accept.setVisibility(View.INVISIBLE);

                        Log.d(TAG, array.toString());

                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(getActivity(), ContractorConsole.class));

                    }
                    else {

                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("ruserid",id);
                params.put("accept_jobid", json);
                params.put("usertype", usertype);

                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }

}