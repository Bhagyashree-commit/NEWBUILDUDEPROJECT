package com.blucorsys.app.labourcontractorapp.Contractor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.blucorsys.app.labourcontractorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackingActivity extends AppCompatActivity {
    private static final String TAG = TrackingActivity.class.getSimpleName();
    Preferences pref;
    CustomLoader loader;
    String type;
    ArrayList<JobModel> joblist;
    RecyclerView rv_tracklist;
    String selectedjob,laborid,labourname,lati,longi;
    JSONArray array;
    TextView btn_track;

    public static List<String> joblisttt  = new ArrayList<>();
    public static List<String> idlist  = new ArrayList<>();
    public static List<String> labornameist  = new ArrayList<>();
    public static List<String> latlist  = new ArrayList<>();
    public static List<String> lnglist  = new ArrayList<>();
    public static ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(this);
        type="CONTRACTOR";
        rv_tracklist=findViewById(R.id.rv_tracklist);
        btn_track=findViewById(R.id.btn_track);
        joblist = new ArrayList<JobModel>();
        getTrackList(pref.get(Constants.USERID),type);

        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(joblisttt.size()==0){
                    Toast.makeText(getApplicationContext(),
                            "Please Click Any One Check Box",
                            Toast.LENGTH_LONG).show();
                }
                else{

                    arrayList.clear();
                    HashMap<String,String> map;

                    for(int i=0;i<joblisttt.size();i++)
                    {

                        map=new HashMap<>();
                        map.put("lat",""+latlist.get(i));
                        map.put("lng",""+lnglist.get(i));
                        map.put("id",""+idlist.get(i));
                        map.put("name",""+labornameist.get(i));
                        arrayList.add(map);


                        Log.e("latitude",""+latlist);
                        Log.e("id",""+idlist.get(i));
                        Log.e("Laborname",""+labornameist.get(i));

                        Log.d("Testtttttt", "Array "+arrayList.toString());

                    }
                    Intent intent1 = new Intent(TrackingActivity.this, TrackLaborOnMap.class);
                    startActivity(intent1);
                }

                Log.d(TAG, "address "+joblisttt.toString());
            }



        });

    }

    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerview.setAdapter(new TrackingActivity.ApplyJobAdapter(joblist));
    }

    //*Recyclerview Adapter*//
    private class ApplyJobAdapter extends RecyclerView.Adapter<TrackingActivity.Holder> {
        private Context context;
        private List<JobModel> jobmodel;

        public ApplyJobAdapter(List<JobModel> jobmodel) {

            this.jobmodel = jobmodel;
        }

        public TrackingActivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TrackingActivity.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.track_list, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final TrackingActivity.Holder holder, final int position) {
            holder.itemView.setTag(jobmodel.get(position));

            final JobModel pu = jobmodel.get(position);
            holder.jobareainput.setText(pu.getLocation());
            holder.labortypeinput.setText(pu.getCat_english());
            holder.wagerateinput.setText(pu.getWage());
            holder.jobdateinput.setText(pu.getCreate_datetime());
            holder.lbname.setText(pu.getFirst_name());

            String address=pu.getLocation();


            holder.btncheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                    if(b){

                        selectedjob= pu.getJobid_details();
                        laborid= pu.getLabour_id();
                        labourname= pu.getFirst_name();
                        lati= pu.getLatitude();
                        longi= pu.getLongitude();
                        Log.e("AMOLf777",""+selectedjob);
                        joblisttt.add(selectedjob);
                        idlist.add(laborid);
                        labornameist.add(labourname);
                        latlist.add(lati);
                        lnglist.add(longi);
//

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

        ImageView ivCategory;
        TextView jobareainput;
        TextView labortypeinput;
        TextView wagerateinput;
        TextView jobdateinput;
        TextView lbname;

        ImageView mapmarker;
        CheckBox btncheckbox;

        public Holder(View itemView) {
            super(itemView);
//            ivCategory=itemView.findViewById(R.id.ivCategory);
            jobareainput=itemView.findViewById(R.id.jobareainput);
            labortypeinput=itemView.findViewById(R.id.labortypeinput);
            wagerateinput=itemView.findViewById(R.id.wagerateinput);
            jobdateinput=itemView.findViewById(R.id.jobdateinput);
            lbname=itemView.findViewById(R.id.lbname);

            mapmarker=itemView.findViewById(R.id.mapmarker);
            btncheckbox=itemView.findViewById(R.id.btncheckbox);
        }
    }

    private void getTrackList(final String userid,final String type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.TRACKLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                joblist.clear();

                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        btn_track.setVisibility(View.VISIBLE);

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
                                jobmodel.setJob_id(job.getString("job_id"));
                                jobmodel.setLocation(job.getString("location"));
                                jobmodel.setCreate_datetime(job.getString("create_datetime"));
                                jobmodel.setCattype_id(job.getString("cattype_id"));
                                jobmodel.setWage(job.getString("wage"));
                                jobmodel.setLabour_id(job.getString("labour_id"));
                                jobmodel.setCat_english(job.getString("cat_english"));
                                jobmodel.setCat_hindi(job.getString("cat_hindi"));
                                jobmodel.setCat_marathi(job.getString("cat_marathi"));
                                jobmodel.setTrack_jobid(job.getString("track_jobid"));
                                jobmodel.setLatitude(job.getString("latitude"));
                                jobmodel.setLongitude(job.getString("longitude"));
                                jobmodel.setFirst_name(job.getString("first_name"));

                                joblist.add(jobmodel);
                            }
                        }

                        // Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(getActivity(), ContractorConsole.class));
                        setAdapter(rv_tracklist);
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

}