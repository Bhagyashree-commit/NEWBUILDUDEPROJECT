package com.blucorsys.app.labourcontractorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.blucorsys.app.CustomComponent.Constants;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.Contractor.ContractorConsole;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    Spinner langspinner;
    VideoView vid;
    TextView btncontinue;
    TextView btn_process;
    TextView btn_info;
    Preferences pref;
    Locale myLocale;
    String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        langspinner=findViewById(R.id.spin_lang);
        btncontinue=findViewById(R.id.btn_cont);
        btn_process=findViewById(R.id.btn_process);
        btn_info=findViewById(R.id.btn_info);

        List<String> list = new ArrayList();
        list.add("LANGUAGE");
        list.add("ENGLISH");
        list.add("हिंदी");
        list.add("मराठी");
pref=new Preferences(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langspinner.setAdapter(adapter);

        langspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String lang = MainActivity.this.langspinner.getSelectedItem().toString();
                MainActivity.this.pref.set(Constants.Lang, lang);
                MainActivity.this.pref.commit();
                Log.e("", "" + MainActivity.this.pref.get(Constants.Lang));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btncontinue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MainActivity.this.pref.get(Constants.Lang).equalsIgnoreCase("Language")) {
                    Toast.makeText(MainActivity.this, "please select any one language", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("ttttttt", MainActivity.this.pref.get(Constants.Lang));
                    MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

            }
        });
        this.btn_process.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://example.i-tech.consulting/Buildude/dist/media/1613458569PROMO_2_English.mov")));
                Log.i("Video", "Video Playing....");
            }
        });
        this.btn_info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(Uri.parse("http://example.i-tech.consulting/Buildude/dist/media/1613458569City_Gateway_Brochure.pdf"), "text/html");
                MainActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}