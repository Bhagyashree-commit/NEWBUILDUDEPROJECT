package com.blucorsys.app.labourcontractorapp.Labour;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.hsalf.smileyrating.SmileyRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPayment extends Fragment {

    RecyclerView rv_getpaylist;
    private static final String TAG = GetPayment.class.getSimpleName();
    Preferences pref;
    CustomLoader loader;
    String type,cdate,jobid;
    ArrayList<JobModel> joblist;
    public GetPayment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_get_payment, container, false);

        loader = new CustomLoader(getContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(getActivity());
        rv_getpaylist=v.findViewById(R.id.rv_getpaylist);
        type="LABOUR";
        joblist = new ArrayList<JobModel>();
        getpaylist(pref.get(Constants.USERID),type);

        return v;
    }


    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview.setAdapter(new GetPayment.ApplyJobAdapter(joblist));
    }

    //*Recyclerview Adapter*//
    private class ApplyJobAdapter extends RecyclerView.Adapter<GetPayment.Holder> {
        private Context context;
        private List<JobModel> jobmodel;

        public ApplyJobAdapter(List<JobModel> jobmodel) {

            this.jobmodel = jobmodel;
        }

        public GetPayment.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GetPayment.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.get_pay_list, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final GetPayment.Holder holder, final int position) {
            holder.itemView.setTag(jobmodel.get(position));

            final JobModel pu = jobmodel.get(position);
            holder.jobareainput.setText(pu.getLocation());
            holder.labortypeinput.setText(pu.getSite_detail());
            holder.wagerateinput.setText(pu.getWages_paid());
            holder.jobdateinput.setText(pu.getCreate_datetime());
            holder.tv_thankyou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    type="LABOUR";
                    jobid=pu.getJob_id();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cdate = simpledateformat.format(calendar.getTime());
                    Log.e("date",""+cdate);

                    endprocess(pu.getRuserid(),jobid,pu.getLabour_id());
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

        ImageView ivCategory;
        TextView jobareainput;
        TextView labortypeinput;
        TextView wagerateinput;
        TextView jobdateinput;
        TextView tv_requestpay;
        TextView tv_thankyou;


        public Holder(View itemView) {
            super(itemView);
//            ivCategory=itemView.findViewById(R.id.ivCategory);
            jobareainput=itemView.findViewById(R.id.jobareainput);
            labortypeinput=itemView.findViewById(R.id.labortypeinput);
            wagerateinput=itemView.findViewById(R.id.wagerateinput);
            jobdateinput=itemView.findViewById(R.id.jobdateinput);
            tv_requestpay=itemView.findViewById(R.id.tv_requestpay);
            tv_thankyou=itemView.findViewById(R.id.tv_thankyou);

        }
    }

    private void getpaylist(final String userid,final String type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GETPAYMETTOLABOUR, new Response.Listener<String>() {
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
                                 jobmodel.setLabour_id(job.getString("labour_id"));
                                jobmodel.setJobid_details(job.getString("jobid_details"));
                                  jobmodel.setJob_id(job.getString("job_id"));
                                jobmodel.setLocation(job.getString("location"));
                                jobmodel.setCreate_datetime(job.getString("create_datetime"));
                                jobmodel.setWages_paid(job.getString("wages_paid"));
                                jobmodel.setSite_detail(job.getString("site_detail"));
                               jobmodel.setRuserid(job.getString("ruserid"));
                               // jobmodel.setCat_english(job.getString("cat_english"));
                              //  jobmodel.setCat_hindi(job.getString("cat_hindi"));
                                //jobmodel.setCat_marathi(job.getString("cat_marathi"));
                             //   jobmodel.setTrack_jobid(job.getString("track_jobid"));


                                joblist.add(jobmodel);
                            }
                        }

                        setAdapter(rv_getpaylist);
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
                params.put("labour_id",userid);
                params.put("usertype", type);

                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }



    private void endprocess(final String userid,final String jobid,final String laborid) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ENDPROCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();

                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("success").equalsIgnoreCase("true")) {

                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();

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
                params.put("ruserid",userid);
                params.put("job_id", jobid);
                params.put("labour_id", laborid);


                Log.e("",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }

}