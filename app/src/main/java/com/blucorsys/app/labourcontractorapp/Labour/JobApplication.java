package com.blucorsys.app.labourcontractorapp.Labour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.blucorsys.app.ServerCall.AppConfig;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JobApplication extends AppCompatActivity {
    private static final String TAG = JobApplication.class.getSimpleName();
CustomLoader loader;
Preferences pref;
Spinner catspin,spin_loc,spin_date;
    private ArrayList<String> categoryEng;
    private ArrayList<String> categoryID;
    private ArrayList<String> loca;
    private ArrayList<String> date;
    private ArrayList<String> jobid;
    TextView tv_applyjob,tv_appliedjob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_application);
        catspin=findViewById(R.id.spincategory);
        spin_loc=findViewById(R.id.spin_loc);
        spin_date=findViewById(R.id.spin_date);
        tv_applyjob=findViewById(R.id.tv_applyjob);
        tv_appliedjob=findViewById(R.id.tv_appliedjob);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(this);
        categoryEng = new ArrayList<String>();
        categoryID = new ArrayList<String>();
        loca = new ArrayList<String>();
        jobid = new ArrayList<String>();
        date = new ArrayList<String>();

        replaceFragmentWithAnimation(new ApplyJobFragment());

        tv_applyjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentWithAnimation(new ApplyJobFragment());
            }
        });
        tv_appliedjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentWithAnimation(new AppliedJobFragment());
            }
        });
    }



    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
       // transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame_jobapply, fragment);
        FragmentManager mFragmentManager=getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }

}