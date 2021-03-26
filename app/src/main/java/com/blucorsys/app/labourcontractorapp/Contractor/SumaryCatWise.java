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


public class SumaryCatWise extends Fragment {
    private static final String TAG = SumaryCatWise.class.getSimpleName();
    Preferences pref;
    CustomLoader loader;
    RecyclerView rv_summcat;
    String usertype,credate,cati;
    TextView tv_sdate,tv_ok,tv_edate;
    Spinner spin_loc,spincat;
    String location,dates,cattype_id;
    private ArrayList<String> categoryEng;
    private ArrayList<String> categoryMar;
    private ArrayList<String> categoryHin;
    private ArrayList<String> categoryID;
    private ArrayList<String> loca;
    private ArrayList<String> date;
    ArrayList<JobModel> joblist;

    public SumaryCatWise() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_sumary_cat_wise, container, false);
        loader = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(getActivity());
        rv_summcat=v.findViewById(R.id.rv_summcat);
        tv_sdate=v.findViewById(R.id.tv_sdate);
        spincat=v.findViewById(R.id.spin_cat);
        tv_ok=v.findViewById(R.id.tv_ok);

        spin_loc=v.findViewById(R.id.spin_loc);
        usertype="CONTRACTOR";
        loca = new ArrayList<String>();
        date = new ArrayList<String>();
        joblist = new ArrayList<JobModel>();
        categoryEng = new ArrayList<String>();
        categoryID = new ArrayList<String>();
        categoryMar = new ArrayList<String>();
        categoryHin = new ArrayList<String>();



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

        getloc(pref.get(Constants.USERID),usertype);
        getCategory();

        spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cati=categoryID.get(position);
                cattype_id=spincat.getSelectedItem().toString();
                Log.e("nishaaa344",""+cati);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitSummary(pref.get(Constants.USERID),location,usertype,cati);
            }
        });
        return v;
    }
    private void getloc(final String userid,final String type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SITEWISEFILTER, new Response.Listener<String>() {
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
                params.put("ruserid",userid);
                params.put("usertype", type);
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
        spin_loc.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, loca
        ));

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
                categoryMar.add(json.getString(Constants.CATMAR));
                categoryHin.add(json.getString(Constants.CATHIN));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(pref.get(Constants.Lang).equals("ENGLISH")){
            spincat.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryEng));

        }
        else if(pref.get(Constants.Lang).equals("हिंदी")){
            spincat.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryHin));

        }
        else {
            spincat.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryMar));

        }
    }



    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(new SumaryCatWise.AcceptJobAdapter(joblist));
    }

    //*Recyclerview Adapter*//
    private class AcceptJobAdapter extends RecyclerView.Adapter<SumaryCatWise.Holder> {
        private Context context;
        private List<JobModel> jobmodel;

        public AcceptJobAdapter(List<JobModel> jobmodel) {

            this.jobmodel = jobmodel;
        }

        public SumaryCatWise.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SumaryCatWise.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_list, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final SumaryCatWise.Holder holder, final int position) {
            holder.itemView.setTag(jobmodel.get(position));

            final JobModel pu = jobmodel.get(position);

            holder.tv_sdetailsinput.setText(pu.getSite_detail());
            holder.tv_sdatee.setText(pu.getCreate_datetime());
            holder.tv_wagesinput.setText(pu.getWages_paid());
            holder.tv_dwageinput.setText("0");
            holder.tv_lnameinput.setText(pu.getFirst_name());

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

        TextView tv_sdetailsinput,tv_sdatee,tv_wagesinput,tv_dwageinput,tv_lnameinput;



        public Holder(View itemView) {
            super(itemView);
            tv_sdetailsinput=itemView.findViewById(R.id.tv_sdetailsinput);
            tv_sdatee=itemView.findViewById(R.id.tv_sdatee);
            tv_wagesinput=itemView.findViewById(R.id.tv_wagesinput);
            tv_dwageinput=itemView.findViewById(R.id.tv_dwageinput);
            tv_lnameinput=itemView.findViewById(R.id.tv_lnameinput);



        }
    }

    private void hitSummary(final String userid,final String location,final String usertype,final String catid) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SITEWISEFILTER4, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                joblist.clear();
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
                                jobmodel.setWages_paid(job.getString("wages_paid"));
                                jobmodel.setFirst_name(job.getString("first_name"));


                                joblist.add(jobmodel);
                            }
                        }

                        setAdapter(rv_summcat);
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
                params.put("ruserid", userid);
                params.put("location", location);
                params.put("usertype", usertype);
                params.put("cattype_id", catid);
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