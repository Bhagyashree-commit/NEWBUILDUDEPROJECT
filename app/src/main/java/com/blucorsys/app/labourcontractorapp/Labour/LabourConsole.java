package com.blucorsys.app.labourcontractorapp.Labour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blucorsys.app.CustomComponent.Constants;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.R;


public class LabourConsole extends AppCompatActivity {
CardView cardprofile,cardapply;
TextView tv_name;
Preferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_console);
        cardprofile=findViewById(R.id.cardprofile);
        cardapply=findViewById(R.id.cardapply);
        tv_name=findViewById(R.id.tv_name);
        pref=new Preferences(this);
        tv_name.setText(pref.get(Constants.FIRSTNAME));
        cardprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LabourConsole.this, LabourProfile.class));
            }
        });
        cardapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LabourConsole.this, JobApplication.class));
            }
        });

    }
}