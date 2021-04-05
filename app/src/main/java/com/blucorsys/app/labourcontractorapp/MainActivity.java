package com.blucorsys.app.labourcontractorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    String lang;
    String currentLanguage;
    ImageView share;
    LinearLayout openLink;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        langspinner=findViewById(R.id.spin_lang);
        btncontinue=findViewById(R.id.btn_cont);
        btn_process=findViewById(R.id.btn_process);
        btn_info=findViewById(R.id.btn_info);
        share=findViewById(R.id.share);
        openLink=findViewById(R.id.ll_shantal);


        ObjectAnimator anim = ObjectAnimator.ofInt(openLink, "backgroundColor", Color.WHITE, Color.RED,
                Color.WHITE);
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here");
                String app_url = " https://drive.google.com/file/d/1qnIAtbiBw4St_HKagdUE5-2-VFlfLlOc/view?usp=sharing";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        openLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        OpenWebsite.class);
                startActivity(i);
            }
        });

        btncontinue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e("ttttttt11", lang);
                if (lang.equalsIgnoreCase(pref.get(Constants.Lang))) {
                    Toast.makeText(MainActivity.this, "please select any one language", Toast.LENGTH_LONG).show();
                } else {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
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
                lang =langspinner.getSelectedItem().toString();
//                MainActivity.this.pref.set(Constants.Lang, lang);
//                MainActivity.this.pref.commit();

                switch (position) {
                    case 0:

                        break;
                    case 1:
                        MainActivity.this.pref.set(Constants.Lang, lang);
                        MainActivity.this.pref.commit();
//                        pref.set("Lang","en");
//                        pref.commit();
                        setLocale("en");
                        break;
                    case 2:
                        MainActivity.this.pref.set(Constants.Lang, lang);
                        MainActivity.this.pref.commit();
//                        pref.set("Lang","hi");
//                        pref.commit();
                        setLocale("hi");
                        break;
                    case 3:
                        MainActivity.this.pref.set(Constants.Lang, lang);
                        MainActivity.this.pref.commit();
//                        pref.set("Lang","mar");
//                        pref.commit();
                        setLocale("mar");
                        break;
                }

                Log.e("anhgjh", "" + MainActivity.this.pref.get(Constants.Lang));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
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
    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.putExtra(currentLanguage, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(MainActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}