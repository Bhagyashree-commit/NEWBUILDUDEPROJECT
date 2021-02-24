package com.blucorsys.app.labourcontractorapp.Labour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
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
import com.blucorsys.app.ServerCall.AppConfig;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.Contractor.CustomAdapter;
import com.blucorsys.app.labourcontractorapp.Contractor.PostJob;
import com.blucorsys.app.labourcontractorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LabourProfile extends AppCompatActivity {
    private static final String TAG = LabourProfile.class.getSimpleName();
Spinner spincat;
CustomLoader loader;
Preferences pref;
private ArrayList<String> categoryEng;
    private ArrayList<String> categoryID;
    LinearLayout linlay;
    CustomAdapter customAdapterTwo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_profile);
        spincat = findViewById(R.id.spinn_cat);
        linlay = findViewById(R.id.linlay);
        pref = new Preferences(this);
        categoryEng = new ArrayList<String>();
        categoryID = new ArrayList<String>();

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        getCategory();

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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
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
        spincat.setAdapter(new ArrayAdapter<String>(LabourProfile.this, android.R.layout.select_dialog_item, categoryEng));
    }
}