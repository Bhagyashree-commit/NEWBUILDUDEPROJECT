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
import android.widget.LinearLayout;
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


public class ModifiedFragment extends Fragment {

    private static final String TAG = ModifiedFragment.class.getSimpleName();
    TextView tv_getaccpetedjob,tv_date,tv_ok;
    CustomLoader loader;
    Preferences pref;
    String type,credate;
    LinearLayout lin_lay24;
    Spinner spin_loc;
    String location;
    RecyclerView rv_acceptedoffer;
    private ArrayList<String> loca;
    private ArrayList<String> date;
    ArrayList<JobModel> joblist;

    public ModifiedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_modified, container, false);

        lin_lay24=v.findViewById(R.id.lin_lay24);
        spin_loc=v.findViewById(R.id.spin_loc);
        tv_date=v.findViewById(R.id.tv_date);
        tv_ok=v.findViewById(R.id.tv_ok);
        rv_acceptedoffer=v.findViewById(R.id.rv_acceptedoffer);

        loader = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(getActivity());
        loca = new ArrayList<String>();
        date = new ArrayList<String>();
        joblist = new ArrayList<JobModel>();
        type="CONTRACTOR";
        hitAPI(pref.get(Constants.USERID),type);


        spin_loc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                credate=date.get(position);
                location=spin_loc.getSelectedItem().toString();
                Log.e("nishaaa3",""+credate);
                tv_date.setText(credate);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitAPIOK(pref.get(Constants.USERID),type,location);
            }
        });

        return v;
    }


    private void hitAPI(final String userid,final String type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GETLOCBYCON, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {

                        lin_lay24.setVisibility(View.VISIBLE);
                        JSONArray array=object.getJSONArray("Show-Job");
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
        spin_loc.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, loca));
    }


    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(new ModifiedFragment.ApplyJobAdapter(joblist));
    }

    //*Recyclerview Adapter*//
    private class ApplyJobAdapter extends RecyclerView.Adapter<ModifiedFragment.Holder> {
        private Context context;
        private List<JobModel> jobmodel;

        public ApplyJobAdapter(List<JobModel> jobmodel) {

            this.jobmodel = jobmodel;
        }

        public ModifiedFragment.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ModifiedFragment.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_offer_item, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final ModifiedFragment.Holder holder, final int position) {
            holder.itemView.setTag(jobmodel.get(position));

            final JobModel pu = jobmodel.get(position);
            holder.tv_lname.setText(pu.getFirst_name());
            holder.tv_type.setText(pu.getCat_english());
            holder.tv_wage.setText(pu.getWage());
            holder.tv_no.setText(pu.getNo());


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
        TextView tv_type;
        TextView tv_wage;
        TextView tv_no;

        public Holder(View itemView) {
            super(itemView);
            tv_lname=itemView.findViewById(R.id.tv_lname);
            tv_type=itemView.findViewById(R.id.tv_type);
            tv_wage=itemView.findViewById(R.id.tv_wage);
            tv_no=itemView.findViewById(R.id.tv_number);

        }
    }


    private void hitAPIOK(final String userid,final String type,final String location) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GETLOCFILTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                joblist.clear();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

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
                                jobmodel.setId(job.getString("id"));
                                jobmodel.setFirst_name(job.getString("first_name"));
                                jobmodel.setLocation(job.getString("location"));

                                jobmodel.setCreate_datetime(job.getString("create_datetime"));
                                jobmodel.setCattype_id(job.getString("cattype_id"));
                                jobmodel.setWage(job.getString("wage"));
                                jobmodel.setNo(job.getString("no"));
                                jobmodel.setCat_english(job.getString("cat_english"));
                                jobmodel.setCat_hindi(job.getString("cat_hindi"));
                                jobmodel.setCat_marathi(job.getString("cat_marathi"));

                                joblist.add(jobmodel);
                            }
                        }

                        setAdapter(rv_acceptedoffer);
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
                params.put("ruserid", pref.get(Constants.USERID));
                params.put("usertype", type);
                params.put("location",location);

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