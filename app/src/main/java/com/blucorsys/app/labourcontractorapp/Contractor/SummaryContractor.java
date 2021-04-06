package com.blucorsys.app.labourcontractorapp.Contractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blucorsys.app.labourcontractorapp.R;

public class SummaryContractor extends AppCompatActivity {

    TextView tv_sumconso,tv_sumcatwise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_contractor);
        tv_sumconso=findViewById(R.id.tv_sumconso);
        tv_sumcatwise=findViewById(R.id.tv_sumcatwise);

        replaceFragmentWithAnimation(new summary_consofragment());

        tv_sumconso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentWithAnimation(new summary_consofragment());
            }
        });
        tv_sumcatwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentWithAnimation(new SumaryCatWise());
            }
        });

    }
    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.fram_summary, fragment);
        FragmentManager mFragmentManager=getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }


}