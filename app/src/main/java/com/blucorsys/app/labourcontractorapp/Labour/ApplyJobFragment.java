package com.blucorsys.app.labourcontractorapp.Labour;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApplyJobFragment extends Fragment {
    private static final String TAG = JobApplication.class.getSimpleName();
RecyclerView rv_applyjob;
Preferences pref;
CustomLoader loader;
    Spinner catspin,spin_loc;
    private ArrayList<String> categoryEng;
    private ArrayList<String> categoryID;
    private ArrayList<String> loca;
    private ArrayList<String> date;
    private ArrayList<String> jobid;
    String selectedjob;
    JSONArray array;

    public static List<String> joblisttt  = new ArrayList<>();
    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    TextView tv_applyjob,tv_appliedjob,btn_apply,tv_date;
    TextView tv_ok;
    EditText et_wages;
    ArrayList<JobModel> joblist;
    String location,cattype_id,credate;

    public ApplyJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_apply_job, container, false);
        rv_applyjob=v.findViewById(R.id.rv_applyjob);
        loader = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(getActivity());
       // hitApplyJobs();
        catspin=v.findViewById(R.id.spincategory);
        spin_loc=v.findViewById(R.id.spin_loc);
        tv_date=v.findViewById(R.id.tv_date);
        tv_applyjob=v.findViewById(R.id.tv_applyjob);
        tv_appliedjob=v.findViewById(R.id.tv_appliedjob);
        et_wages=v.findViewById(R.id.tv_wages);
        btn_apply=v.findViewById(R.id.btn_apply);
        tv_ok=v.findViewById(R.id.tv_ok);

        categoryEng = new ArrayList<String>();
        categoryID = new ArrayList<String>();
        loca = new ArrayList<String>();
        jobid = new ArrayList<String>();
        date = new ArrayList<String>();
        joblist = new ArrayList<JobModel>();



//        spin_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                create_datetime=spin_date.getSelectedItem().toString();
//                Log.e("nishaaa1",""+create_datetime);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });

        spin_loc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                credate=date.get(position);
                location=spin_loc.getSelectedItem().toString();
                Log.e("nishaaa2",""+location);
                tv_date.setText(credate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        catspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cattype_id=catspin.getSelectedItem().toString();
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
                // location="Unnamed Road, Vatkhale, Maharashtra 412409, India";
                // create_datetime="27/2/2021  4 : 25 AM";

                String wage=et_wages.getText().toString();
                // cattype_id="Unskilled Labour Female";
                if (!wage.isEmpty()){
                    hitAPI(location,credate,wage,cattype_id);
                }
                else
                {
                    Toast.makeText(getContext(),
                            "Please enter the wages!", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });


        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=pref.get(Constants.USERID);
                String jsonarr="";
                String type="LABOUR";
                hipApplyJob(id, String.valueOf(array),type);
            }
        });

        getCategory();
        getLocation();
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
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        catspin.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryEng));
    }

    private void getLocation() {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.GETLOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    //Parsing the fetched Json String to JSON Object
                    j = new JSONObject(response);

                    JSONArray jsonArray= j.getJSONArray(Constants.LOC);

                    //Calling method getStudents to get the students from the JSON Array
                    getlocations(jsonArray);
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

    private void getlocations(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                loca.add(json.getString(Constants.LOC));
                jobid.add(json.getString(Constants.JOBID));
                date.add(json.getString(Constants.careda));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spin_loc.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, loca));
    }

    private void getDate() {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.GETDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                JSONObject j = null;
                try {
                    //Parsing the fetched Json String to JSON Object
                    j = new JSONObject(response);

                    JSONArray jsonArray= j.getJSONArray(Constants.DATE);

                    //Calling method getStudents to get the students from the JSON Array
                  //  getDatess(jsonArray);
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

//    private void getDatess(JSONArray j){
//        //Traversing through all the items in the json array
//        for(int i=0;i<j.length();i++){
//            try {
//                //Getting json object
//                JSONObject json = j.getJSONObject(i);
//
//                //Adding the name of the student to array list
//                date.add(json.getString(Constants.DATE1));
//                jobid.add(json.getString(Constants.JOBID));
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        spin_date.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, date));
//    }




    public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(new ApplyJobAdapter(joblist));
    }

    //*Recyclerview Adapter*//
    private class ApplyJobAdapter extends RecyclerView.Adapter<Holder> {
        private Context context;
        private List<JobModel> jobmodel;

        public ApplyJobAdapter(List<JobModel> jobmodel) {

            this.jobmodel = jobmodel;
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.apply_job_item, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {
            holder.itemView.setTag(jobmodel.get(position));

            final JobModel pu = jobmodel.get(position);
            holder.jobareainput.setText(pu.getLocation());
            holder.jobdateinput.setText(pu.getCreate_datetime());
            holder.wagerateinput.setText(pu.getWage());
            holder.labortypeinput.setText(pu.getCat_english());
            holder.cratinginput.setText("3.0");
            holder.tv_counter.setText("COUNTER" + "-" +pu.getNo());
            String address=pu.getLocation();

            holder.mapmarker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(), MapsActivity.class);
//                    startActivity(intent);

                 /// getaddress(address);

                }
            });
            holder.btncheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                    if(b){

                         selectedjob= pu.getJobid_details();
                        Log.e("AMOLf777",""+selectedjob);
                        joblisttt.add(selectedjob);
                        HashMap<String,String> map;
                        map=new HashMap<>();

                        map.put("jobid_details",""+selectedjob);
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

        ImageView ivCategory;
        TextView jobareainput;
        TextView cratinginput;
        TextView labortypeinput;
        TextView wagerateinput;
        TextView jobdateinput;
        TextView tv_counter;
        ImageView mapmarker;
        CheckBox btncheckbox;

        public Holder(View itemView) {
            super(itemView);
//            ivCategory=itemView.findViewById(R.id.ivCategory);
            jobareainput=itemView.findViewById(R.id.jobareainput);
            cratinginput=itemView.findViewById(R.id.cratinginput);
            labortypeinput=itemView.findViewById(R.id.labortypeinput);
            wagerateinput=itemView.findViewById(R.id.wagerateinput);
            jobdateinput=itemView.findViewById(R.id.jobdateinput);
            tv_counter=itemView.findViewById(R.id.tv_counter);
            mapmarker=itemView.findViewById(R.id.mapmarker);
            btncheckbox=itemView.findViewById(R.id.btncheckbox);
        }
    }



    private void hitAPI(final String location,final String create_datetime,final String wage,final String cattype_id) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.APPLYJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                joblist.clear();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        btn_apply.setVisibility(View.VISIBLE);
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
                                jobmodel.setJobid_details(job.getString("jobid_details"));
                                jobmodel.setJob_id(job.getString("job_id"));
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

                       // Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(getActivity(), ContractorConsole.class));
                        setAdapter(rv_applyjob);
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
                params.put("location",location);
                params.put("create_datetime", credate);
                params.put("wage", wage);
                params.put("cattype_id", cattype_id);
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


public  void getaddress(String address){


    Geocoder coder = new Geocoder(getContext());
    try {
        ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName("Your Address", 50);
        for(Address add : adresses){
           //Controls to ensure it is right address such as country etc.
                double longitude = add.getLongitude();
                double latitude = add.getLatitude();

        }
    } catch (IOException e) {
        e.printStackTrace();
    }

}

    private void hipApplyJob(final String id,final String json,final String type) {
        loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.APPLIEDJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                loader.dismiss();
                joblist.clear();
                JSONObject j = null;
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("Success").equalsIgnoreCase("true")) {
                        btn_apply.setVisibility(View.INVISIBLE);

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
                params.put("ruserid",pref.get(Constants.USERID));
                params.put("job_applied_id", json);
                params.put("usertype",type);

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