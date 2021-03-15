package com.blucorsys.app.labourcontractorapp.Labour;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.blucorsys.app.labourcontractorapp.Contractor.ContractorConsole;
import com.blucorsys.app.labourcontractorapp.Contractor.PostJob;
import com.blucorsys.app.labourcontractorapp.Contractor.TrackingActivity;
import com.blucorsys.app.labourcontractorapp.MainActivity;
import com.blucorsys.app.labourcontractorapp.R;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentRequest extends AppCompatActivity {
RecyclerView rv_paymentlist;
    private static final String TAG = PaymentRequest.class.getSimpleName();
    Preferences pref;
    CustomLoader loader;
    String type;
    String rating;
    ArrayList<JobModel> joblist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_request);
        rv_paymentlist=findViewById(R.id.rv_paymentlist);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(this);
        joblist = new ArrayList<JobModel>();
        type="LABOUR";
        getpaylist(pref.get(Constants.USERID),type);
    }
    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerview.setAdapter(new PaymentRequest.ApplyJobAdapter(joblist));
    }

    //*Recyclerview Adapter*//
    private class ApplyJobAdapter extends RecyclerView.Adapter<PaymentRequest.Holder> {
        private Context context;
        private List<JobModel> jobmodel;

        public ApplyJobAdapter(List<JobModel> jobmodel) {

            this.jobmodel = jobmodel;
        }

        public PaymentRequest.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PaymentRequest.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pay_list, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final PaymentRequest.Holder holder, final int position) {
            holder.itemView.setTag(jobmodel.get(position));

            final JobModel pu = jobmodel.get(position);
            holder.jobareainput.setText(pu.getLocation());
            holder.labortypeinput.setText(pu.getCat_english());
            holder.wagerateinput.setText(pu.getWage());
            holder.jobdateinput.setText(pu.getCreate_datetime());
            holder.tv_requestpay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(PaymentRequest.this, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alert);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    dialog.show();
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    //findId
                    TextView tvYes = (TextView) dialog.findViewById(R.id.tvOk);
                    SmileyRating smileyRating = (SmileyRating) dialog.findViewById(R.id.smile_rating);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialog.show();
                    smileyRating.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
                        @Override
                        public void onSmileySelected(SmileyRating.Type type) {
                             rating = String.valueOf(type.getRating());
                            Log.e("new","rating"+rating);
                        }
                    });


                    //set listener
                    tvYes.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(View v) {
                            String type="LABOUR";

                            hitRequestPayment(pref.get(Constants.USERID),type,pu.getTrack_jobid(),pu.getWage(),rating);
                            dialog.dismiss();
                        }
                    });
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
        SmileyRating smile_rating;
        ImageView mapmarker;
        CheckBox btncheckbox;

        public Holder(View itemView) {
            super(itemView);
//            ivCategory=itemView.findViewById(R.id.ivCategory);
            jobareainput=itemView.findViewById(R.id.jobareainput);
            labortypeinput=itemView.findViewById(R.id.labortypeinput);
            wagerateinput=itemView.findViewById(R.id.wagerateinput);
            jobdateinput=itemView.findViewById(R.id.jobdateinput);
            tv_requestpay=itemView.findViewById(R.id.tv_requestpay);
            smile_rating=itemView.findViewById(R.id.smile_rating);
            mapmarker=itemView.findViewById(R.id.mapmarker);
            btncheckbox=itemView.findViewById(R.id.btncheckbox);
        }
    }

    private void getpaylist(final String userid,final String type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAYLIST, new Response.Listener<String>() {
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
                                jobmodel.setJobid_details(job.getString("jobid_details"));
                              //  jobmodel.setJob_id(job.getString("job_id"));
                                jobmodel.setLocation(job.getString("location"));
                                jobmodel.setCreate_datetime(job.getString("create_datetime"));
                                jobmodel.setCattype_id(job.getString("cattype_id"));
                                jobmodel.setWage(job.getString("wage"));
//                                jobmodel.setNo(job.getString("no"));
                                jobmodel.setCat_english(job.getString("cat_english"));
                                jobmodel.setCat_hindi(job.getString("cat_hindi"));
                                jobmodel.setCat_marathi(job.getString("cat_marathi"));
                                jobmodel.setTrack_jobid(job.getString("track_jobid"));


                                joblist.add(jobmodel);
                            }
                        }

                        // Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(getActivity(), ContractorConsole.class));
                        setAdapter(rv_paymentlist);
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

    private void hitRequestPayment(final String userid,final String type,final String trackjobid,final String wages, final String rating) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAYREQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();

                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("success").equalsIgnoreCase("true")) {

                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();

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
                params.put("ruserid",userid);
                params.put("usertype", type);
                params.put("track_jobid", trackjobid);
                params.put("wages", wages);
                params.put("review", rating);

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